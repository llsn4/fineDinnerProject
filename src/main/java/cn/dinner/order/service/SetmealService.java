package cn.dinner.order.service;

import cn.dinner.order.dto.SetmealDto;
import cn.dinner.order.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 同时加入到套餐类和套餐菜品类中
     * @param setmealDto
     */
    void saveSetmealWithDish(SetmealDto setmealDto);
    SetmealDto getSetmealWithDish(Long id);
    void updateSetmealWithDish(SetmealDto setmealDto);
    void updateOnlyStatus(int status,Long []ids);
    void deleteWithDish(List<Long> ids);
}
