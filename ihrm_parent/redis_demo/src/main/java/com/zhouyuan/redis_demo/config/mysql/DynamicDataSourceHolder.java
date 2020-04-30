package com.zhouyuan.redis_demo.config.mysql;

/**
 * @description: 使用ThreadLocal使数据源与线程绑定
 * @author: yuand
 * @create: 2020-04-29 17:27
 * @modifiedBy: yuand
 **/
public class DynamicDataSourceHolder {

    // 使用ThreadLocal把数据源与当前线程绑定
    private static final ThreadLocal<String> dataSources = new ThreadLocal<String>();

    public static void setDataSource(String dataSourceName) {
        dataSources.set(dataSourceName);
    }

    public static String getDataSource() {
        return (String) dataSources.get();
    }

    public static void clearDataSource() {
        dataSources.remove();
    }

}
