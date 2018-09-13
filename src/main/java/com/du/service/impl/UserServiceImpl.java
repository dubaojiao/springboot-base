package com.du.service.impl;

import com.du.component.SysCacheComponent;
import com.du.constant.SysConstant;
import com.du.dao.UserInfoDao;
import com.du.domain.mysql.UserInfo;
import com.du.domain.redis.LoginUser;
import com.du.pojo.ApiResult;
import com.du.pojo.LoginData;
import com.du.pojo.UserInfoEntry;
import com.du.service.IUserService;
import com.du.util.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Title 用户
 * @ClassName UserServiceImpl
 * @Author duke
 * @Date 2018/9/13
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserInfoDao userInfoDao;
    @Autowired
    SysCacheComponent sysCacheComponent;
    @Value("${sms.code}")
    String smsCode;
    @Override
    public ApiResult login(LoginData data) {
        if(!smsCode.equals(data.getCode())){
            return ApiResult.returnError("验证码错误");
        }
        UserInfo userInfo = userInfoDao.findUserInfoByPhone(data.getPhone());
        if(CheckUtil.objectIsNull(userInfoDao)){
            return ApiResult.returnError("用户不存在");
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setUid(userInfo.getId());
        loginUser.setUserName(userInfo.getName());
        loginUser.setLoginTime(new Date());
        loginUser.setUserPhone(userInfo.getPhone());
        if(sysCacheComponent.setUserCache(loginUser)){
            return ApiResult.returnSuccess(SysConstant.SUCCESS,loginUser);
        }
        return ApiResult.returnError("登录异常,请重试");
    }

    @Override
    public ApiResult getUserList(Integer index) {
        Map map = new HashMap(2);
        Long total = userInfoDao.countAll();
        List<UserInfoEntry> list;
        if(total > 0){
            list = userInfoDao.findAllBySize(index);
        }else {
            list = new ArrayList<>();
        }
        map.put("total",total);
        map.put("rows",list);
        return ApiResult.returnSuccess(SysConstant.SUCCESS,map);
    }
}
