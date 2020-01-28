package com.zhouyuan.baiduface;

import com.baidu.aip.face.AipFace;
import com.baidu.aip.util.Base64Util;
import org.json.JSONObject;
import org.junit.Before;
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
public class BaiduAiFaceTest {

    private AipFace client;
    @Before
    public void init(){
        //1.创建java代码和百度云交互的client对象
        client = new AipFace("18325919",
                "x8yItPvFygrLvvxhzL0PXPRI", "3XAiGjFQ6vt2HUhESGbvGv0Kg3zwuQq5");
    }

    /**
     * 人脸注册：将用户的人脸图片上传到百度云人脸库你指定的群组里
     * @throws IOException
     */
    @Test
    public void faceRegisterTest() throws IOException {

        //2.参数设置
        HashMap<String,String> options = new HashMap<>(2);
        //图片质量  NONE  LOW  NORMAL，HIGH
        options.put("quality_control", "NORMAL");
        //活体检测
        options.put("liveness_control", "LOW");
        //3.构造图片
        String path = "E:\\SchoolWork\\program\\projects\\ihrm\\saas_ihrm\\ihrm_parent\\baidu_face_demo\\src\\main\\resources\\facePhoto\\001.png";
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
        System.out.println(jsonObject.toString(2));
    }

    /**
     * 人脸更新
     * @throws IOException
     */
    @Test
    public void faceUpdateTest() throws IOException {

        //2.参数设置
        HashMap<String,String> options = new HashMap<>(2);
        //图片质量  NONE  LOW  NORMAL，HIGH
        options.put("quality_control", "NORMAL");
        //活体检测
        options.put("liveness_control", "LOW");
        //3.构造图片
        String path = "E:\\SchoolWork\\program\\projects\\ihrm\\saas_ihrm\\ihrm_parent\\baidu_face_demo\\src\\main\\resources\\facePhoto\\002.png";
        //上传的图片  两种格式 ： url地址，Base64字符串形式
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String photoData = Base64Util.encode(bytes);
        /**
         * 4.调用api方法完成人脸更新
         * 参数一：（图片的url或者图片的Base64字符串），
         * 参数二：图片形式（URL,BASE64）
         * 参数三：组ID（自定义字符串）
         * 参数四：用户ID（业务意义上的用户id）
         * 参数五：hashMap中的基本参数配置
         */
        JSONObject jsonObject = client.updateUser(photoData, "BASE64", "zhouyuanTest", "10000", options);
        System.out.println(jsonObject.toString(2));
    }

    /**
     * 人脸检测：判断图片中是否具有面部信息
     * @throws IOException
     */
    @Test
    public void faceDetect() throws IOException {
        //构造图片
        String path = "E:\\SchoolWork\\program\\projects\\ihrm\\saas_ihrm\\ihrm_parent\\baidu_face_demo\\src\\main\\resources\\facePhoto\\002.png";
        //待检测的图片  两种格式 ： url地址，Base64字符串形式
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String photoData = Base64Util.encode(bytes);
        /**
         * 调用api方法完成人脸检测
         * 参数一：（图片的url或者图片的Base64字符串），
         * 参数二：图片形式（URL,BASE64）
         * 参数三：hashMap中的基本参数配置（null：使用默认配置）
         */
        JSONObject res = client.detect(photoData, "BASE64", null);
        System.out.println(res.toString(2));
    }

    /**
     * 人脸搜索：根据用户上传的图片和指定人脸库中的所有人脸进行比较，获取相似度最高的一个或者某几个的评分
     * 说明：返回值（数组，按相似度倒序排序，我们只获取相似度最高的那一条数据）
     * score：相似度评分（百度云推荐80分以上可以认为是同一个人）
     * @throws IOException
     */
    @Test
    public void faceSearch() throws IOException {
        //构造图片
        String path = "E:\\SchoolWork\\program\\projects\\ihrm\\saas_ihrm\\ihrm_parent\\baidu_face_demo\\src\\main\\resources\\facePhoto\\cat.jpg";
        //待检测的图片  两种格式 ： url地址，Base64字符串形式
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String photoData = Base64Util.encode(bytes);
        /**
         * 调用api方法完成人脸搜索
         * 参数一：（图片的url或者图片的Base64字符串），
         * 参数二：图片形式（URL,BASE64）
         * 参数三：hashMap中的基本参数配置（null：使用默认配置）
         */
        JSONObject res = client.search(photoData, "BASE64", "zhouyuanTest",null);
        System.out.println(res.toString(2));
    }

    @Test
    public void dataUrlTest() throws IOException {
        String imgUrl = Base64Util.encode(Files.readAllBytes(Paths.get("E:\\SchoolWork\\program\\projects\\ihrm\\saas_ihrm\\ihrm_parent\\baidu_face_demo\\src\\main\\resources\\facePhoto\\cat.jpg")));
        imgUrl = "data:image/jpg;base64," + imgUrl;
        System.out.println(imgUrl);
    }
}
