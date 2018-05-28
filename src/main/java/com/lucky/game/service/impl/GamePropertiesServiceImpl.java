package com.lucky.game.service.impl;

import com.lucky.game.constant.DictEnum;
import com.lucky.game.dao.GamePropertiesDao;
import com.lucky.game.entity.GamePropertiesEntity;
import com.lucky.game.service.GamePropertiesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author conan
 *         2018/1/5 16:03
 **/
@Slf4j
@Service
public class GamePropertiesServiceImpl implements GamePropertiesService {

    @Autowired
    private GamePropertiesDao gamePropertiesDao;

    @Override
    public void saveDigitalCashGamePropeties(String gameId) {
        initProperties(gameId, DictEnum.GAME_PROPERTIES_WIN.getCode());
        initProperties(gameId, DictEnum.GAME_PROPERTIES_LOSE.getCode());
        initProperties(gameId, DictEnum.GAME_PROPERTIES_DRAW.getCode());
    }

    @Override
    public List<GamePropertiesEntity> findByGameId(String gameId) {
        return gamePropertiesDao.findByGameId(gameId);
    }

    @Override
    public GamePropertiesEntity save(GamePropertiesEntity entity) {
        return gamePropertiesDao.save(entity);
    }

    private void initProperties(String gameId, String key) {
        GamePropertiesEntity entity = new GamePropertiesEntity();
        entity.setGameId(gameId);
        entity.setProKey(key);
        gamePropertiesDao.save(entity);
    }

    @Override
    public GamePropertiesEntity findByGameIdAndProKey(String gameId, String key) {
        return gamePropertiesDao.findByGameIdAndProKey(gameId, key);
    }

}
