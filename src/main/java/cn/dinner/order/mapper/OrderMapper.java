package cn.dinner.order.mapper;

import cn.dinner.order.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.core.annotation.Order;
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
