/*
 * MIT License
 *
 * Copyright (c) 2017 JinhuaiLee
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.grandlynn.utils.watermark;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * 利用Java代码给图片加水印
 *
 * @author jinhuailee
 * @date 2017/10/16 15:28
 */
public class WaterMarkUtil {
    /**
     * @param srcImgBytes      源图片二进制
     * @param waterMarkContent 水印内容
     * @param markContentColor 水印颜色
     * @param font             水印字体
     */
    public static byte[] addWaterMark(byte[] srcImgBytes, String waterMarkContent, Color markContentColor, Font font) {

        try {
            // 读取原图片信息

            //二进制转化为图片
            InputStream buffin = new ByteArrayInputStream(srcImgBytes);
            Image srcImg = ImageIO.read(buffin);
            //获取图片的宽
            int srcImgWidth = srcImg.getWidth(null);
            //获取图片的高
            int srcImgHeight = srcImg.getHeight(null);
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            //根据图片的背景设置水印颜色
            g.setColor(markContentColor);
            //设置字体
            g.setFont(font);

            //设置水印的坐标
            int x = srcImgWidth - 1 * getWatermarkLength(waterMarkContent, g) - 10;
            int y = srcImgHeight - 10;
            //画出水印
            g.drawString(waterMarkContent, x, y);
            g.dispose();
            // 输出图片
            ByteArrayOutputStream outImgStream = new ByteArrayOutputStream();
            ImageIO.write(bufImg, "jpg", outImgStream);
            return outImgStream.toByteArray();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return srcImgBytes;
    }

    private static int getWatermarkLength(String waterMarkContent, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
    }
}
