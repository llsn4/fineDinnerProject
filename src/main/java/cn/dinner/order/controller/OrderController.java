package cn.dinner.order.controller;

import cn.dinner.order.common.BaseContext;
import cn.dinner.order.common.R;
import cn.dinner.order.dto.OrderDto;
import cn.dinner.order.entity.OrderDetail;
import cn.dinner.order.entity.Orders;
import cn.dinner.order.service.OrderDetailService;
import cn.dinner.order.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        orderService.submit(orders);
        return R.success("成功");

    }
    @GetMapping("/userPage")
    public R<Page> page(int page,int pageSize){
        Long userId = BaseContext.getCurrentId();
        Page<Orders> pageOrder=new Page<>(page,pageSize);
        Page<OrderDto> pageInfo=new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper=new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.eq(Orders::getUserId,userId);
        ordersLambdaQueryWrapper.orderByDesc(Orders::getCheckoutTime);
        orderService.page(pageOrder,ordersLambdaQueryWrapper);
        BeanUtils.copyProperties(pageOrder,pageInfo,"records");
      List<OrderDto> list= orderService.list(ordersLambdaQueryWrapper).stream().map((item)->{
            LambdaQueryWrapper<OrderDetail> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(OrderDetail::getOrderId,item.getId());
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(item,orderDto);
            orderDto.setSumNum(orderDetailService.count(queryWrapper));
            return orderDto;
        }).collect(Collectors.toList());
        pageInfo.setRecords(list);

        return R.success(pageInfo);



    }
    @GetMapping("/page")
    public R<Page> pageBack(int page,int pageSize){
        Page<Orders> pageOrder=new Page<>(page,pageSize);
        Page<OrderDto> pageInfo=new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper=new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.orderByDesc(Orders::getCheckoutTime);
        orderService.page(pageOrder,ordersLambdaQueryWrapper);
        BeanUtils.copyProperties(pageOrder,pageInfo,"records");
        List<OrderDto> list= orderService.list(ordersLambdaQueryWrapper).stream().map((item)->{
            LambdaQueryWrapper<OrderDetail> queryWrapper=new LambdaQueryWrapper<>();
            log.info("{}",item.toString());
            queryWrapper.eq(OrderDetail::getOrderId,item.getId());
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(item,orderDto);
            orderDto.setUserName(orderDto.getUserId().toString());
            orderDto.setSumNum(orderDetailService.count(queryWrapper));
            return orderDto;
        }).collect(Collectors.toList());
        pageInfo.setRecords(list);

        return R.success(pageInfo);



    }
    @PutMapping
    public R<String> deliver(@RequestBody Orders orders){
//        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();
//        queryWrapper.eq(Orders::getId,id);
//        Orders one = orderService.getOne(queryWrapper);
//        one.setStatus(status);
        orderService.updateById(orders);
        return R.success("修改完成");

    }
}
