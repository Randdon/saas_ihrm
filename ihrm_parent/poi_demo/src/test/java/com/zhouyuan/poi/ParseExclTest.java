package com.zhouyuan.poi;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.IOException;

/**
 * @description: 利用apache poi的用户模型解析一个excel，该模型无法处理百万量级的excel，会oom
 * @author: yuand
 * @create: 2019-12-30 10:45
 **/
public class ParseExclTest {

    @Test
    public void parseExcl() throws IOException {
        //根据excl文件创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook("D:\\projects\\zhouyuan\\saas_ihrm\\ihrm_parent\\poi_demo\\test\\demo.xlsx");
        //根据sheet索引获取表单sheet对象
        XSSFSheet sheet = workbook.getSheetAt(0);
        /**
         * 获取该表单最后一行的行索引（从0开始）
         */
        int lastRowIndex = sheet.getLastRowNum();
        int lastCellNum;
        XSSFRow row;
        XSSFCell cell;
        StringBuilder sb;
        //遍历循环获取该sheet表单内的每一行和每一个单元格
        for (int rowIndex = 0; rowIndex < lastRowIndex + 1; rowIndex++) {
            //根据行索引从表单中获取行对象
            row = sheet.getRow(rowIndex);
            if (null == row){
                continue;
            }
            System.out.println("rowNum:" + (rowIndex + 1));
            /**
             * 获取该行最后一个单元格的编号（从1开始）
             */
            lastCellNum = row.getLastCellNum();
            sb = new StringBuilder();
            for (int cellNum = 0; cellNum < lastCellNum; cellNum++) {
                //根据单元格编号从行对象中获取单元格对象
                cell = row.getCell(cellNum);
                if (null == cell){
                    continue;
                }
                //获取该单元格的数据值
                Object cellValue = getCellValue(cell);
                sb.append(cellValue).append("**");
            }
            System.out.println(sb);
        }
    }

    /**
     * 获取单元格的数据
     * @param cell
     * @return
     */
    public Object getCellValue(XSSFCell cell){
        //得到单元格的数据类型
        CellType cellType = cell.getCellType();
        Object value = null;
        //根据单元格数据类型获取数据
        switch (cellType){
            case STRING:
                value = cell.getStringCellValue();
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case FORMULA:
                //公式
                value = cell.getCellFormula();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)){
                    //日期格式数据
                    value = cell.getLocalDateTimeCellValue();
                }else {
                    //数字
                    value = cell.getNumericCellValue();
                }
            default:
                break;
        }
        return value;
    }
}
