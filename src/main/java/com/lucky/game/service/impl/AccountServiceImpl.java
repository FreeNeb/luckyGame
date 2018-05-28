package com.lucky.game.service.impl;

import com.lucky.game.constant.DictEnum;
import com.lucky.game.constant.ErrorEnum;
import com.lucky.game.dao.AccountDao;
import com.lucky.game.entity.AccountEntity;
import com.lucky.game.exception.BizException;
import com.lucky.game.service.AccountService;
import com.lucky.game.core.common.SeqGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author conan
 *         2018/1/5 11:24
 **/
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private SeqGenerator seqGenerator;

    @Override
    public AccountEntity initAccount(String userOid, String userType){
        AccountEntity accountEntity = new AccountEntity();
        String accountNo = this.seqGenerator.next(DictEnum.ACCOUNT_NO_PREFIX.getCode());
        accountEntity.setAccountNo(accountNo);
        accountEntity.setUserOid(userOid);
        accountEntity.setAccountType(userType);
        accountEntity.setStatus(DictEnum.USER_STATUS_NORMAL.getCode());
        return accountEntity;
    }

    @Override
    public AccountEntity save(AccountEntity accountEntity){
        return accountDao.save(accountEntity);
    }

    @Override
    public AccountEntity findByUserOid(String userOid) {
        AccountEntity accountEntity = accountDao.findByUserOid(userOid);
        if (accountEntity == null) {
            throw new BizException(ErrorEnum.USER_NOT_FOUND);
        }
        if(DictEnum.USER_STATUS_FORBIDDER.getCode()
                .equals(accountEntity.getStatus())){
            throw new BizException(ErrorEnum.USER_NOT_FORBIDDEN);
        }
        return accountEntity;
    }

    @Override
    public AccountEntity findById(String oid){
        return accountDao.findOne(oid);
    }

    @Override
    public AccountEntity findByAccountType(String accountType) {
        return accountDao.findByAccountType(accountType);
    }
}
