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

package com.grandlynn.utils.http;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Vector;

/**
 * @author jinhuailee
 * @date 2017/12/12 16:14
 */
public class HttpRequester {
    private String defaultContentEncoding;

    private static final String HTTP_METHOD_GET = "GET";
    private static final String HTTP_METHOD_POST = "POST";

    public HttpRequester() {
        this.defaultContentEncoding = Charset.defaultCharset().name();
    }

    /**
     * 发送GET请求
     *
     * @param urlString URL地址
     * @return 响应对象
     * @throws IOException
     */
    public HttpResponse sendGetRequest(String urlString) throws IOException {
        return this.sendHttpRequest(urlString, "GET", null, null);
    }

    /**
     * 发送GET请求
     *
     * @param urlString URL地址
     * @return IO流
     * @throws IOException
     */
    public HttpResponse sendGetRequestResponseStream(String urlString) throws IOException {
        return this.sendHttpRequest(urlString, "GET", null, null);
    }

    /**
     * 发送GET请求
     *
     * @param urlString URL地址
     * @param params    参数集合
     * @return 响应对象
     * @throws IOException
     */
    public HttpResponse sendGetRequest(String urlString, Map<String, String> params) throws IOException {
        return this.sendHttpRequest(urlString, "GET", params, null);
    }

    /**
     * 发送GET请求
     *
     * @param urlString URL地址
     * @param params    参数集合
     * @return IO流
     * @throws IOException
     */
    public InputStream sendGetRequestResponseStream(String urlString, Map<String, String> params) throws IOException {
        return this.sendHttpRequestResponseStream(urlString, "GET", params, null);
    }

    /**
     * 发送GET请求
     *
     * @param urlString URL地址
     * @param params    参数集合
     * @param propertys 请求属性
     * @return 响应对象
     * @throws IOException
     */
    public HttpResponse sendGetRequest(String urlString, Map<String, String> params, Map<String, String> propertys) throws IOException {
        return this.sendHttpRequest(urlString, "GET", params, propertys);
    }

    /**
     * 发送GET请求
     *
     * @param urlString URL地址
     * @param params    参数集合
     * @param propertys 请求属性
     * @return IO流
     * @throws IOException
     */
    public InputStream sendGetRequestResponseStream(String urlString, Map<String, String> params, Map<String, String> propertys) throws IOException {
        return this.sendHttpRequestResponseStream(urlString, "GET", params, propertys);
    }

    /**
     * 发送POST请求
     *
     * @param urlString URL地址
     * @return 响应对象
     * @throws IOException
     */
    public HttpResponse sendPostRequest(String urlString) throws IOException {
        return this.sendHttpRequest(urlString, "POST", null, null);
    }

    /**
     * 发送POST请求
     *
     * @param urlString URL地址
     * @return IO流
     * @throws IOException
     */
    public InputStream sendPostRequestResponseStream(String urlString) throws IOException {
        return this.sendHttpRequestResponseStream(urlString, "POST", null, null);
    }

    /**
     * 发送POST请求
     *
     * @param urlString URL地址
     * @param params    参数集合
     * @return 响应对象
     * @throws IOException
     */
    public HttpResponse sendPostRequest(String urlString, Map<String, String> params) throws IOException {
        return this.sendHttpRequest(urlString, "POST", params, null);
    }

    /**
     * 以JSON传参方式发送POST请求
     *
     * @param urlString URL地址
     * @param object    对象
     * @return 响应对象
     * @throws IOException
     */
    public HttpResponse sendPostRequestJson(String urlString, Object object) throws IOException {
        return this.sendHttpRequestJson(urlString, "POST", object, null);
    }

    /**
     * 发送POST请求
     *
     * @param urlString URL地址
     * @param params    参数集合
     * @return IO流
     * @throws IOException
     */
    public InputStream sendPostRequestResponseStream(String urlString, Map<String, String> params) throws IOException {
        return this.sendHttpRequestResponseStream(urlString, "POST", params, null);
    }

    /**
     * 发送POST请求
     *
     * @param urlString URL地址
     * @param params    参数集合
     * @param propertys 请求属性
     * @return 响应对象
     * @throws IOException
     */
    public HttpResponse sendPostRequest(String urlString, Map<String, String> params, Map<String, String> propertys) throws IOException {
        return this.sendHttpRequest(urlString, "POST", params, propertys);
    }

    /**
     * 以JSON传参方式发送POST请求
     *
     * @param urlString URL地址
     * @param object    数据对象
     * @param propertys 请求属性
     * @return 响应对象
     * @throws IOException
     */
    public HttpResponse sendPostRequestJson(String urlString, Object object, Map<String, String> propertys) throws IOException {
        return this.sendHttpRequestJson(urlString, "POST", object, propertys);
    }

    /**
     * 发送POST请求
     *
     * @param urlString URL地址
     * @param params    参数集合
     * @param propertys 请求属性
     * @return IO流
     * @throws IOException
     */
    public InputStream sendPostRequestResponseStream(String urlString, Map<String, String> params, Map<String, String> propertys) throws IOException {
        return this.sendHttpRequestResponseStream(urlString, "POST", params, propertys);
    }

    /**
     * 发送HTTP请求
     *
     * @param urlString
     * @return 响映对象
     * @throws IOException
     */
    private HttpResponse sendHttpRequest(String urlString, String method, Map<String, String> parameters, Map<String, String> propertys) throws IOException {
        HttpURLConnection urlConnection;

        if (method.equalsIgnoreCase(HTTP_METHOD_GET) && parameters != null) {
            urlString += getParamStrBuffer(parameters);
        }
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod(method);
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);

        if (propertys != null) {
            for (String key : propertys.keySet()) {
                urlConnection.addRequestProperty(key, propertys.get(key));
            }
        }

        if (method.equalsIgnoreCase(HTTP_METHOD_POST) && parameters != null) {
            StringBuffer param = new StringBuffer();
            for (String key : parameters.keySet()) {
                param.append("&");
                param.append(key).append("=").append(parameters.get(key));
            }
            urlConnection.getOutputStream().write(param.toString().getBytes());
            urlConnection.getOutputStream().flush();
            urlConnection.getOutputStream().close();
        }

        return this.makeContent(urlString, urlConnection);
    }

    /**
     * 发送HTTP请求
     * JSON字符串的参数
     *
     * @param urlString
     * @param method
     * @param object
     * @param propertys
     * @throws IOException
     * @return响映对象
     */
    private HttpResponse sendHttpRequestJson(String urlString, String method, Object object, Map<String, String> propertys) throws IOException {
        HttpURLConnection urlConnection;

        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod(method);
        // 设置允许输出
        urlConnection.setDoOutput(true);
        // 设置允许输入
        urlConnection.setDoInput(true);
        // 设置不用缓存
        urlConnection.setUseCaches(false);

        // 设置维持长连接
        urlConnection.setRequestProperty("Connection", "Keep-Alive");

        if (propertys != null) {
            for (String key : propertys.keySet()) {
                urlConnection.addRequestProperty(key, propertys.get(key));
            }
        }

        if (object != null) {
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(object));

            //转换为字节数组
            byte[] writeBytes = jsonObject.toString().getBytes();
            // 设置文件长度
            urlConnection.setRequestProperty("Content-Length", String.valueOf(writeBytes.length));
            // 设置文件类型
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.getOutputStream().write(writeBytes);
            urlConnection.getOutputStream().flush();
            urlConnection.getOutputStream().close();
        }

        return this.makeContent(urlString, urlConnection);
    }

    /**
     * 发送HTTP请求
     *
     * @param urlString
     * @param method
     * @param parameters
     * @param propertys
     * @throws IOException
     * @returnIO流
     */
    private InputStream sendHttpRequestResponseStream(String urlString, String method, Map<String, String> parameters, Map<String, String> propertys) throws IOException {
        HttpURLConnection urlConnection;

        urlString += getParamStrBuffer(parameters);
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod(method);
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);

        if (propertys != null) {
            for (String key : propertys.keySet()) {
                urlConnection.addRequestProperty(key, propertys.get(key));
            }
        }

        return urlConnection.getInputStream();
    }

    /**
     * 得到响应对象
     *
     * @param urlString
     * @param urlConnection
     * @throws IOException
     * @return响应对象
     */
    private HttpResponse makeContent(String urlString, HttpURLConnection urlConnection) throws IOException {
        HttpResponse httpResponse = new HttpResponse();
        try {
            String ecod = urlConnection.getContentEncoding();
            if (ecod == null) {
                ecod = this.defaultContentEncoding;
            }
            InputStream in = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(in, ecod));
            httpResponse.contentCollection = new Vector<>();
            StringBuffer temp = new StringBuffer();
            String line = bufferedReader.readLine();
            while (line != null) {
                httpResponse.contentCollection.add(line);
                temp.append(line).append("\r\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

            httpResponse.urlString = urlString;

            httpResponse.defaultPort = urlConnection.getURL().getDefaultPort();
            httpResponse.file = urlConnection.getURL().getFile();
            httpResponse.host = urlConnection.getURL().getHost();
            httpResponse.path = urlConnection.getURL().getPath();
            httpResponse.port = urlConnection.getURL().getPort();
            httpResponse.protocol = urlConnection.getURL().getProtocol();
            httpResponse.query = urlConnection.getURL().getQuery();
            httpResponse.ref = urlConnection.getURL().getRef();
            httpResponse.userInfo = urlConnection.getURL().getUserInfo();

            httpResponse.content = new String(temp.toString());
            httpResponse.contentEncoding = ecod;
            httpResponse.code = urlConnection.getResponseCode();
            httpResponse.message = urlConnection.getResponseMessage();
            httpResponse.contentType = urlConnection.getContentType();
            httpResponse.method = urlConnection.getRequestMethod();
            httpResponse.connectTimeout = urlConnection.getConnectTimeout();
            httpResponse.readTimeout = urlConnection.getReadTimeout();

            return httpResponse;
        } catch (IOException e) {
            throw e;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    /**
     * 默认的响应字符集
     */
    public String getDefaultContentEncoding() {
        return this.defaultContentEncoding;
    }

    /**
     * 设置默认的响应字符集
     *
     * @param defaultContentEncoding
     */
    public void setDefaultContentEncoding(String defaultContentEncoding) {
        this.defaultContentEncoding = defaultContentEncoding;
    }

    /**
     * 拼接GET请求参数
     *
     * @param parameters 参数集合
     * @return
     */
    private StringBuffer getParamStrBuffer(Map<String, String> parameters) {
        StringBuffer param = new StringBuffer();
        int i = 0;
        if (null != parameters) {
            for (String key : parameters.keySet()) {
                if (i == 0) {
                    param.append("?");
                } else {
                    param.append("&");
                }
                param.append(key).append("=").append(parameters.get(key));
                i++;
            }
        }

        return param;
    }
}
