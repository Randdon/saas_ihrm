//package com.zhouyuan.redis_demo.config.mysql;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @description: 数据源配置
// * @author: yuand
// * @create: 2020-04-29 17:10
// * @modifiedBy: yuand
// **/
//@Configuration
//public class DataSourceConfig {
//
//    public final static String WRITE_DATASOURCE_KEY = "writeDruidDataSource";
//    public final static String READ_DATASOURCE_KEY = "readDruidDataSource";
//
//    @ConfigurationProperties(prefix = "spring.datasource.druid.read")
//    @Bean(name = READ_DATASOURCE_KEY)
//    public DataSource readDruidDataSource() {
//        return new DruidDataSource();
//    }
//
//    @ConfigurationProperties(prefix = "spring.datasource.druid.write")
//    @Bean(name = WRITE_DATASOURCE_KEY)
//    @Primary
//    public DataSource writeDruidDataSource() {
//        return new DruidDataSource();
//    }
//
//    /**
//     * 注入AbstractRoutingDataSource
//     *
//     * @return
//     * @throws Exception
//     */
//    @Bean(name = "routingDataSource")
//    public AbstractRoutingDataSource routingDataSource() throws Exception {
//        DynamicDataSource dataSource = new DynamicDataSource();
//        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
//        targetDataSources.put(WRITE_DATASOURCE_KEY, writeDruidDataSource());
//        targetDataSources.put(READ_DATASOURCE_KEY, readDruidDataSource());
//        dataSource.setTargetDataSources(targetDataSources);// 配置数据源
//        dataSource.setDefaultTargetDataSource(writeDruidDataSource());// 默认为主库用于写数据
//        return dataSource;
//    }
//
//}
