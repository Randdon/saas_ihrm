package com.zhouyuan.poi;

import com.zhouyuan.poi.handler.SheetHandler;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
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

    /**
     * 使用事件模式完成百万数据报表的解析
     * @throws OpenXML4JException
     * @throws IOException
     * @throws SAXException
     */
    @Test
    public void parseBillionExcel() throws OpenXML4JException, IOException, SAXException {
        String path = System.getProperty("user.dir") + File.separator + "test" + File.separator + "billionData.xlsx";
        //以压缩包的方式打开xlsx文件，设置为只读模式
        OPCPackage opcPackage = OPCPackage.open(path, PackageAccess.READ);
        //用压缩包构造XSSFReader对象
        XSSFReader xssfReader = new XSSFReader(opcPackage);

        //使用工厂创建XMLReader对象
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();

        //从XSSFReader中获取SharedStringsTable对象
        SharedStringsTable sharedStringsTable = xssfReader.getSharedStringsTable();
        //从XSSFReader中获取StylesTable对象
        StylesTable stylesTable = xssfReader.getStylesTable();
        //使用StylesTable对象和SharedStringsTable对象以及自定义的事件处理器SheetHandler来构造XSSFSheetXMLHandler事件处理器
        XSSFSheetXMLHandler xmlHandler =
                new XSSFSheetXMLHandler(stylesTable,sharedStringsTable,new SheetHandler(),false);

        //将XSSFSheetXMLHandler事件处理器注册到XMLReader对象中
        xmlReader.setContentHandler(xmlHandler);

        //从XSSFReader中获取该xlsx文件的全部sheet表单集合的迭代器
        XSSFReader.SheetIterator sheetsData = (XSSFReader.SheetIterator) xssfReader.getSheetsData();

        //循环sheet表单集合，解析每个表单
        InputSource inputSource;
        while (sheetsData.hasNext()){
            inputSource = new InputSource(sheetsData.next());
            //使用xmlReader解析表单，实际上调用的是注册在xmlReader中的事件处理器来处理，
            // 即会走我们自定义事件处理器代码中的逻辑，见SheetHandler
            xmlReader.parse(inputSource);
        }
    }
}
