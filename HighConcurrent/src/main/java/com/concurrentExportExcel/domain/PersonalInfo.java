package com.concurrentExportExcel.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
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
    @ExcelProperty(value = {"姓名"}, index = 0)
    private String name;
    @ExcelProperty(value = {"身份证号"}, index = 1)
    private String idCode;
    @ExcelProperty(value = {"手机号"}, index = 2)
    private String phone;
}