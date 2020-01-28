package com.zhouyuan.baiduface;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
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

    /**
     * 通过zxing生成dataUrl形式的二维码
     * 我自己的写法
     * @throws WriterException
     * @throws IOException
     */
    @Test
    public void getQrCodeByDataUrl() throws WriterException, IOException {
        //二维码中的信息
        String content = "https://www.marxists.org/chinese/";
        //创建QRCodeWriter对象
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        //基本配置
        BitMatrix matrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200);
        //创建ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        /**
         * 将二维码数据保存到ByteArrayOutputStream
         * 1.二维码信息
         * 2.图片格式
         * 3.outputStream
         */
        MatrixToImageWriter.writeToStream(matrix,"png",outputStream);
        //对byte数组进行base64处理
        String dataUrl = Base64.encode(outputStream.toByteArray());
        dataUrl = "data:image/png;base64," + dataUrl;
        System.out.println(dataUrl);
    }

    /**
     * 通过zxing生成dataUrl形式的二维码
     * 教程中的写法
     * @throws WriterException
     * @throws IOException
     */
    @Test
    public void getQrCodeByDataUrl1() throws WriterException, IOException {
        //二维码中的信息
        String content = "https://www.marxists.org/chinese/";
        //创建QRCodeWriter对象
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        //基本配置
        BitMatrix matrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200);
        //创建ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //将二维码数据以byte数组的形式保存到ByteArrayOutputStream
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(matrix);
        /**
         * 1.image对象
         * 2.图片格式
         * 3.outputStream
         */
        ImageIO.write(bufferedImage,"png",outputStream);
        //对byte数组进行base64处理
        String dataUrl = Base64.encode(outputStream.toByteArray());
        dataUrl = "data:image/png;base64," + dataUrl;
        System.out.println(dataUrl);
    }
}
