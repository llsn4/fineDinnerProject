package cn.dinner.order.interceptor;

import cn.dinner.order.common.BaseContext;
import cn.dinner.order.common.R;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 86139
 */
@Slf4j
public class MyInterceptor implements HandlerInterceptor {
    /**
     *     路径匹配器,支持通配符
     */
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截到请求,{}",request.getRequestURI());
        //获取请求的uri
        if( request.getSession().getAttribute("employee")!=null){
          long id=  (long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(id);
            log.info("用户已登录");
            return true;

        }
        else if( request.getSession().getAttribute("user")!=null){
            long id=  (long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(id);
            log.info("用户已登录");
            return true;

        }
        else {
            log.info("用户未登录");
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            return false;
        }

        //定义不需要处理的请求路径
//        String  []urls={
//                "/employee/login",
//                "/employee/logout",
//                "/backend/**",
//                "/front/**"
//
//        };

    }

    /**
     * 检测本次请求是否需要放行
     * @param requestURI
     * @return
     */
//    public boolean check(String requestURI){
//
//    }
}
