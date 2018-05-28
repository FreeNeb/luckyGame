package com.lucky.game.service.impl;

import com.lucky.game.dao.SmsSendDao;
import com.lucky.game.entity.SmsSendEntity;
import com.lucky.game.service.SmsSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author conan
 *         2018/1/12 11:05
 **/
@Service
public class SmsSendServiceImpl implements SmsSendService {

    @Autowired
    private SmsSendDao smsSendDao;

    public SmsSendEntity initSmsSendService(String type, String content, String errCode, String errMsg, String status) {
        SmsSendEntity smsSendEntity = new SmsSendEntity();
        smsSendEntity.setSmsSendTypes(type);
        smsSendEntity.setNotifyContent(content);
        smsSendEntity.setErrorCode(errCode);
        smsSendEntity.setErrorMessage(errMsg);
        smsSendEntity.setNotifyStatus(status);
        return smsSendEntity;
    }

    public void save(SmsSendEntity smsSendEntity) {
        smsSendDao.save(smsSendEntity);
    }
}
