package com.zhouyuan.poi;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @description: 利用apache poi创建一个excel
 * @author: yuand
 * @create: 2019-12-30 10:45
 **/
public class CreatExclTest {

    @Test
    public void createExcl() throws IOException {
        //创建excel工作簿，HSSFWorkbook：2003版（xls）；XSSFWorkbook：2007版（xlsx）
        Workbook workbook = new XSSFWorkbook();
        //创建表单sheet
        workbook.createSheet("poi_create");
        //创建文件输出流
        FileOutputStream outputStream = new FileOutputStream("D:\\projects\\zhouyuan\\saas_ihrm\\ihrm_parent\\poi_demo\\test\\creat.xlsx");
        //写入文件
        workbook.write(outputStream);
        outputStream.close();
    }
}
