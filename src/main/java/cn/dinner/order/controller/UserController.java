package cn.dinner.order.controller;

import cn.dinner.order.common.R;
import cn.dinner.order.entity.User;
import cn.dinner.order.service.UserService;
import cn.dinner.order.utils.SMSUtils;
import cn.dinner.order.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 发送手机验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone=user.getPhone();

        if(StringUtils.isNotEmpty(phone)){
            //生成随机验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);
            //调用短信api发送短信
           // SMSUtils.sendMessage();
            //将生成的验证码保存到session中
            session.setAttribute(phone,code);
            return R.success("手机验证码发送成功");
        }
      return R.error("短信发送失败");


    }
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        //获取手机号
       String phone= (String) map.get("phone");
       String code=(String) map.get("code");
       String codeInSession= (String) session.getAttribute(phone);
      if(codeInSession!=null&&codeInSession.equals(code)){
          //如果是新用户，自动注册
          LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper();
          queryWrapper.eq(User::getPhone,phone);
          User user1=userService.getOne(queryWrapper);
         if(user1==null) {
             User user=new User();
             user.setPhone(phone);
             userService.save(user);
             session.setAttribute("user",user.getId());
             return R.success(user);
          }
         else {
             session.setAttribute("user",user1.getId());
         }

          return R.success(user1);
      }
      return R.error("登录失败");

    }
}
