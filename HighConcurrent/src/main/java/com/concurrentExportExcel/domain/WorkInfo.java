package com.concurrentExportExcel.domain;

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
    private String workName;
    private String address;
}