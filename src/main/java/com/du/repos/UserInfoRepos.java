package com.du.repos;

import com.du.domain.mysql.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Title  用户信息 jpa 仓储
 * @ClassName UserInfoRepos
 * @Author duke
 * @Date 2018/9/13
 */
@Repository
public interface UserInfoRepos extends JpaRepository<UserInfo,Integer> {
    UserInfo findFirstByPhone(String phone);
}
