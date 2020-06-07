package com.rpc.v1;

import com.rpc.common.IOrderService;
import com.rpc.common.Order;

import java.math.BigDecimal;

/**
 * 订单操作具体实现
 */
public class OrderServiceImpl implements IOrderService {

    @Override
    public Order findOrderById(Integer id) {
        return new Order(id, BigDecimal.valueOf(100));
    }
}