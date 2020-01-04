package com.zhouyuan.poi.handler;

import com.zhouyuan.poi.entity.PoiEntity;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

/**
 * @description: 自定义事件处理器
 *               处理每一行数据的读取
 *               实现poi事件模型下的SheetContentsHandler接口
 * @author: yuand
 * @create: 2020-01-03 15:09
 **/
public class SheetHandler implements XSSFSheetXMLHandler.SheetContentsHandler {
    private PoiEntity poiEntity;

    /**
     * 当开始解析某一行的时候触发该方法
     * @param i 行索引
     */
    @Override
    public void startRow(int i) {
        if (i > 0){
            //读取到非首行标题的时候实例化对象
            poiEntity = new PoiEntity();
        }
    }

    /**
     * 当结束解析某一行的时候出发该方法
     * @param i 行索引
     */
    @Override
    public void endRow(int i) {
        //使用对象进行业务操作，如入库
        System.out.println(poiEntity);
    }

    /**
     * 对行中的每一个单元格进行处理
     * @param cellRef 单元格名称
     * @param formattedValue 单元格的值
     * @param xssfComment 批注
     */
    @Override
    public void cell(String cellRef, String formattedValue, XSSFComment xssfComment) {
        //对对象属性赋值
        if (null != poiEntity){
            //从单元格名称中截取出列编码
            cellRef = cellRef.substring(0,1);
            switch (cellRef){
                case "A":
                    poiEntity.setId(formattedValue);
                    break;
                case "B":
                    poiEntity.setBreast(formattedValue);
                    break;
                case "C":
                    poiEntity.setAdipocytes(formattedValue);
                    break;
                case "D":
                    poiEntity.setNegative(formattedValue);
                    break;
                case "E":
                    poiEntity.setStaining(formattedValue);
                    break;
                case "F":
                    poiEntity.setSupportive(formattedValue);
                    break;
                default:
                    break;
            }
        }
    }
}
