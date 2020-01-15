package com.zhouyuan.qiniu;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

/**
 * @description: 七牛云测试类
 * @author: yuand
 * @create: 2020-01-13 17:32
 **/
public class QiniuDemoTest {

    /**
     * 将图片上传到七牛云服务
     *      1.更新用户图片信息（用户id=key）
     *      2.访问图片
     *          存储空间分配的域名：http://q41bl82gz.bkt.clouddn.com/，加上上传的文件名后访问该链接即可查看图片，
     *          即http://q41bl82gz.bkt.clouddn.com/zhouTest
     *          更新图片之后：访问的时候，在请求连接后添加上时间戳或随机数，就可以刷新看到更新后的图片，不然看到的还是更新前的图片，如：
     *          http://q41bl82gz.bkt.clouddn.com/zhouTest?test=1234
     * 参见demo：https://developer.qiniu.com/kodo/sdk/1239/java#server-upload
     *
     */
    @Test
    public void uploadTest(){
        //构造一个带指定 Region 对象的配置类,指定上传文件服务器地址：region0代表华东地区
        Configuration cfg = new Configuration(Region.region0());
        //构造上传管理器
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "7VtKGQ4VlMTkZALCoL_nupzOYb3HYZPGEOP_vngt";
        String secretKey = "FtSvmDWnNpx7TlFp4zMHhPm_lfSOOoUWyuJUXdcz";
        String bucket = "ihrm-zhou";
        //图片路径
        String localFilePath = "C:\\Users\\yuand\\Pictures\\Saved Pictures\\th.jpg";
        //设置存入到存储空间的文件名，默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "zhouTest";
        //身份认证
        Auth auth = Auth.create(accessKey, secretKey);
        //指定覆盖上传，添加传参-文件名以指定上传方式为覆盖上传，即支持更新操作
        String upToken = auth.uploadToken(bucket,key);
        try {
            //上传
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }


    }
}
