package com.du.aop;

import com.du.component.SysCacheComponent;
import com.du.constant.SysConstant;
import com.du.domain.mongodb.SysLog;
import com.du.domain.redis.LoginUser;
import com.du.pojo.ApiResult;
import com.du.task.AsyncTaskService;
import com.du.util.CheckUtil;
import com.du.util.JSONUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @Title  Aop 用户写入日志
 * @ClassName UserController
 * @Author duke
 * @Date 2018/9/13
 */
@Component
@Aspect
public class WebControllerAop {

    private static final Logger logger = LoggerFactory.getLogger(WebControllerAop.class);

    @Autowired(required = false)
    private HttpServletRequest request;

    @Autowired
    AsyncTaskService asyncTaskService;

    @Autowired
    SysCacheComponent cacheComponent;

    //匹配com..xchxin.simons.controller包及其子包下的所有类的所有方法
    @Pointcut("execution(* com.du.controller..*.*(..))")
    public void executeService() {

    }

    /**
     * 拦截器具体实现
     *
     * @param pjp
     * @return JsonResult（被拦截方法的执行结果，或需要登录的错误提示。）
     */
    @Around("executeService()") //指定拦截器规则；也可以直接把“execution(* com.............)”写进这里
    public Object Interceptor(ProceedingJoinPoint pjp) throws Throwable {
        long beginTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        //获取被拦截的方法
        Method method = signature.getMethod();
        //获取被拦截的方法名
        String methodName = method.getName();
        //Set<Object> allParams = new LinkedHashSet<>(); //保存所有请求参数，用于输出到日志中
        logger.info("请求开始，方法：{}", methodName);
        Object result = null;
        Object[] args = pjp.getArgs();
        StringBuffer params = new StringBuffer();
        if (!CheckUtil.objectIsNull(args)) {
            for (Object arg : args) {
                if (!CheckUtil.objectIsNull(arg)) {
                    params.append(JSONUtil.toJson(arg));
                }
            }
        }
        logger.info("当前请求的参数是{}", params);
        SysLog log = new SysLog();
        log.setParam(params.toString());
        log.setMethodName(methodName);
        log.setRequestMethod(request.getMethod());
        log.setTime(new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS").format(new Date()));
        String url = request.getRequestURI();
        LoginUser loginUser = cacheComponent.getUserCache();
        int port = 1;
        if(url.contains(SysConstant.APP)){
            port = 2;

        }else if(url.contains(SysConstant.WEB)){
            port = 1;
        }
        if(CheckUtil.objectIsNull(loginUser)){
            loginUser = new LoginUser();
            loginUser.setUid(0);
            loginUser.setUserName("未登录");
        }
        log.setUid(loginUser.getUid());
        log.setPath(request.getServletPath());
        if (result == null) {
            // 一切正常的情况下，继续执行被拦截的方法
            try {
                result = pjp.proceed();
                if(!url.contains(SysConstant.CALLBACK)){
                    //返回结果解析 日志记录
                    result = setSysLogAtt(result,log);
                }
            }catch (Exception ex){
                ex.printStackTrace();
                result =  ApiResult.returnError("服务器异常，请稍后重试");
                log.setType(2);
                log.setTypeName("异常");
                log.setErrorMsg(ex.getMessage());
                log.setStackTrace(this.getStackTrace(ex));
            }
        }
        log.setPort(port);
        long costMs = System.currentTimeMillis() - beginTime;
        logger.info("{}请求结束，耗时：{}ms", methodName, costMs);
        logger.info("请求结果：{}", JSONUtil.toJson(result));
        log.setCostMs(costMs);
        log.setReturnData(JSONUtil.toJson(result));
        asyncTaskService.executeAsyncLogTask(log);
        return result;
    }

    private ApiResult setSysLogAtt(Object result, SysLog log) {
        ApiResult apiResult = (ApiResult) result;
        if(apiResult.isSuccess()){
            log.setType(1);
            log.setTypeName("正常");
        }else {
            if(apiResult.getCode() == 500){
                //表示出错了
                if(!CheckUtil.objectIsNull(apiResult.getEx())){
                    log.setType(2);
                    log.setTypeName("异常");
                    log.setErrorMsg(apiResult.getEx().getMessage());
                    log.setStackTrace(this.getStackTrace(apiResult.getEx()));
                    apiResult.setEx(null);
                    apiResult.setMessage("服务器内部错误");
                }else{
                    log.setType(2);
                    log.setTypeName("异常");
                    log.setErrorMsg(apiResult.getMessage());
                }
            }else {
                log.setType(2);
                log.setTypeName("异常");
                log.setErrorMsg(apiResult.getMessage());
            }
        }
        return apiResult;
    }


    public  String getStackTrace(Throwable throwable)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try
        {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally
        {
            pw.close();
        }
    }
}
