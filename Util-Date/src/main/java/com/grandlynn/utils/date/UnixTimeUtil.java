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

package com.grandlynn.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author jinhuailee
 * @date 2017/12/14 09:03
 */
public class UnixTimeUtil {

    /**
     * 将Unix时间戳转换成指定格式日期字符串
     *
     * @param timestampLong 时间戳 （精确到秒） 如："1473048265";
     * @param formats       要格式化的格式 默认："yyyy-MM-dd HH:mm:ss";
     * @return 返回结果 如："2016-09-05 16:06:42";
     */
    public static String TimeStamp2DateStr(Long timestampLong, String formats) {
        if ("".equals(formats)) {
            formats = "yyyy-MM-dd HH:mm:ss";
        }
        Long timestamp = timestampLong * 1000;
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }

    /**
     * 将Unix时间戳转换成日期格式
     *
     * @param timestampLong 时间戳 （精确到秒） 如："1473048265";
     * @return 返回结果 Date;
     */
    public static Date TimeStamp2Date(Long timestampLong) {
        String formats = "yyyy-MM-dd HH:mm:ss";
        Long timestamp = timestampLong * 1000;
        String dateStr = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        Date date = null;
        try {
            date = new SimpleDateFormat(formats).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 日期格式字符串转换成时间戳 （精确到秒）
     *
     * @param dateStr 字符串日期
     * @param format  如：yyyy-MM-dd HH:mm:ss
     * @return 时间戳 （精确到秒）
     */
    public static Long DateStr2TimeStamp(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return Long.valueOf(sdf.parse(dateStr).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Long.valueOf(0);
    }

    /**
     * 日期格式字符串转换成时间戳 （精确到秒）
     *
     * @param date 日期
     * @return 时间戳 （精确到秒）
     */
    public static Long Date2TimeStamp(Date date) {
        try {
            return Long.valueOf(date.getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Long.valueOf(0);
    }

    /**
     * 取得当前时间戳（精确到秒）
     *
     * @return nowTimeStamp
     */
    public static String getNowTimeStamp() {
        long time = System.currentTimeMillis();
        String nowTimeStamp = String.valueOf(time / 1000);
        return nowTimeStamp;
    }
}
