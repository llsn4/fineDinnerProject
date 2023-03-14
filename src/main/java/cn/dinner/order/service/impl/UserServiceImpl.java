package cn.dinner.order.service.impl;

import cn.dinner.order.entity.User;
import cn.dinner.order.mapper.UserMapprer;
import cn.dinner.order.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapprer, User> implements UserService {
}
