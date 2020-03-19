package com.batchInsert.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cost {

    private int id;
    private String type;//类型
    private Date createDate;
    private BigDecimal money;
}