package com.zhouyuan.jasper_spring.controller;

import net.sf.jasperreports.engine.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: Jasper整合Spring boot的controller测试
 * @author: yuand
 * @create: 2020-01-16 11:21
 **/
@RestController
public class JasperController {

    /**
     * 根据jasper模板创建pdf测试controller
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping(value = "/testJasper")
    public void createPdfByTemp(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ServletOutputStream outputStream = null;
        InputStream inputStream = null;
        //1.引入pdf模板jasper文件
        Resource resource = new ClassPathResource("/jasperTemplates/jrTest.jasper");
        try {
            inputStream = resource.getInputStream();
            outputStream = response.getOutputStream();

            /**
             * 2.创建JasperPrint,向jasper文件中填充数据
             * inputStream: jasper文件输入流
             * new HashMap ：向模板中输入的参数
             * JasperDataSource：数据源（和数据库数据源不同）
             *              填充模板的数据来源（可以是MySQL connection，javaBean，Map）
             *              如果不填充数据也必须有填充空数据来源的参数：JREmptyDataSource，
             *              不然导出的pdf是空白的，连模板里的样式也没有
             */
            JasperPrint print = JasperFillManager.
                    fillReport(inputStream, new HashMap<>(16), new JREmptyDataSource());
            //3.将JasperPrint以PDF流的形式输出
            JasperExportManager.exportReportToPdfStream(print,outputStream);
        } catch (JRException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != outputStream){
                outputStream.close();
            }
            if (null != inputStream){
                inputStream.close();
            }
        }
    }

    /**
     * 基于parameters以Map的形式填充数据到jasper生成的模板文件中
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping(value = "/testJasper/fillDataByParams")
    public void fillDataByParams(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ServletOutputStream outputStream = null;
        InputStream inputStream = null;
        //1.引入pdf模板jasper文件
        Resource resource = new ClassPathResource("/jasperTemplates/fillDataByParams.jasper");
        try {
            inputStream = resource.getInputStream();
            outputStream = response.getOutputStream();

            /**
             * 2.创建JasperPrint,向jasper文件中填充数据
             * 设置参数 参数的key 要严格等于 用jaspersoft Studio设计的模板中使用的parameters参数的name
             */
            Map map = new HashMap<>(4);
            map.put("username","zhouyuan");
            map.put("company","Paris Commune");
            map.put("department","Marx");
            map.put("mobile","12874569577");
            JasperPrint print = JasperFillManager.
                    fillReport(inputStream, map, new JREmptyDataSource());
            //3.将JasperPrint以PDF流的形式输出
            JasperExportManager.exportReportToPdfStream(print,outputStream);
        } catch (JRException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != outputStream){
                outputStream.close();
            }
            if (null != inputStream){
                inputStream.close();
            }
        }
    }

    /**
     * 基于JDBC数据源的形式填充数据到jasper生成的模板文件中
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping(value = "/testJasper/fillDataByJdbc")
    public void fillDataByJdbc(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ServletOutputStream outputStream = null;
        InputStream inputStream = null;
        //1.引入pdf模板jasper文件
        Resource resource = new ClassPathResource("/jasperTemplates/fillDataByJdbc.jasper");
        try {
            inputStream = resource.getInputStream();
            outputStream = response.getOutputStream();

            /**
             * 2.用数据库连接作为参数创建JasperPrint,向jasper文件中填充数据
             */
            Connection connection = getConnection();
            JasperPrint print = JasperFillManager.
                    fillReport(inputStream, new HashMap<>(), connection);
            //3.将JasperPrint以PDF流的形式输出
            JasperExportManager.exportReportToPdfStream(print,outputStream);
        } catch (JRException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (null != outputStream){
                outputStream.close();
            }
            if (null != inputStream){
                inputStream.close();
            }
        }
    }

    /**
     * 获取数据库连接
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ihrm?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC",
                "root",
                "root1234");
        return connection;
    }
}
