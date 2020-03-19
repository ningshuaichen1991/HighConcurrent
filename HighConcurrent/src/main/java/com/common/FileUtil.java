package com.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import java.io.*;

/**
 * 文件下载
 */
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * @param byteArrayOutputStream 将文件内容写入ByteArrayOutputStream
     * @param response              HttpServletResponse	写入response
     * @param returnName            返回的文件名
     */
    public static void download(ByteArrayOutputStream byteArrayOutputStream, HttpServletResponse response, String returnName) throws IOException {
        response.setContentType("application/octet-stream;charset=utf-8");
        returnName = new String(returnName.getBytes(), "iso8859-1");
        logger.info("导出文件工具类，fileName=" + returnName);
        logger.info("导出文件工具类，response：" + response + "returnName:" + returnName);
        response.setHeader("Content-Disposition", "attachment;filename=" + returnName);
        response.setContentLength(byteArrayOutputStream.size());
        ServletOutputStream outputstream = response.getOutputStream();    //取得输出流
        byteArrayOutputStream.writeTo(outputstream);                    //写到输出流
        outputstream.flush();                                            //刷数据
        byteArrayOutputStream.close();
        if (outputstream != null) {
            outputstream.close();//关闭
        }
    }
}
