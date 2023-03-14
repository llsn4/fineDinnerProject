package cn.dinner.order.controller;

import cn.dinner.order.common.BaseContext;
import cn.dinner.order.common.R;
import cn.dinner.order.entity.ShoppingCart;
import cn.dinner.order.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService service;

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = service.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        //设置用户id
        Long userId = BaseContext.getCurrentId();
        //查询当前的菜品或套餐是否在购物车中
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        if(shoppingCart.getDishId()!=null){
            //菜品
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }
        else {
            //套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart one = service.getOne(queryWrapper);
        if( one!=null){
            //如果存在，则增加数量
            one.setNumber(one.getNumber()+1);
            service.updateById(one);
        }
        //如果不存在，则加入到数据库中
        else {
            shoppingCart.setUserId(userId);
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            service.save(shoppingCart);
        }
        return R.success(one);



    }

    /**
     * 减少购物车中物品数量
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public  R<String> sub(@RequestBody ShoppingCart shoppingCart){
        log.info("受到的信息{}",shoppingCart);
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        Long userId = BaseContext.getCurrentId();
        if(shoppingCart.getDishId()!=null){
            //菜品
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());

        }
        else {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        ShoppingCart one = service.getOne(queryWrapper);
        if(one.getNumber()==1){
            service.removeById(one);
            return R.success("直接移除商品");
        }

        one.setNumber(one.getNumber()-1);
        service.updateById(one);
        return R.success("修改成功");
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> delete(){
        Long userId=BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        service.remove(queryWrapper);
        return R.success("删除成功");
    }
}
