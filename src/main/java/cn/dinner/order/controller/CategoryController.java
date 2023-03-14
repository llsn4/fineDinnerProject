package cn.dinner.order.controller;

import cn.dinner.order.common.R;
import cn.dinner.order.entity.Category;
import cn.dinner.order.entity.Employee;
import cn.dinner.order.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 86139
 * 分类管理
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("新增分类：{}",category);
        categoryService.save(category);
        return R.success("新增成功");


    }


    /**
     * 分页查询分类
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
       Page pageInfo=new Page(page,pageSize);
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,queryWrapper);
        log.info("分页查询分类");
        return R.success(pageInfo);

    }
    @DeleteMapping
    public  R<String> delete(Long ids){
        log.info("删除id为{}",ids);
        categoryService.remove(ids);
        return R.success("删除成功");

    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        log.info("修改成功,id为{}",category.getId());
        return R.success("修改成功");
    }

    /**
     *根据条件查询分类数据
     * @param  category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        //排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);


    }


}
