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

package com.grandlynn.utils.file;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 文件读取工具类
 *
 * @author jinhuailee
 * @date 2017/12/14 09:19
 */
public class FileReadUtil {


    /**
     * 按照字节读取文件内容
     * FileInputStream 字节流读取文件 【注意：读取中文的时候会乱码】
     *
     * @param filePath 文件路径加文件名
     * @return
     * @throws IOException
     */
    public static String readFileByByte(String filePath) throws IOException {
        String s = "";
        File f = new File(filePath);
        InputStream in;
        try {
            in = new FileInputStream(f);
            int tempByte;
            while ((tempByte = in.read()) != -1) {
                System.out.println(tempByte);
                s += tempByte;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return s;
    }

    /**
     * 按照字符读取文件内容
     * InputStreamReader 字符流读取文件内容
     *
     * @param filePath 文件路径加文件名
     * @return
     * @throws IOException
     */
    public static String readFileByChar(String filePath) throws IOException {
        String s = "";
        File f = new File(filePath);
        Reader rdr = null;
        try {
            rdr = new InputStreamReader(new FileInputStream(f));
            int temp;
            while ((temp = rdr.read()) != -1) {
                //对于window下，\r\n这两个字符在一起时，表示一个换行。
                //但是如果这两个字符分开显示时，会换两行。
                //因此，屏蔽掉\r，或者屏蔽掉\n。否则，将会出现很多空行
                if (((char) temp) != '\r') {
                    s += (char) temp;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                rdr.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
        return s;
    }


    /**
     * 按照行读取文件
     * BufferedReader 以行为单位读取文件内容
     *
     * @param filePath 文件路径加文件名
     * @return
     * @throws IOException
     */
    public static String readFileByLine(String filePath) throws IOException {
        String s = "";
        File f = new File(filePath);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "utf-8"));
            String temp;
            while ((temp = br.readLine()) != null) {
                s += temp;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
        return s;
    }


    /**
     * 随机行读取文件
     * 随机读取文件中的部分内容： RandomAccessFile
     *
     * @param filePath 文件路径加文件名
     * @return
     * @throws IOException
     */
    public static String readFileByRand(String filePath) throws IOException {
        String s = "";
        File f = new File(filePath);
        RandomAccessFile raf = null;
        try {
            //打开一个文件流， 按只读方式
            raf = new RandomAccessFile(f.getName(), "r");
            //文件长度，字节数
            long fileLength = raf.length();
            //读文件的起始位置
            int beginIndex = (fileLength > 4) ? 4 : 0;
            //将读文件的开始位置移到beginIndex位置
            raf.seek(beginIndex);
            byte[] bytes = new byte[10];
            int byteread = 0;
            //一次读10个字节，如果文件内容不足10个字节，则读剩下的文字。
            //将一次读取的字节数赋给byteread
            while ((byteread = raf.read(bytes)) != -1) {
                System.out.write(bytes, 0, byteread);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
        return s;
    }

    /**
     * 显示输入流中还剩的字节数
     *
     * @param in 输入流
     */
    public static int showAvailableBytes(InputStream in) {
        try {
            return in.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 在线打开方式 下载
     *
     * @param filePath    文件路径
     * @param response
     * @param fileNewName 下载的新文件名
     * @throws Exception
     */
    public void downLoad(String filePath, HttpServletResponse response, String fileNewName) throws Exception {
        File f = new File(filePath);
        OutputStream out = response.getOutputStream();
        if (!f.exists()) {
            response.setCharacterEncoding("UTF-8");
            String notFileHtml = getNotFileHtml(filePath, "文件找不到");
            out.write(notFileHtml.getBytes("UTF-8"));
            out.flush();
            return;
        }
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len;
        // 非常重要
        response.reset();
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileNewName);

        while ((len = br.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        br.close();
        out.close();
    }

    private String getNotFileHtml(String filePath, String message) {
        return "<html><head><title>" + message + "</title></head><body>" + filePath + message + "</body></html>";
    }
}
