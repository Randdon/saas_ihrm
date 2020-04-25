package com.ihrm.company.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;

/**
 * @auther: chenjh
 * @time: 2019/4/16 20:04
 * @description
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    private final static Logger LOGGER = LoggerFactory.getLogger(WebConfig.class);
    /**
     * 访问外部文件配置
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String filePath = System.getProperty("user.dir")+ File.separator+"ihrm_company"+File.separator+"src"+File.separator
                +"main"+File.separator+"file"+File.separator;
        LOGGER.info("Add resource locations: {}", filePath);
        registry.addResourceHandler("/**").addResourceLocations("file:" + filePath);
        super.addResourceHandlers(registry);
    }
}
