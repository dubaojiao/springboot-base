package com.du.service;
import com.du.pojo.ApiResult;
import com.du.pojo.LoginData; /**
 * @Title 用户
 * @ClassName IUserService
 * @Author duke
 * @Date 2018/9/13
 */
public interface IUserService {
    ApiResult login(LoginData data);

    ApiResult getUserList(Integer index);
}
