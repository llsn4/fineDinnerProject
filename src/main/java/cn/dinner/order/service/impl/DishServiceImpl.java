package cn.dinner.order.service.impl;

import cn.dinner.order.common.CustomException;
import cn.dinner.order.dto.DishDto;
import cn.dinner.order.entity.Dish;
import cn.dinner.order.entity.DishFlavor;
import cn.dinner.order.entity.Setmeal;
import cn.dinner.order.entity.SetmealDish;
import cn.dinner.order.mapper.DishMapper;
import cn.dinner.order.service.DishFlavorService;
import cn.dinner.order.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 86139
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     *新增菜品同时保存对应的口味数据
     * @param  dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品基本信息
        super.save(dishDto);
        //保存后MP会分配id
        Long id = dishDto.getId();
        //
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item)->{
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);


    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        dishDto.setFlavors(list);
        return dishDto;
    }


    @Override
    public void updateOnlyStatus(int status, Long[] ids) {
        List<Dish>list=new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            list.add(this.getById(ids[i]));
        }
        list.stream().map((item)->{
            item.setStatus(status);
            return item;
        }).collect(Collectors.toList());
        this.updateBatchById(list);
    }

    @Transactional
    @Override
    public void deleteWithFlavor(List<Long> ids) {
        //获取所有需要删除的菜品并加到list中
        LambdaQueryWrapper<Dish> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.eq(Dish::getStatus,1);
        queryWrapper1.in(Dish::getId,ids);
        if(this.count(queryWrapper1)>0){
            throw new CustomException("有的菜品处于启售状态，无法删除");
        }
        this.removeByIds(ids);
        //删除对应的套餐菜品
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(DishFlavor::getDishId,ids);
        dishFlavorService.remove(queryWrapper);

    }

    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //修改dish
        this.updateById(dishDto);
        List<DishFlavor> flavors = dishDto.getFlavors();
        //删除DishFlavor
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //添加DishFlavor
        flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);


    }


}
