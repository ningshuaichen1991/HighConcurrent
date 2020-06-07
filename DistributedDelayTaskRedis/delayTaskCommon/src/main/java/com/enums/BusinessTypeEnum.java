package com.enums;
import	java.util.stream.Collectors;

import java.util.Arrays;
import java.util.List;

/**
 * 业务类型
 */
public enum BusinessTypeEnum {

    /**
     * 开户
     */
    opendAccount("openAccount");


    private String businessValue;

    private BusinessTypeEnum(String businessValue) {
        this.businessValue = businessValue;
    }

    public String getBusinessValue() {
        return businessValue;
    }

    /**
     * 获取所有的枚举值
     * @return
     */
    public static List<String> getAllBusinessTypeStringList(){
        return Arrays.stream(BusinessTypeEnum.values()).
                map(x -> x.getBusinessValue()).collect(Collectors.toList());
    }

    /**
     * 根据value获取枚举对象
     * @param value
     * @return
     */
    public static BusinessTypeEnum getByValue(String value){
        return Arrays.stream(BusinessTypeEnum.values()).filter(v->v.getBusinessValue().equals(value)).findFirst().get();
    }

    /**
     * 获取所有的枚举对象
     * @return
     */
    public static List<BusinessTypeEnum> getAllBusinessTypeList(){
        return Arrays.stream(BusinessTypeEnum.values()).collect(Collectors.toList());
    }
}
