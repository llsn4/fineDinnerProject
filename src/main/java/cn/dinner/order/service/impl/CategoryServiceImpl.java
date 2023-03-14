package cn.dinner.order.service.impl;

import cn.dinner.order.common.CustomException;
import cn.dinner.order.entity.Category;
import cn.dinner.order.entity.Dish;
import cn.dinner.order.entity.Setmeal;
import cn.dinner.order.mapper.CategoryMapper;
import cn.dinner.order.service.CategoryService;
import cn.dinner.order.service.DishService;
import cn.dinner.order.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 86139
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{
   @Autowired
   private DishService dishService;
   @Autowired
   private SetmealService setmealService;


    /**
     * 根据id删除分类，在之前要进行判断是否关联菜品或套餐
     * @param id
     */
    @Override
    public void remove(Long id) {
        //查询是否关联菜品，如果关联，抛出业务异常
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,id);
      if(dishService.count(queryWrapper)>0){
          //关联
          log.info("有菜品关联此分类，无法删除");
          throw new CustomException("有菜品关联此分类，无法删除");
      }
        //查询是否关联套餐，如果关联，抛出业务异常
        LambdaQueryWrapper<Setmeal> queryWrapper2=new LambdaQueryWrapper<>();
       queryWrapper2.eq(Setmeal::getCategoryId,id);
        if(setmealService.count(queryWrapper2)>0){
            //关联
            log.info("有套餐关联此分类，无法删除");
            throw new CustomException("有套餐关联此分类，无法删除");
        }
        //正常删除
        super.removeById(id);

    }
}
