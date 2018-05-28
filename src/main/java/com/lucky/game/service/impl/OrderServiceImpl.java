package com.lucky.game.service.impl;

import com.lucky.game.constant.DictEnum;
import com.lucky.game.dao.OrderDao;
import com.lucky.game.dto.client.QueryOrderDto;
import com.lucky.game.entity.OrderEntity;
import com.lucky.game.service.OrderService;
import com.lucky.game.core.common.SeqGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author conan
 *         2018/1/8 14:03
 **/
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private SeqGenerator seqGenerator;

    @Autowired
    private OrderDao orderDao;

    @Override
    public OrderEntity initOrder(String accountOid, String userOid, String gameId, BigDecimal amount, String key) {
        OrderEntity entity = new OrderEntity();
        entity.setGameId(gameId);
        entity.setProKey(key);
        entity.setAccountOid(accountOid);
        entity.setUserOid(userOid);
        entity.setAmount(amount);
        entity.setStatus(DictEnum.ORDER_STATUS_01.getCode());
        String orderNo = this.seqGenerator.next(DictEnum.ORDER_NO_PREFIX.getCode());
        entity.setOrderNo(orderNo);
        return entity;

    }

    @Override
    public OrderEntity save(OrderEntity orderEntity) {
        return orderDao.save(orderEntity);
    }

    public Page<OrderEntity> findByGameIdAndProKey(QueryOrderDto dto) {

        Pageable pageable = new PageRequest(dto.getCurrentPage() - 1, dto.getPageSize(), new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
        return orderDao.findAll(buildSpecification(dto), pageable);
    }

    @Override
    public Integer updateStatusByGameId(String status,String gameId) {
        return orderDao.updateStatusByGameId(status,gameId);
    }


    private Specification<OrderEntity> buildSpecification(final QueryOrderDto dto) {
        Specification<OrderEntity> spec = (root, query, cb) -> {
            List<Predicate> bigList = new ArrayList<Predicate>();
            if (StringUtils.isNotEmpty(dto.getGameId())) {
                bigList.add(cb.equal(root.get("gameId").as(String.class), dto.getGameId()));
            }
            if (StringUtils.isNotEmpty(dto.getKey())) {
                bigList.add(cb.equal(root.get("proKey").as(String.class), dto.getKey()));
            }

            query.where(cb.and(bigList.toArray(new Predicate[bigList.size()])));

            query.orderBy(cb.desc(root.get("createTime")));

            // 条件查询
            return query.getRestriction();
        };
        return spec;
    }
}
