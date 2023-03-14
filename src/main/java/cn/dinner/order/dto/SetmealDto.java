package cn.dinner.order.dto;

import cn.dinner.order.entity.Setmeal;
import cn.dinner.order.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
