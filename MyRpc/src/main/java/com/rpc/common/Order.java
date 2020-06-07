package com.rpc.common;
import lombok.Data;

import	java.math.BigDecimal;

import java.io.Serializable;

/**
 * 订单
 */
@Data
public class Order implements Serializable {

    /**
     * 订单号
     */
    private Integer orderId;
    /**
     * 金额
     */
    private BigDecimal amount;



    public Order(Integer orderId,BigDecimal amount){
        this.orderId = orderId;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", amount=" + amount +
                '}';
    }
}