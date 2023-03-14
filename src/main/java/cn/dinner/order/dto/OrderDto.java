package cn.dinner.order.dto;

import cn.dinner.order.entity.Orders;
import lombok.Data;

@Data
public class OrderDto extends Orders {
    private int sumNum;
    private String userName;
}
