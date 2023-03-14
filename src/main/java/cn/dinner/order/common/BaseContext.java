package cn.dinner.order.common;

/**
 * 基于ThreadLocal封装的工具类
 * @author 86139
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);

    }
    public static Long getCurrentId(){
      return   threadLocal.get();

    }


}
