package com.lucky.game.service;

import com.lucky.game.dto.client.RegisterDto;
import com.lucky.game.entity.UserEntity;

/**
 * @author conan
 *         2018/1/5 9:57
 **/
public interface UserService {

    /**
     * 创建用户账号
     */
    UserEntity addUser(RegisterDto dto);

    /**
     * 根据账号或者手机号查询
     */
    UserEntity findByUserAccountOrPhone(String userAccount,String phone);

    UserEntity findByOid(String oid);
}
