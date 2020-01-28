package com.zhouyuan.baiduface;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * 通过zxing生成二维码（保存到本地图片，支持以data url的形式体现）
 */
public class QRCodeTest {

    @Test
    public void qrCodeTest() throws WriterException, IOException {
        //二维码中的信息
        String content = "https://www.marxists.org/chinese/";
        //创建QRCodeWriter对象
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        /**
         * 基本配置
         * 1.二维码信息
         * 2.图片类型
         * 3.宽度
         * 4.长度
         */
        BitMatrix matrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200);
        //保存二维码到本地
        MatrixToImageWriter.writeToPath(matrix,"png", Paths.get("E:\\SchoolWork\\program\\projects\\ihrm\\saas_ihrm\\ihrm_parent\\baidu_face_demo\\src\\main\\resources\\facePhoto\\qrcode.png"));
    }
}
