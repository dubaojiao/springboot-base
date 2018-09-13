package com.du.constant;
/**
 * @Title redis 常量
 * @ClassName RedisConstant
 * @Author duke
 * @Date 2018/9/13
 */
public class RedisConstant {

    private final static  String BASE = "base";
    private final static  String WEB= "web";
    private final static  String APP= "app";

    public static  String getWebKey(String token){
        return new StringBuilder(BASE).append(":").append(WEB).append(":").append(token).toString();
    }


    public static  String getAppKey(String token){
        return new StringBuilder(BASE).append(":").append(APP).append(":").append(token).toString();
    }
}
