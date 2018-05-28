package com.lucky.game.service.impl;

import com.lucky.game.constant.DictEnum;
import com.lucky.game.constant.ErrorEnum;
import com.lucky.game.dao.UserDao;
import com.lucky.game.entity.UserEntity;
import com.lucky.game.exception.BizException;
import com.lucky.game.service.UserService;
import com.lucky.game.dto.client.RegisterDto;
import com.lucky.game.core.util.Digests;
import com.lucky.game.core.util.PwdUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author conan
 *         2018/1/5 9:57
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public UserEntity addUser(RegisterDto dto) {
        UserEntity entity = new UserEntity();
        entity.setUserAccount(dto.getUserAccount());
        if (!StringUtils.isEmpty(dto.getUserPwd())) {
            entity.setSalt(Digests.genSalt());
            entity.setUserPwd(PwdUtil.encryptPassword(dto.getUserPwd(), entity.getSalt()));
        }
        entity.setPhone(dto.getPhone());
        entity.setUserType(DictEnum.USER_TYPE_01.getCode());
        entity.setStatus(DictEnum.USER_STATUS_NORMAL.getCode());
        userDao.save(entity);
        return entity;
    }

    @Override
    public UserEntity findByUserAccountOrPhone(String userAccount, String phone) {
        UserEntity userEntity = userDao.findByUserAccountOrPhone(userAccount, phone);
        if (userEntity == null) {
            throw new BizException(ErrorEnum.USER_NOT_FOUND);
        }
        return userEntity;
    }

    @Override
    public UserEntity findByOid(String oid) {
        UserEntity userEntity = userDao.findOne(oid);
        if (userEntity == null) {
            throw new BizException(ErrorEnum.USER_NOT_FOUND);
        }
        if(DictEnum.USER_STATUS_FORBIDDER.getCode()
        .equals(userEntity.getStatus())){
            throw new BizException(ErrorEnum.USER_NOT_FORBIDDEN);
        }
        return userEntity;
    }
}
