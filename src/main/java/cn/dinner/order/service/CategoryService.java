package cn.dinner.order.service;

import cn.dinner.order.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 86139
 */
public interface CategoryService extends IService<Category> {
     void remove(Long id);


}
