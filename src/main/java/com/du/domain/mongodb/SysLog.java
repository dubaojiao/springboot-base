package com.du.domain.mongodb;



import com.du.util.TimeUtils;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
/**
 * @Title 系统日志类
 * @ClassName SysLog
 * @Author duke
 * @Date 2018/9/13
 */
public class SysLog implements Serializable {

    /**
     * 主键.
     */
    @Id
    @GeneratedValue
    @Column(columnDefinition = "int(10) COMMENT 'ID'", nullable = false)
    private String logId;

    @Column(columnDefinition = "int(10) COMMENT '类型1正常2异常3错误'", nullable = false)
    private Integer type;

    @Column(columnDefinition = "varchar(64) COMMENT '类型名称 正常/异常/错误'", nullable = false)
    private String typeName;

    /**
     * 创建时间.
     */
    private String time;

    @Column(columnDefinition = "varchar(258) COMMENT '请求参数'", nullable = false)
    private String param;

    @Column(columnDefinition = "varchar(5000) COMMENT '请求参数'", nullable = false)
    private String returnData;


    @Column(columnDefinition = "int(10) COMMENT '当前登录Id'", nullable = false)
    private Integer uid;

    @Column(columnDefinition = "int(10) COMMENT '请求耗时'", nullable = false)
    private Long costMs;

    @Column(columnDefinition = "varchar(128) COMMENT '请求路径'", nullable = false)
    private String path;

    @Column(columnDefinition = "varchar(128) COMMENT '请求方法名称'", nullable = false)
    private String methodName;

    @Column(columnDefinition = "varchar(128) COMMENT '请求方式'", nullable = false)
    private String requestMethod;


    @Column(columnDefinition = "varchar(500) COMMENT '错误消息'")
    private String errorMsg;

    @Column(columnDefinition = "varchar(500) COMMENT '错误堆栈信息'")
    private String stackTrace;
    //端口
    @Column(columnDefinition = "int(2) COMMENT '端口 1 web  2 app'", nullable = false)
    private Integer port;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getCostMs() {
        return costMs;
    }

    public void setCostMs(Long costMs) {
        this.costMs = costMs;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time == null || "".equals(time)? TimeUtils.getCompleteDate():time;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getReturnData() {
        return returnData;
    }

    public void setReturnData(String returnData) {
        this.returnData = returnData;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }
}
