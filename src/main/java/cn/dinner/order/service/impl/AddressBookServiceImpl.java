package cn.dinner.order.service.impl;

import cn.dinner.order.entity.AddressBook;
import cn.dinner.order.mapper.AddressBookMapper;
import cn.dinner.order.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
