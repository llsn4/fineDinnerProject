package cn.dinner.order.service;

import cn.dinner.order.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.core.annotation.Order;

public interface OrderService extends IService<Orders> {
    void  submit(Orders orders);
}
