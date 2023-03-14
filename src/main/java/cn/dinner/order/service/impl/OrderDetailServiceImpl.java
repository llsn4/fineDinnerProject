package cn.dinner.order.service.impl;

import cn.dinner.order.entity.OrderDetail;
import cn.dinner.order.mapper.OrderDetailMapper;
import cn.dinner.order.mapper.OrderMapper;
import cn.dinner.order.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
