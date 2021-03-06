package com.lucky.game;

import com.lucky.game.entity.GameEntity;
import com.lucky.game.biz.GameBiz;
import com.lucky.game.biz.SmartContractBiz;
import com.lucky.game.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author conan
 *         2018/3/14 14:52
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class GameBizTest {

    @Autowired
    private SmartContractBiz smartContractBiz;

    @Autowired
    private GameBiz gameBiz;

    @Autowired
    private GameService gameService;

    @Test
    public void createGameTest() {
        GameEntity gameEntity = gameService.findByOid("2c9852886361d6e9016361d8223a047c");
        smartContractBiz.createGame(gameEntity);
    }

    @Test
    public void createSmartGameTest() {
        gameBiz.createSmartGame();
    }


}
