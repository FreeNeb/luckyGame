package com.lucky.game.dao;

import com.lucky.game.entity.SmsSendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author conan
 *         2018/1/12 11:03
 **/
public interface SmsSendDao extends JpaRepository<SmsSendEntity, String>, JpaSpecificationExecutor<SmsSendEntity> {
}
