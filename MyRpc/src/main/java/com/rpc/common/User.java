package com.rpc.common;
import	java.io.Serializable;

import lombok.Data;

/**
 * 用户
 */
@Data
public class User implements Serializable{
    /**
     * id
     */
    private Integer id;
    /**
     * 用户名称
     */
    private String userName;

}