package cn.dinner.order.service.impl;

import cn.dinner.order.entity.Employee;
import cn.dinner.order.mapper.EmployeeMapper;
import cn.dinner.order.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 86139
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
