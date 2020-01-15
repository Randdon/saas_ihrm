package com.zhouyuan.jasper.report;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.view.JasperViewer;
import org.junit.Test;

import java.util.HashMap;

public class PDFTest {

    /**
     * 将pdf模板文件jxml编译为jasper文件
     */
    public static void createJasper(){
        try{
            String path = "D:\\projects\\zhouyuan\\saas_ihrm\\ihrm_parent\\jasper_demo\\test\\test.jrxml";
            JasperCompileManager.compileReportToFile(path);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 将jasper文件和数据进行填充，获取jrprint
     */
    public static void createJrprint(){
        try{
            String path = "D:\\projects\\zhouyuan\\saas_ihrm\\ihrm_parent\\jasper_demo\\test\\test01.jasper";
            //通过空参数和空数据源进行填充
            JasperFillManager.fillReportToFile(path,new HashMap(),new JREmptyDataSource());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 预览文件，该方法不能用单元测试来测试，看不到效果，需要用main方法来测试
     */
    public static void showPdf(){
        try{
            String path = "D:\\projects\\zhouyuan\\saas_ihrm\\ihrm_parent\\jasper_demo\\test\\test01.jrprint";
            JasperViewer.viewReport(path,false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        showPdf();
    }
}
