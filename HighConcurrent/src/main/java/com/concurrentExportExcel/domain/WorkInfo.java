package com.concurrentExportExcel.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 工作信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WorkInfo {
    @ExcelProperty(value = {"工作名称"}, index = 0)
    private String workName;
    @ExcelProperty(value = {"单位地址"}, index = 1)
    private String address;
}