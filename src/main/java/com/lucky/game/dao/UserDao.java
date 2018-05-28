package com.lucky.game.dao;

import com.lucky.game.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author conan
 *         2017/10/26 13:41
 **/
public interface UserDao extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {

    UserEntity findByUserAccountOrPhone(String userAccount,String phone);

}


