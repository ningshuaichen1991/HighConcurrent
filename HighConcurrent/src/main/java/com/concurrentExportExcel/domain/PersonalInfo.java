package com.concurrentExportExcel.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 个人信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PersonalInfo {
    private String name;
    private String idCode;
    private String phone;
}