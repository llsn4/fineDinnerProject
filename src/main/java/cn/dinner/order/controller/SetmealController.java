package cn.dinner.order.controller;

import cn.dinner.order.common.R;
import cn.dinner.order.dto.SetmealDto;
import cn.dinner.order.entity.Setmeal;
import cn.dinner.order.service.CategoryService;
import cn.dinner.order.service.SetmealDishService;
import cn.dinner.order.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 86139
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @CacheEvict(value = "setmealCache" , allEntries = true)
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveSetmealWithDish(setmealDto);

        return R.success("新增成功");



    }
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //需要展示到前端的   dto
        Page<SetmealDto> pageInfo=new Page<>(page,pageSize);
        //从数据库拿到的     setmeal
        Page<Setmeal> pageOrigin=new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageOrigin,queryWrapper);
        //排序完成，开始进行赋值
        BeanUtils.copyProperties(pageOrigin,pageInfo,"records");
        List<Setmeal> records = pageOrigin.getRecords();
        List<SetmealDto> list=  records.stream().map((item)->{
          SetmealDto setmealDto=new SetmealDto();
          BeanUtils.copyProperties(item,setmealDto);
          String name1 = categoryService.getById(setmealDto.getCategoryId()).getName();
          setmealDto.setCategoryName(name1);
          return setmealDto;

      }).collect(Collectors.toList());
      pageInfo.setRecords(list);
      return R.success(pageInfo);


    }
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getSetmealWithDish(id);
        return R.success(setmealDto);
    }
    @CacheEvict(value = "setmealCache" , allEntries = true)
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateSetmealWithDish(setmealDto);
        return R.success("修改成功");
    }
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable("status") int status,Long []ids){
        setmealService.updateOnlyStatus(status,ids);
        return R.success("修改成功");
    }
    //allEntries = true  删除setmealCache下所有的缓存数据
    @CacheEvict(value = "setmealCache" , allEntries = true)
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long>ids){
        setmealService.deleteWithDish(ids);
        return R.success("删除成功");
    }
    @Cacheable(value = "setmealCache" ,key = "#setmealDto.categoryId+'_'+#setmealDto.status")
    @GetMapping("/list")
    public R<List<SetmealDto>> list(SetmealDto setmealDto){
        //caId   status
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(setmealDto.getCategoryId()!=null,Setmeal::getCategoryId,setmealDto.getCategoryId());
        lambdaQueryWrapper.eq(setmealDto.getStatus()!=null,Setmeal::getStatus,setmealDto.getStatus());
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(lambdaQueryWrapper);
       List<SetmealDto> listR= list.stream().map((item)->{
            SetmealDto dto= setmealService.getSetmealWithDish(item.getId());
            return dto;
        }).collect(Collectors.toList());
       return R.success(listR);

    }
    @GetMapping("/dish/{id}")
    public R<SetmealDto> getDish(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getSetmealWithDish(id);
        return R.success(setmealDto);
    }

}
