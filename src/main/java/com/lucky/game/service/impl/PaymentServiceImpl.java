package com.lucky.game.service.impl;

import com.lucky.game.constant.DictEnum;
import com.lucky.game.constant.ErrorEnum;
import com.lucky.game.dao.PaymentDao;
import com.lucky.game.dto.manager.PaymentQueryDto;
import com.lucky.game.entity.PaymentEntity;
import com.lucky.game.exception.BizException;
import com.lucky.game.service.PaymentService;
import com.lucky.game.core.common.SeqGenerator;
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
 *         2018/1/5 11:25
 **/
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private SeqGenerator seqGenerator;

    @Autowired
    private PaymentDao paymentDao;

    @Override
    public PaymentEntity initPayment(String accountOid, String userOid, String tradeType, String orderNo, String status, String direction, BigDecimal amount) {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setAccountOid(accountOid);
        paymentEntity.setUserOid(userOid);
        paymentEntity.setTradeType(tradeType);
        String payNo = this.seqGenerator.next(DictEnum.PAY_NO_PREFIX.getCode());
        paymentEntity.setPayNo(payNo);
        if (StringUtils.isNotEmpty(orderNo)) {
            paymentEntity.setOrderNo(orderNo);
        }
        paymentEntity.setStatus(status);
        paymentEntity.setDirection(direction);
        paymentEntity.setAmount(amount);
        return paymentEntity;
    }

    @Override
    public PaymentEntity save(PaymentEntity paymentEntity) {
        return paymentDao.save(paymentEntity);
    }

    @Override
    public PaymentEntity findByPayNo(String payNo) {
        PaymentEntity paymentEntity = paymentDao.findByPayNo(payNo);
        if (paymentEntity == null) {
            throw new BizException(ErrorEnum.RECOLD_NOT_FOUND);
        }
        return paymentEntity;
    }

    @Override
    public Page<PaymentEntity> findAll(PaymentQueryDto dto) {

        Pageable pageable = new PageRequest(dto.getCurrentPage() - 1, dto.getPageSize(), new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
        return paymentDao.findAll(buildSpecification(dto), pageable);
    }

    private Specification<PaymentEntity> buildSpecification(final PaymentQueryDto dto) {
        Specification<PaymentEntity> spec = (root, query, cb) -> {
            List<Predicate> bigList = new ArrayList<Predicate>();
            if (StringUtils.isNotEmpty(dto.getUserOid())) {
                bigList.add(cb.equal(root.get("userOid").as(String.class), dto.getUserOid()));
            }
            if (StringUtils.isNotEmpty(dto.getPayNo())) {
                bigList.add(cb.equal(root.get("payNo").as(String.class), dto.getPayNo()));
            }
            if (StringUtils.isNotEmpty(dto.getTradeType())) {
                bigList.add(cb.equal(root.get("tradeType").as(String.class), dto.getTradeType()));
            }
            query.where(cb.and(bigList.toArray(new Predicate[bigList.size()])));
            query.orderBy(cb.desc(root.get("createTime")));
            // 条件查询
            return query.getRestriction();
        };
        return spec;
    }
}
