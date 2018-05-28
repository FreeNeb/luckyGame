package com.lucky.game.dao;

import com.lucky.game.entity.SysUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author conan
 *         2017/10/26 13:41
 **/
public interface SysUserDao extends JpaRepository<SysUserEntity, String>, JpaSpecificationExecutor<SysUserEntity> {


}


