package cn.dinner.order.controller;

import cn.dinner.order.common.R;
import cn.dinner.order.dto.DishDto;
import cn.dinner.order.entity.Category;
import cn.dinner.order.entity.Dish;
import cn.dinner.order.entity.DishFlavor;
import cn.dinner.order.service.CategoryService;
import cn.dinner.order.service.DishFlavorService;
import cn.dinner.order.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    DishService dishService;
    @Autowired
    DishFlavorService dishFlavorService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);
        String key="dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);
        return R.success("添加成功");

    }

    /**
     * 查询dish相关信息
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //分页构造器
        Page <Dish>pageInfo=new Page(page,pageSize);
        Page<DishDto> dishDtoPage=new Page<>();
        //条件构造器
        LambdaQueryWrapper <Dish>queryWrapper=new LambdaQueryWrapper();
        //排序条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo,queryWrapper);
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> dishList=pageInfo.getRecords();
        List<DishDto> list= dishList.stream().map((item)->{
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category byId = categoryService.getById(categoryId);
            log.info("{}",byId.toString());
            String categoryName = byId.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable("id") Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
       return R.success(dishDto);


    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){

        dishService.updateWithFlavor(dishDto);
        //清理所有菜品的缓存数据
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);

        return R.success("修改成功");
    }
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable("status") int status,Long []ids){
        dishService.updateOnlyStatus(status,ids);
        return R.success("修改成功");
    }
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        dishService.deleteWithFlavor(ids);
       return R.success("删除成功");
    }

    /**
     * 根据条件查询对应的菜品
     * @param dish
     * @return
     */

    @GetMapping("/list")
    public R<List<DishDto>> getByCategory(Dish dish){
        List<DishDto>  dishDtoList=null;
        String key="dish_"+dish.getCategoryId()+"_"+dish.getStatus();
        //从redis中获取缓存数据，如果存在直接返回，不存在的话进行查询
         dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
         if(dishDtoList!=null){
             //如果存在直接返回
             return R.success(dishDtoList);
         }
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper();
        //构造查询条件
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.like(dish.getName()!=null,Dish::getName,dish.getName());
        //排除停售的菜品
        queryWrapper.eq(Dish::getStatus,1);
        //构造排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);


        dishDtoList = list.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,item.getId());
            List<DishFlavor> list1 = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(list1);
            return dishDto;

        }).collect(Collectors.toList());
        //不存在的话进行查询并存到redis中
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);

       return R.success(dishDtoList);
    }
   /* @GetMapping("/list")
    public R<List<Dish>> getByCategory(Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper();
        //构造查询条件
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.like(dish.getName()!=null,Dish::getName,dish.getName());
        //排除停售的菜品
        queryWrapper.eq(Dish::getStatus,1);
        //构造排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);
    }*/

}
