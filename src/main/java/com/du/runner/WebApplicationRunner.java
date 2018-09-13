package com.du.runner;

import com.du.dao.UserInfoDao;
import com.du.domain.mysql.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * @Title 项目启动-最后执行的类
 * @ClassName WebApplicationRunner
 * @Author duke
 * @Date 2018/9/13
 */

@Component
@Order(value = 999)
public class WebApplicationRunner implements ApplicationRunner {

    Logger logger = LoggerFactory.getLogger(WebApplicationRunner.class);

    @Autowired
    UserInfoDao userInfoDao;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(userInfoDao.countAll() == 0){
            //系统启动添加默认用户信息
            UserInfo userInfo = new UserInfo();
            userInfo.setId(1);
            userInfo.setName("duke");
            userInfo.setPhone("15555555555");
            userInfoDao.save(userInfo);
            logger.info(userInfo.toString());
        }
        logger.info("项目启动完成");
    }

}
