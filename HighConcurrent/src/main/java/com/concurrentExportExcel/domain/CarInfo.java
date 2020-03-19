package com.concurrentExportExcel.domain;

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
   private String cardCode;
   private String carType;


}