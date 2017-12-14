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

import java.io.*;

/**
 * 文件写入工具类
 *
 * @author jinhuailee
 * @date 2017/12/14 10:27
 */
public class FileWriteUtil {

    /**
     * FileWriter 和BufferedWriter 结合写入文件
     * <p>
     * FileWriter是字符流写入字符到文件。默认情况下，它会使用新的内容代替文件原有的所有内容，但是，当指定一个true值作为FileWriter构造函数的第二个参数，它会保留现有的内容，并追加新内容在文件的末尾。
     * <p>
     * BufferedWriter:缓冲字符，是一个字符流类来处理字符数据。不同于字节流（数据转换成字节FileOutPutStream），可以直接写字符串、数组或字符数据保存到文件。
     *
     * @param filePath 文件路径加文件名
     * @param content  写入的内容
     * @param append   是否追加 true表示可以追加新内容  false 表示不追加
     * @throws IOException
     */
    public static void writeInFileByFB(String filePath, String content, boolean append) throws IOException {
        File f = new File(filePath);
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        FileWriter fw;
        BufferedWriter bw;
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            fw = new FileWriter(f.getAbsoluteFile(), append);
            bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * FileOutPutStream 字节流写入文件
     * <p>
     * 文件输出流是一种用于处理原始二进制数据的字节流泪。 为了将数据写入到文件中，必须将数据转换为字节，并保存到文件。
     *
     * @param filePath 文件路径加文件名
     * @param content  写入的内容
     * @throws IOException
     */
    public static void writeInFileByFO(String filePath, String content) throws IOException {
        File f = new File(filePath);
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        FileOutputStream fos = null;
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            fos = new FileOutputStream(f);
            fos.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }

        }
    }

    /**
     * RandomAccessFile 写入文件
     * <p>
     * RandomAccessFile的唯一父类是Object，与其他流父类不同。是用来访问那些保存数据记录的文件的，这样你就可以用seek( )方法来访问记录，并进行读写了。这些记录的大小不必相同；但是其大小和位置必须是可知的。
     *
     * @param filePath 文件路径加文件名
     * @param content  写入的内容
     * @throws IOException
     */
    public static void writeInFileByRdA(String filePath, String content) throws IOException {
        try {
            File f = new File(filePath);
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(filePath, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            //将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
