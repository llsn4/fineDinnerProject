package cn.dinner.order.service.impl;

import cn.dinner.order.entity.ShoppingCart;
import cn.dinner.order.mapper.ShoppingCartMapper;
import cn.dinner.order.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
