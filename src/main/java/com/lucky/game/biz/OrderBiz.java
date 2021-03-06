package com.lucky.game.biz;

import com.lucky.game.entity.*;
import com.lucky.game.exception.BizException;
import com.lucky.game.vo.client.CreateOrderVo;
import com.lucky.game.constant.DictEnum;
import com.lucky.game.constant.ErrorEnum;
import com.lucky.game.dto.client.OrderDto;
import com.lucky.game.core.constant.ResponseData;
import com.lucky.game.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author conan
 *         2018/1/8 14:27
 **/
@Component
@Slf4j
public class OrderBiz {

    @Autowired
    private GameService gameService;

    @Autowired
    private GamePropertiesService gamePropertiesService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PaymentService paymentService;

    public ResponseData createOrder(String userId,OrderDto dto) {
        log.info("创建订单,userId={},dto={}",userId, dto);
        AccountEntity accountEntity = accountService.findByUserOid(userId);
        if (accountEntity.getBalance().compareTo(dto.getAmount()) < 0) {
            throw new BizException(ErrorEnum.ACCOUNT_BALANCE_TOO_LITTLE);
        }
        GameEntity gameEntity = gameService.findByOid(dto.getGameId());

        if (!DictEnum.GAME_STATUS_01.getCode().equals(gameEntity.getStatus())) {
            throw new BizException(ErrorEnum.GAME_START);
        }
        CreateOrderVo vo = doOrder(userId,dto, accountEntity, gameEntity);
        log.info("创建订单结束,vo={}", vo);
        return ResponseData.success(vo);
    }

    private CreateOrderVo doOrder(String userId,OrderDto dto, AccountEntity accountEntity, GameEntity gameEntity) {

        //创建订单
        OrderEntity orderEntity = orderService.initOrder(accountEntity.getOid(), userId, dto.getGameId(), dto.getAmount(), dto.getOperation());
        orderService.save(orderEntity);
        //增加资金池
        gameEntity.setFundingPool(gameEntity.getFundingPool().add(dto.getAmount()));
        gameService.save(gameEntity);
        //计算赔率
        CreateOrderVo createOrderVo = calcProbability(dto, gameEntity);
        //增加交易明细
        PaymentEntity paymentEntity = paymentService.initPayment(accountEntity.getOid(), userId, DictEnum.TRADE_TYPE_04.getCode(),
                orderEntity.getOrderNo(), DictEnum.PAY_STATUS_03.getCode(), DictEnum.PAY_DIRECTION_02.getCode(), dto.getAmount());
        paymentEntity.setGameId(gameEntity.getOid());
        paymentService.save(paymentEntity);
        //扣除余额
        accountEntity.setBalance(accountEntity.getBalance().subtract(dto.getAmount()));
        accountService.save(accountEntity);
        return createOrderVo;
    }

    /**
     * 计算概率
     */
    private CreateOrderVo calcProbability(OrderDto dto, GameEntity gameEntity) {

        CreateOrderVo createOrderVo = new CreateOrderVo();
        List<GamePropertiesEntity> gamePropertiesList = gamePropertiesService.findByGameId(gameEntity.getOid());
        for (GamePropertiesEntity gameProperties : gamePropertiesList) {
            if (gameProperties.getProKey().equals(dto.getOperation())) {
                gameProperties.setSupportCount(gameProperties.getSupportCount() + 1);
                gameProperties.setAmount(gameProperties.getAmount().add(dto.getAmount()));
            }
            gameProperties.setProValue(gameProperties.getAmount().divide(gameEntity.getFundingPool(), 2, BigDecimal.ROUND_HALF_DOWN));
            gamePropertiesService.save(gameProperties);
        }
        createOrderVo.setGamePropertiesList(gamePropertiesList);
        createOrderVo.setGameId(dto.getGameId());

        return createOrderVo;
    }
}
