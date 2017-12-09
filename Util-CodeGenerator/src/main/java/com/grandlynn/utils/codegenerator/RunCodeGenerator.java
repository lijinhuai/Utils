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

package com.grandlynn.utils.codegenerator;

/**
 * @author jinhuailee
 * @date 2017/10/25 12:37
 */
public class RunCodeGenerator {
    public static void main(String[] args) {

        String projectName = "BootFrame";

        /**
         * 参数1： controller 和service生成的上级包名，不填则为默认连接数据库用户
         * 不填则为默认的数据库用户名
         */
        CodeGenerator.initCodeGenerator("","", projectName+"-application", projectName+"-web", projectName+"-service", projectName+"-dao", projectName+"-model");
        /**
         * 参数1：数据表用户名，不填则为默认连接数据库用户
         * 参数2："输入表名"，若%表示所有表
         */
        CodeGenerator.genCode("jj", "SHH_FW");
    }
}
