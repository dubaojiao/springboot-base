package com.du.domain.redis;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title 登陆用户信息
 * @ClassName LoginUser
 * @Author duke
 * @Date 2018/9/13
 */
public class LoginUser implements Serializable {
    private Integer uid;//用户Id
    private String userName;//用户名
    private String userPhone;//用户手机---登陆手机号
    private Date loginTime;//最后登陆时间
    private Integer userType;//用户类型
    private String appType;//手机类型 （ios/android/web/wx）

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    @Override
    public String toString() {
        return "LoginUser{" +
                "uid=" + uid +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", loginTime=" + loginTime +
                ", userType=" + userType +
                ", appType='" + appType + '\'' +
                '}';
    }
}
