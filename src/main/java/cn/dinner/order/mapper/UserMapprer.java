package cn.dinner.order.mapper;

import cn.dinner.order.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapprer extends BaseMapper<User> {
}
