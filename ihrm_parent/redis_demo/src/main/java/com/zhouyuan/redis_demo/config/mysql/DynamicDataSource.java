package com.zhouyuan.redis_demo.config.mysql;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @description: 动态数据源配置
 * @author: yuand
 * @create: 2020-04-29 17:24
 * @modifiedBy: yuand
 **/
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        // 可以做一个简单的负载均衡策略
        String lookupKey = DynamicDataSourceHolder.getDataSource();
        System.out.println("------------lookupKey---------" + lookupKey);
        return lookupKey;
    }
}
