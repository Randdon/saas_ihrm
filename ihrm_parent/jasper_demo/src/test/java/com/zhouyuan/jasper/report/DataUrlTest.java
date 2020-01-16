package com.zhouyuan.jasper.report;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.io.File;
import java.io.IOException;

/**
 * @description: 测试dataurl形式的图片
 * @author: yuand
 * @create: 2020-01-16 10:17
 **/
public class DataUrlTest{

    @Test
    public void dataTest() throws IOException {
        File file = new File("D:\\projects\\zhouyuan\\saas_ihrm\\ihrm_parent\\poi_demo\\test\\hammer.png");
        byte[] bytes = FileUtils.readFileToByteArray(file);
        String imgUrl = Base64.encode(bytes);
        imgUrl = "data:image/png;base64," + imgUrl;
        System.out.println(imgUrl);
    }

}
