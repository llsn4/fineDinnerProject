package cn.dinner.order.service.impl;

import cn.dinner.order.common.CustomException;
import cn.dinner.order.dto.SetmealDto;
import cn.dinner.order.entity.Dish;
import cn.dinner.order.entity.Setmeal;
import cn.dinner.order.entity.SetmealDish;
import cn.dinner.order.mapper.SetmealMapper;
import cn.dinner.order.service.SetmealDishService;
import cn.dinner.order.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    public SetmealDto getSetmealWithDish(Long id) {
        SetmealDto setmealDto = new SetmealDto();
        Setmeal setmeal = this.getById(id);
        BeanUtils.copyProperties(setmeal,setmealDto);
        LambdaQueryWrapper<SetmealDish> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(list);
        return setmealDto;



    }

    @Transactional
    @Override
    public void updateSetmealWithDish(SetmealDto setmealDto) {
        this.updateById(setmealDto);
        List<SetmealDish> dishList = setmealDto.getSetmealDishes();
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(queryWrapper);
        dishList.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(dishList);


    }

    @Override
    public void updateOnlyStatus(int status, Long[] ids) {
        List<Setmeal>list=new ArrayList<>();
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
    public void deleteWithDish(List<Long> ids) {
        //查找是否有套餐未停售
            LambdaQueryWrapper<Setmeal> queryWrapper1=new LambdaQueryWrapper<>();
            queryWrapper1.eq(Setmeal::getStatus,1);
            queryWrapper1.in(Setmeal::getId,ids);
            if(this.count(queryWrapper1)>0){
                throw new CustomException("有的套餐处于启售状态，无法删除");
            }
            this.removeByIds(ids);
            //删除对应的套餐菜品
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(queryWrapper);




    }

    @Transactional
    @Override
    public void saveSetmealWithDish(SetmealDto setmealDto) {
        //保存套餐信息
        this.save(setmealDto);
        //保存套餐菜品的关联信息
        List<SetmealDish> dishList = setmealDto.getSetmealDishes();
        dishList.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(dishList);


    }
}
