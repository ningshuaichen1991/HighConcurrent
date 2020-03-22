package com.batchInsert.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 费用信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cost {

    private int id;
    /**
     * 类型
     */
    private String type;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 金额
     */
    private BigDecimal money;
}