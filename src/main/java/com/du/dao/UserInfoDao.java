package com.du.dao;

import com.du.common.CommonDao;
import com.du.domain.mysql.UserInfo;
import com.du.pojo.UserInfoEntry;
import com.du.repos.UserInfoRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title  用户信息 数据 操作
 * @ClassName UserInfoDao
 * @Author duke
 * @Date 2018/9/13
 */
@Service
public class UserInfoDao {
    @Autowired
    UserInfoRepos userInfoRepos;
    @Autowired
    CommonDao commonDao;
    @Value("${spring.page.size.web}")
    Integer size;
    public UserInfo save(UserInfo userInfo){
        return userInfoRepos.save(userInfo);
    }

    public long countAll(){
        return userInfoRepos.count();
    }

    public UserInfo findUserInfoByPhone(String phone){
        return userInfoRepos.findFirstByPhone(phone);
    }

    public List<UserInfoEntry>  findAllBySize(Integer index){
        int a = (index-1)*size;
        int b = size;
        String sql = "SELECT * FROM user_info ORDER BY id LIMIT :a,:b";
        Map map  = new HashMap(2);
        map.put("a",a);
        map.put("b",b);
        return commonDao.queryListEntity(sql,map,UserInfoEntry.class);

    }
}
