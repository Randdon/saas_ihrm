package com.zhouyuan.baiduface;

import com.baidu.aip.face.AipFace;
import com.baidu.aip.util.Base64Util;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * @description: 百度云人脸识别测试类
 *              官方文档：https://cloud.baidu.com/doc/FACE/s/8k37c1rqz
 * @author: yuand
 * @create: 2020-01-21 17:29
 **/
public class test {

    @Test
    public void faceRegisterTest() throws IOException {
        //1.创建java代码和百度云交互的client对象
        AipFace client = new AipFace("18325919",
                "x8yItPvFygrLvvxhzL0PXPRI", "3XAiGjFQ6vt2HUhESGbvGv0Kg3zwuQq5");
        //2.参数设置
        HashMap<String,String> options = new HashMap<>(2);
        //图片质量  NONE  LOW  NORMAL，HIGH
        options.put("quality_control", "NORMAL");
        //活体检测
        options.put("liveness_control", "LOW");
        //3.构造图片
        String path = "D:\\projects\\zhouyuan\\saas_ihrm\\ihrm_parent\\baidu_face_demo\\src\\main\\resources\\facePhoto\\001.png";
        //上传的图片  两种格式 ： url地址，Base64字符串形式
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String photoData = Base64Util.encode(bytes);
        /**
         * 4.调用api方法完成人脸注册
         * 参数一：（图片的url或者图片的Base64字符串），
         * 参数二：图片形式（URL,BASE64）
         * 参数三：组ID（自定义字符串）
         * 参数四：用户ID（业务意义上的用户id）
         * 参数五：hashMap中的基本参数配置
         */
        JSONObject jsonObject = client.addUser(photoData, "BASE64", "zhouyuanTest", "10000", options);
        System.out.println(jsonObject);
    }
}
