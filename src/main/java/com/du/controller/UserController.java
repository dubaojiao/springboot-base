package com.du.controller;

import com.du.constant.SysConstant;
import com.du.pojo.ApiResult;
import com.du.pojo.LoginData;
import com.du.service.IUserService;
import com.du.util.CheckUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Title 用户
 * @ClassName UserController
 * @Author duke
 * @Date 2018/9/13
 */
@Api(value = "UserController",description = "用户控制器",basePath="/web/",tags={"UserController"})
@RestController
@RequestMapping(value = "/web/")
public class UserController {
    @Autowired
    IUserService userService;


    @ApiOperation(value = "登陆接口",notes = "用户登陆")
    @ApiImplicitParam(name = "obj", value = "{phone:\"15555555555\":code:\"123456\"}",required = true, dataType = "UserData")
    @PostMapping(value = "login")
    public ApiResult login(@RequestBody LoginData data){
        try {
            if(CheckUtil.objectIsNull(data)){
               return  ApiResult.returnError(SysConstant.ERROR_PARAM);
            }
            return userService.login(data);
        }catch (Exception ex){
            ex.printStackTrace();
          return  ApiResult.returnError(SysConstant.ERROR_500,ex);
        }
    }
    @ApiOperation(value = "获取用户信息列表",notes = "获取用户信息列表")
    @ApiImplicitParam(name = "index", value = "1",required = true)
    @GetMapping(value = "getUserList")
    public ApiResult getUserList(Integer index){
        try {
            if(CheckUtil.integerIsNull(index)){
                index = 1;
            }
            return userService.getUserList(index);
        }catch (Exception ex){
            ex.printStackTrace();
            return  ApiResult.returnError(SysConstant.ERROR_500,ex);
        }
    }

}
