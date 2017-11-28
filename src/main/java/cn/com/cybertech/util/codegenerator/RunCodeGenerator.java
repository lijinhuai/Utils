/*
 *
 * Copyright 2001-2017 Suzhou CyberTech Technology Co., Ltd.
 *
 */

package cn.com.cybertech.util.codegenerator;

import cn.com.cybertech.util.codegenerator.CodeGenerator;

/**
 * @author jinhuailee
 * @date 2017/10/25 12:37
 */
public class RunCodeGenerator {
    public static void main(String[] args) {

        /**
         * 参数1： controller 和service生成的上级包名，不填则为默认连接数据库用户
         * 不填则为默认的数据库用户名
         */
        CodeGenerator.initCodeGenerator("", "BootFrame-application", "BootFrame-web", "BootFrame-service", "BootFrame-dao", "BootFrame-model");
        /**
         * 参数1：数据表用户名，不填则为默认连接数据库用户
         * 参数2："输入表名"，若%表示所有表
         */
        CodeGenerator.genCode("", "%");
    }
}
