package com.zhouyuan.jasper_spring.controller;

import com.zhouyuan.jasper_spring.entity.User;
import com.zhouyuan.jasper_spring.entity.UserCount;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
     * 基于JDBC数据源的形式填充数据到jasper生成的模板文件中，但涉及数据库连接关闭和数据泄露的问题
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
             * 添加数据库查询条件参数
             */
            Map map = new HashMap<>();
            map.put("companyId","1");
            Connection connection = getConnection();
            JasperPrint print = JasperFillManager.
                    fillReport(inputStream, map, connection);
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
     * 基于JavaBean数据源的形式填充数据到jasper生成的模板文件中
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping(value = "/testJasper/fillDataByJavaBean")
    public void fillDataByJavaBean(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ServletOutputStream outputStream = null;
        InputStream inputStream = null;
        //1.引入pdf模板jasper文件
        Resource resource = new ClassPathResource("/jasperTemplates/fillDataByJavaBean.jasper");
        try {
            inputStream = resource.getInputStream();
            outputStream = response.getOutputStream();

            /**
             * 2.用Javabean的jasper数据源对象作为参数创建JasperPrint,向jasper文件中填充数据
             */
            Map map = new HashMap<>();
            List<User> userList = getUserList();
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(userList);
            JasperPrint print = JasperFillManager.
                    fillReport(inputStream, map, dataSource);
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
     * 基于JavaBean数据源的形式填充数据到jasper生成的模板文件中
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping(value = "/testJasper/groupBy")
    public void groupBy(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ServletOutputStream outputStream = null;
        InputStream inputStream = null;
        //1.引入pdf模板jasper文件
        Resource resource = new ClassPathResource("/jasperTemplates/GroupBy.jasper");
        try {
            inputStream = resource.getInputStream();
            outputStream = response.getOutputStream();

            /**
             * 2.用Javabean的jasper数据源对象作为参数创建JasperPrint,向jasper文件中填充数据
             */
            Map map = new HashMap<>();
            List<User> userList = getUserList();
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(userList);
            JasperPrint print = JasperFillManager.
                    fillReport(inputStream, map, dataSource);
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
     * 基于JavaBean数据源的形式填充数据到jasper生成的模板文件中
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping(value = "/testJasper/chart")
    public void chart(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ServletOutputStream outputStream = null;
        InputStream inputStream = null;
        //1.引入pdf模板jasper文件
        Resource resource = new ClassPathResource("/jasperTemplates/chart.jasper");
        try {
            inputStream = resource.getInputStream();
            outputStream = response.getOutputStream();

            /**
             * 2.用Javabean的jasper数据源对象作为参数创建JasperPrint,向jasper文件中填充数据
             */
            Map map = new HashMap<>();
            List<UserCount> userList = getUserCountList();
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(userList);
            JasperPrint print = JasperFillManager.
                    fillReport(inputStream, map, dataSource);
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

    /**
     * 构造用来填充pdf的JavaBean假数据
     * @return
     */
    public List<User> getUserList(){
        ArrayList<User> users = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            users.add(new User(
                    i+"",
                    "marxFollower"+i,
                    "Marx",
                    "Paris Commune",
                    "1884000000"+i));
        }
        for (int i = 0; i < 5; i++) {
            users.add(new User(
                    i+"",
                    "maoFollower"+i,
                    "Mao",
                    "Cultrue Revolution",
                    "1949000000"+i));
        }
        return users;
    }

    /**
     * 构造用来填充有饼状图的pdf的JavaBean假数据
     * @return
     */
    public List<UserCount> getUserCountList(){
        ArrayList<UserCount> users = new ArrayList<>(4);
        UserCount softStone = new UserCount("softStone", 800L);
        UserCount huawei = new UserCount("huawei", 1000L);
        UserCount ali = new UserCount("alibaba",2000L);
        UserCount revolution = new UserCount("格勒",5000L);
        users.add(softStone);
        users.add(huawei);
        users.add(ali);
        users.add(revolution);
        return users;
    }

}
