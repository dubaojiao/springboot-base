package com.du.pojo;

/**
 * @Title 登陆实体
 * @ClassName LoginData
 * @Author duke
 * @Date 2018/9/13
 */
public class LoginData {
    /**手机号*/
    private String phone;
    /**验证码*/
    private String code;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
