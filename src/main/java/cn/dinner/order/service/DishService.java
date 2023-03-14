package cn.dinner.order.service;

import cn.dinner.order.dto.DishDto;
import cn.dinner.order.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DishService extends IService<Dish> {

    /**
     * 新增菜品，同时插入菜品对应的口味数据，操作两张表，dish dish_flavor
     */
    void saveWithFlavor(DishDto dishDto);

    /**
     * 根据id获取dto对象
     * @param id
     * @return
     */
    DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);
    void deleteWithFlavor(List<Long> ids);
    void updateOnlyStatus(int status,Long[] ids);
}
