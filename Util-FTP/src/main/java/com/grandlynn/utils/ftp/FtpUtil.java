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

package com.grandlynn.utils.ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * FTP工具类
 *
 * @author jinhuailee
 * @date 2017/10/16 15:26
 */
public class FtpUtil {
    /**
     * 根据指定参数获取FTP连接
     *
     * @param host     主机名
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public static FTPClient connectFtp(String host, int port, String username, String password) throws IOException {
        FTPClient ftpClient = new FTPClient();
        try {
            int reply;
            // 连接FTP服务器
            ftpClient.connect(host, port);
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            // 登录
            ftpClient.login(username, password);
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return ftpClient;
    }

    /**
     * 断开FTP连接
     *
     * @param ftpClient 已连接的FTP
     */
    public static void disconnectFtp(FTPClient ftpClient) throws IOException {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
    }


    /**
     * Description: 向FTP服务器上传文件
     *
     * @param ftpClient 已连接的FTP
     * @param basePath  FTP服务器基础目录
     * @param filePath  FTP服务器文件存放路径。例如分日期存放：/2015/01/01。文件的路径为basePath+filePath
     * @param fileName  上传到FTP服务器上的文件名
     * @param input     输入流
     * @return 成功返回true，否则返回false
     */
    public static boolean uploadFile(FTPClient ftpClient, String basePath, String filePath, String fileName, InputStream input) throws IOException {
        boolean result = false;
        if (ftpClient.isConnected()) {
            try {
                //切换到上传目录
                if (!ftpClient.changeWorkingDirectory(basePath + filePath)) {
                    //如果目录不存在创建目录
                    String[] dirs = filePath.split("/");
                    String tempPath = basePath;
                    for (String dir : dirs) {
                        if (null == dir || "".equals(dir)) {
                            continue;
                        }
                        tempPath += "/" + dir;
                        if (!ftpClient.changeWorkingDirectory(tempPath)) {
                            if (!ftpClient.makeDirectory(tempPath)) {
                                return result;
                            } else {
                                ftpClient.changeWorkingDirectory(tempPath);
                            }
                        }
                    }
                }
                //设置上传文件的类型为二进制类型
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                //上传文件
                if (!ftpClient.storeFile(fileName, input)) {
                    return result;
                }
                input.close();
                ftpClient.logout();
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
        return result;
    }

    /**
     * Description: 从FTP服务器下载文件
     *
     * @param ftpClient  已连接的FTP
     * @param remotePath FTP服务器上的相对路径
     * @param fileName   要下载的文件名
     * @param localPath  下载后保存到本地的路径
     * @return
     */
    public static boolean downloadFile(FTPClient ftpClient, String remotePath, String fileName, String localPath) throws IOException {
        boolean result = false;
        if (ftpClient.isConnected()) {
            try {
                // 转移到FTP服务器目录
                ftpClient.changeWorkingDirectory(remotePath);
                FTPFile[] fs = ftpClient.listFiles();
                for (FTPFile ff : fs) {
                    if (ff.getName().equals(fileName)) {
                        File p = new File(localPath);
                        if (p.isDirectory() && !p.exists()) {
                            p.mkdirs();
                        }
                        File localFile = new File(localPath + "/" + ff.getName());
                        OutputStream is = new FileOutputStream(localFile);
                        ftpClient.retrieveFile(ff.getName(), is);
                        is.close();
                    }
                }
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
        return result;
    }

    /**
     * 返回FTP目录下的文件列表
     *
     * @param ftpClient  已连接的FTP
     * @param remotePath FTP服务器目录
     * @return
     */

    public static List<String> getFileNameList(FTPClient ftpClient, String remotePath) throws IOException {
        List<String> list = new ArrayList<>();
        if (ftpClient.isConnected()) {
            try {
                String[] fileNames = ftpClient.listNames(remotePath);
                list = Arrays.asList(fileNames);
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
        return list;

    }

    /**
     * 读取ftp文件
     *
     * @param ftpClient  已连接的FTP
     * @param remotePath FTP服务器目录
     * @param fileName   读取的文件名
     * @return
     */
    public static InputStream getFileStream(FTPClient ftpClient, String remotePath, String fileName) throws IOException {
        InputStream ins = null;
        if (ftpClient.isConnected()) {
            try {
                // 转移到FTP服务器目录
                ftpClient.changeWorkingDirectory(remotePath);
                ins = ftpClient.retrieveFileStream(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
        return ins;
    }


    /**
     * 删除FTP上的文件
     *
     * @param ftpClient  已连接的FTP
     * @param remotePath FTP服务器目录
     * @param fileName   FTP服务器上要删除的的文件名
     * @return
     */

    public static boolean deleteFile(FTPClient ftpClient, String remotePath, String fileName) throws IOException {
        boolean result = false;
        if (ftpClient.isConnected()) {
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                result = ftpClient.deleteFile(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
        return result;
    }


    /**
     * 删除FTP目录
     *
     * @param ftpClient  已连接的FTP
     * @param remotePath FTP服务器目录
     * @return
     */

    public static boolean deleteDirectory(FTPClient ftpClient, String remotePath) throws IOException {
        boolean result = false;
        if (ftpClient.isConnected()) {
            try {
                int deleCnt = ftpClient.dele(remotePath);
                if (deleCnt > 0) {
                    result = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
        return result;
    }

}
