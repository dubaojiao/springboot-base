package com.du.component;

import com.du.common.RedisClient;
import com.du.constant.RedisConstant;
import com.du.constant.SysConstant;
import com.du.domain.redis.LoginUser;
import com.du.util.CheckUtil;
import com.du.util.CookieUtil;
import com.du.util.JSONUtil;
import com.du.util.RandomCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
/**
 * @Title 系统缓存 组件
 * @ClassName SysCacheComponent
 * @Author duke
 * @Date 2018/9/13
 */
@Component
public class SysCacheComponent {
    //存储用户信息的key
    private static final String USER_INFO_PRO = "USERINFO%";
    //存储用户token的key
    private static final String USER_TOKEN_PRO = "USERTOKEN%";
    //7天
    private static final   Integer expire7 = 60*60*24*30;
    //1天
    private static final   Integer expire1 = 60*60*24;


    @Autowired(required = false)
    HttpServletResponse response;

    @Autowired(required = false)
    HttpServletRequest request;

    @Autowired
    RedisClient redisClient;

    /**
     * 设置用户缓冲信息
     * @param user
     *
     * @return
     */
    public  boolean setUserCache(LoginUser user){
        /*
         * 1.生成一个32位的token
         * 2.将uid进行特殊处理 作为key去获取信息看是否有token 如果有清空token及token对于的信息
         * 3.作为key token作为value 存入redis
         * 4.token作为key 用户信息作为value 存入redis
         * 5.将token设置到Cookie中
         */

        /*switch (type){
            case 1: return webUserCache(user);
            case 2:return appUserCache(user);
            default:return false;
        }*/
        //1 WEB  2  APP 3 商户
        String url = request.getRequestURI();
        if(url.contains(SysConstant.APP)){
            //清空之前的登陆信息
            emptyUserCache(user.getUid(),2);
            return this.appUserCache(user);
        }else if(url.contains(SysConstant.WEB)){
            //清空之前的登陆信息
            emptyUserCache(user.getUid(),1);
            return this.webUserCache(user);
        }else {
            return false;
        }
    }


    private boolean webUserCache(LoginUser user){
        try {
            String token = RandomCodeUtil.getUUID();
            String userInfoKey = USER_INFO_PRO.replace("%",user.getUid().toString());
            redisClient.set(RedisConstant.getWebKey(token),userInfoKey);
            String userTokenKey = USER_TOKEN_PRO.replace("%",user.getUid().toString());
            redisClient.set(RedisConstant.getWebKey(userTokenKey),token);
            redisClient.set(RedisConstant.getWebKey(userInfoKey), JSONUtil.toJson(user));
            CookieUtil.addCookie(response,token,expire1);
            redisClient.expire(RedisConstant.getWebKey(token),expire1);
            redisClient.expire(RedisConstant.getWebKey(userTokenKey),expire1);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    private boolean appUserCache(LoginUser user){
        try {
            String token = RandomCodeUtil.getUUID();
            String userInfoKey = USER_INFO_PRO.replace("%",user.getUid().toString());
            redisClient.set(RedisConstant.getAppKey(token),userInfoKey);
            String userTokenKey = USER_TOKEN_PRO.replace("%",user.getUid().toString());
            redisClient.set(RedisConstant.getAppKey(userTokenKey),token);
            redisClient.set(RedisConstant.getAppKey(userInfoKey), JSONUtil.toJson(user));
            CookieUtil.addCookie(response,token,0);
            redisClient.expire(RedisConstant.getAppKey(token),expire7);
            redisClient.expire(RedisConstant.getAppKey(userTokenKey),expire7);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }


    /**
     * 通过用户ID 获取用户信息
     * @param uid
     * @return
     */
    private LoginUser getWebUserCache(Integer uid){
        try {
            String object = redisClient.get(RedisConstant.getWebKey(USER_INFO_PRO.replace("%",uid.toString())));
            if(CheckUtil.stringIsNull(object)){
                return null;
            }
            return  JSONUtil.toBean(object,LoginUser.class);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 通过用户ID 获取用户信息
     * @param uid
     * @return
     */
    private LoginUser getAppUserCache(Integer uid){
        try {
            String object = redisClient.get(RedisConstant.getAppKey(USER_INFO_PRO.replace("%",uid.toString())));
            if(CheckUtil.stringIsNull(object)){
                return null;
            }
            return  JSONUtil.toBean(object,LoginUser.class);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }



    /**
     * 获取当前登录的用户数据
     * @return
     */
    public LoginUser getUserCache(){
        LoginUser loginUser;
        String url = request.getRequestURI();
        if(url.contains(SysConstant.APP)){
            loginUser =  this.getAppUserCache();
        }else if(url.contains(SysConstant.WEB)){
            loginUser =  this.getWebUserCache();
        }else {
            loginUser =  null;
        }
        return loginUser;
    }

    /**
     * 通过http请求获取登陆用户
     * @return
     */
    private LoginUser getWebUserCache(){
        try {
            String key = CookieUtil.getCookie(request);
            if(CheckUtil.stringIsNull(key)){
                return null;
            }
            String value = redisClient.get(RedisConstant.getWebKey(key));
            if(CheckUtil.stringIsNull(value)){
                return null;
            }
            String object = redisClient.get(RedisConstant.getWebKey(value));
            if(CheckUtil.stringIsNull(object)){
                return null;
            }
            //刷新缓存时间
            redisClient.expire(RedisConstant.getWebKey(key),expire1);
            redisClient.expire(RedisConstant.getWebKey(value),expire1);
            return  JSONUtil.toBean(object,LoginUser.class);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 通过http请求获取登陆用户
     * @return
     */
    private LoginUser getAppUserCache(){
        try {
            String key = CookieUtil.getCookie(request);
            if(CheckUtil.stringIsNull(key)){
                return null;
            }
            String value = redisClient.get(RedisConstant.getAppKey(key));
            if(CheckUtil.stringIsNull(value)){
                return null;
            }
            String object = redisClient.get(RedisConstant.getAppKey(value));
            if(CheckUtil.stringIsNull(object)){
                return null;
            }
            //刷新缓存时间
            redisClient.expire(RedisConstant.getAppKey(key),expire7);
            redisClient.expire(RedisConstant.getAppKey(value),expire7);
            return  JSONUtil.toBean(object,LoginUser.class);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * 修改
     * @param user
     * @return
     */
    public  boolean updateUserCache(LoginUser user,Integer type){
        try {
            if(type == 1){
                String userKey = RedisConstant.getWebKey(USER_INFO_PRO.replace("%",user.getUid().toString()));
                return redisClient.set(userKey, JSONUtil.toJson(user));
            }else if(type == 2){
                String userKey = RedisConstant.getAppKey(USER_INFO_PRO.replace("%",user.getUid().toString()));
                return redisClient.set(userKey, JSONUtil.toJson(user));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            return false;
        }
    }
    /**
     * 清空用户缓存信息
     * @param uid
     * @return
     */
    public  boolean emptyUserCache(Integer uid,Integer type){
        if(type == 1){
            try {
                String userInfoKey = RedisConstant.getWebKey(USER_INFO_PRO.replace("%",uid.toString()));
                String userTokenKey = RedisConstant.getWebKey(USER_TOKEN_PRO.replace("%",uid.toString()));
                String token = RedisConstant.getWebKey(redisClient.get(userTokenKey));
                redisClient.del(token);
                redisClient.del(userInfoKey);
                redisClient.del(userTokenKey);
                return true;
            }catch (Exception ex){
                ex.printStackTrace();
                return false;
            }
        }else if(type == 2){
            try {
                String userInfoKey = RedisConstant.getAppKey(USER_INFO_PRO.replace("%",uid.toString()));
                String userTokenKey = RedisConstant.getAppKey(USER_TOKEN_PRO.replace("%",uid.toString()));
                String token = RedisConstant.getAppKey(redisClient.get(userTokenKey));
                redisClient.del(token);
                redisClient.del(userInfoKey);
                redisClient.del(userTokenKey);
                return true;
            }catch (Exception ex){
                ex.printStackTrace();
                return false;
            }
        }
        return false;
    }

}
