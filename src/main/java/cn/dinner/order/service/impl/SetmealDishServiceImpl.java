package cn.dinner.order.service.impl;

import cn.dinner.order.entity.Setmeal;
import cn.dinner.order.entity.SetmealDish;
import cn.dinner.order.mapper.SetmealDishMapper;
import cn.dinner.order.mapper.SetmealMapper;
import cn.dinner.order.service.SetmealDishService;
import cn.dinner.order.service.SetmealService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
