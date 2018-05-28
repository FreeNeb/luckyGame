package com.lucky.game.service;

import com.lucky.game.entity.GamePropertiesEntity;

import java.util.List;

/**
 * @author conan
 *         2018/1/5 16:03
 **/
public interface GamePropertiesService {

    void saveDigitalCashGamePropeties(String gameId);

    List<GamePropertiesEntity> findByGameId(String gameId);

    GamePropertiesEntity save(GamePropertiesEntity entity);

    GamePropertiesEntity findByGameIdAndProKey(String gameId, String key);
}
