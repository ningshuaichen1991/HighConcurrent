package com.concurrentExportExcel.service;
import java.lang.invoke.VolatileCallSite;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import	java.util.concurrent.locks.LockSupport;
import java.io.ByteArrayOutputStream;
import java.util.List;
import	java.util.concurrent.CountDownLatch;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.common.FileUtil;
import com.concurrentExportExcel.domain.CarInfo;
import com.concurrentExportExcel.domain.PersonalInfo;
import com.concurrentExportExcel.domain.WorkInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
public class ConCurrentExportExcel {


    /**
     * CountDownLatch 导出
     * @param response
     * @throws Exception
     */
    public   void  exportCountDownLatch(HttpServletResponse response) throws Exception{
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        CountDownLatch cont = new CountDownLatch(3);
        //个人信息
        new Thread(()->{
            // 创建sheet
            List<PersonalInfo> personalInfos = selectPersonalInfoList();
            HSSFSheet sheet=null;
            synchronized (hssfWorkbook){
                sheet = hssfWorkbook.createSheet("个人信息");
            }
            HSSFRow headRow = sheet.createRow(0);
            String [] headerArray = {"姓名","身份证号","手机号"};
            for(int i=0;i<headerArray.length;i++){
                headRow.createCell(i).setCellValue(headerArray[i]);
            }
            for(int i = 0;i<personalInfos.size();i++){
                int j=0;
                HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
                dataRow.createCell(j++).setCellValue(personalInfos.get(i).getName());
                dataRow.createCell(j++).setCellValue(personalInfos.get(i).getIdCode());
                dataRow.createCell(j++).setCellValue(personalInfos.get(i).getPhone());
            }
            cont.countDown();
            log.info("个人信息处理完成");

        }).start();

        //工作信息
        new Thread(()->{
            // 创建sheet
            List<WorkInfo> workInfos = selectWorkInfoList();
            HSSFSheet sheet = null;
            synchronized (hssfWorkbook){
                sheet = hssfWorkbook.createSheet("工作信息");
            }
            HSSFRow headRow = sheet.createRow(0);
            String [] headerArray = {"工作名称","公司地址"};
            for(int i=0;i<headerArray.length;i++){
                headRow.createCell(i).setCellValue(headerArray[i]);
            }
            for(int i = 0;i<workInfos.size();i++){
                int j=0;
                HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
                dataRow.createCell(j++).setCellValue(workInfos.get(i).getWorkName());
                dataRow.createCell(j++).setCellValue(workInfos.get(i).getAddress());
            }
            cont.countDown();
            log.info("工作信息处理完成");

        }).start();

        //车辆信息
        new Thread(()->{
            // 创建sheet
            List<CarInfo> carInfos = selectCardInfoList();
            HSSFSheet sheet=null;
            synchronized (hssfWorkbook){
                sheet = hssfWorkbook.createSheet("车辆信息");
            }
            HSSFRow headRow = sheet.createRow(0);
            String [] headerArray = {"厂商","牌照"};
            for(int i=0;i<headerArray.length;i++){
                headRow.createCell(i).setCellValue(headerArray[i]);
            }
            for(int i = 0;i<carInfos.size();i++){
                int j=0;
                HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
                dataRow.createCell(j++).setCellValue(carInfos.get(i).getCarType());
                dataRow.createCell(j++).setCellValue(carInfos.get(i).getCardCode());
            }
            cont.countDown();
            log.info("车辆信息处理完成");
        }).start();

        cont.await();
        // 将excel写到输出流返回
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        hssfWorkbook.write(byteArrayOutputStream);
        // 下载
        FileUtil.download(byteArrayOutputStream, response, "信息数据导出.xls");

    }


    /**
     * CountDownLatch 导出
     * @param response
     * @throws Exception
     */
    public   void  exportCountCyclicBarrier(HttpServletRequest request, HttpServletResponse response) throws Exception{

        String fileName="信息表汇总";
        //组装输出流
        response.setContentType("application/force-download");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + java.net.URLEncoder.encode(fileName, "UTF-8") + ".xlsx" + "\" ");
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
        //个人信息

        CyclicBarrier cy = new CyclicBarrier(4);

        new Thread(()->{
            WriteSheet pSheet = EasyExcel.writerSheet(0, "个人信息").head(PersonalInfo.class).build();
            List<PersonalInfo> personalInfoList = selectPersonalInfoList();
            synchronized (excelWriter){
                excelWriter.write(personalInfoList, pSheet);
            }
            try{
                cy.await();
            }catch (Exception e) {
                log.error("个人信息导出异常:",e);
            }
            log.info("个人信息处理完成");
        }).start();
        //工作信息
        new Thread(()->{
            WriteSheet wSheet = EasyExcel.writerSheet(1, "工作信息").head(WorkInfo.class).build();
            List<WorkInfo> workList = selectWorkInfoList();
            synchronized (excelWriter){
                excelWriter.write(workList, wSheet);
            }
            try{
                cy.await();
            }catch (Exception e) {
                log.error("工作信息导出异常:",e);
            }
            log.info("工作信息处理完成");
        }).start();
        //车辆信息
        new Thread(()->{
            WriteSheet cSheet = EasyExcel.writerSheet(2, "车辆信息").head(CarInfo.class).build();
            List<CarInfo> cardList = selectCardInfoList();
            synchronized (excelWriter){
                excelWriter.write(cardList, cSheet);
            }
            try{
                cy.await();
            }catch (Exception e) {
                log.error("车辆信息导出异常:",e);
            }
            log.info("车辆信息处理完成");
        }).start();

        try{
            cy.await();
            excelWriter.finish();
        }catch (Exception e) {
            log.error("导出异常",e);
        }finally {
            response.getOutputStream().close();
        }
    }



    /**
     * 模拟获取个人信息
     * @return
     */
    private List<PersonalInfo> selectPersonalInfoList(){
        List<PersonalInfo> persons = Lists.newArrayList();
        String name [] = {"小李","小王","小张"};
        for(int i = 0;i<name.length; i++){
            persons.add(new PersonalInfo(name[i],"1523232X"+i,"1300000000"+i));
        }
        return persons;
    }

    /**
     * 模拟获取个人信息
     * @return
     */
    private List<WorkInfo> selectWorkInfoList(){
        List<WorkInfo> workInfos = Lists.newArrayList();
        String name [] = {"会计","销售","业务员"};
        for(int i = 0;i<name.length; i++){
            workInfos.add(new WorkInfo(name[i],"天宫"+(i+1)+"号"));
        }
        return workInfos;
    }

    /**
     * 模拟获取车辆信息
     * @return
     */
    private List<CarInfo> selectCardInfoList(){
        List<CarInfo> carInfos = Lists.newArrayList();
        String carType [] = {"奥迪","大众","福特"};
        for(int i = 0;i<carType.length; i++){
            carInfos.add(new CarInfo(carType[i],"鲁A2122"+i));
        }
        return carInfos;
    }
}