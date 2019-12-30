package com.zhouyuan.poi;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileInputStream;
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
        //创建表单sheet 参数：表单名称
        Sheet sheet = workbook.createSheet("poi_create");
        //创建行，参数：行索引（从0开始）
        Row row = sheet.createRow(2);//创建第3行
        //创建单元格，参数：列索引（从0开始）
        Cell cell = row.createCell(2);//创建第3行第3列的单元格
        //向单元格中写入内容
        cell.setCellValue("世界人民大团结万岁！");

        //设置单元格样式
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);//设置下边框为细线
        cellStyle.setBorderLeft(BorderStyle.THIN);//设置左边框为细线
        cellStyle.setBorderRight(BorderStyle.THIN);//设置右边框为细线
        cellStyle.setBorderTop(BorderStyle.THIN);//设置上边框为细线
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        //创建字体对象
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 28);//字号
        font.setFontName("楷体");//字体

        //将字体设置到单元格样式中
        cellStyle.setFont(font);

        //将单元格样式设置到单元格中
        cell.setCellStyle(cellStyle);

        row.setHeightInPoints(50);//行高，单位为磅
        //第一个参数为指定列索引
        sheet.setColumnWidth(2,50*256);//列宽，指字符宽度

        /**
         * 使用poi为excel中添加图片
         */
        //创建图片文件输入流
        FileInputStream inputStream = new FileInputStream("D:\\projects\\zhouyuan\\saas_ihrm\\ihrm_parent\\poi_demo\\test\\hammer.png");
        //将图片文件输入流转化为二进制数组
        byte[] bytes = IOUtils.toByteArray(inputStream);
        //将该图片添加进poi内存中，返回该图片在图片集合中的索引，参数1：图片的二进制数组；图片2：图片类型
        int picIndex = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
        //利用工作簿对象获取绘制图片工具类
        CreationHelper creationHelper = workbook.getCreationHelper();
        //利用绘制图片工具类获取锚点对象
        ClientAnchor anchor = creationHelper.createClientAnchor();
        //设置图片坐标
        anchor.setCol1(3);
        anchor.setRow1(3);
        //利用表单对象获取绘图对象
        Drawing<?> drawingPatriarch = sheet.createDrawingPatriarch();
        //使用绘图对象，根据图片位置锚点及图片索引绘制图片
        Picture picture = drawingPatriarch.createPicture(anchor, picIndex);
        //自适应渲染图片，必须有这行代码，不然创建的xlsx里没有图片
        picture.resize();

        //创建文件输出流
        FileOutputStream outputStream = new FileOutputStream("D:\\projects\\zhouyuan\\saas_ihrm\\ihrm_parent\\poi_demo\\test\\creat.xlsx");
        //写入文件
        workbook.write(outputStream);
        outputStream.close();
    }
}
