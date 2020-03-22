package com.concurrentExportExcel.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 车辆信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CarInfo {

   @ExcelProperty(value = {"厂商"}, index = 0)
   private String carType;
   @ExcelProperty(value = {"牌照"}, index = 1)
   private String cardCode;

}