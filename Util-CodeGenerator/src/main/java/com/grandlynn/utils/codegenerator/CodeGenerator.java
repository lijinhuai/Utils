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

import com.google.common.base.CaseFormat;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 代码生成器，根据数据表名称生成对应的Model、CommonMapper、Service、Controller简化开发。
 *
 * @author jinhuailee
 * @date 2017/10/25 11:37
 */
public class CodeGenerator {
    /**
     * JDBC配置，请修改为你项目的实际配置
     */
    private static String JDBC_URL;
    private static String JDBC_USERNAME;
    private static String JDBC_PASSWORD;
    private static String JDBC_DIVER_CLASS_NAME;

    /**
     * 项目在硬盘上的基础路径
     */
    private static String PROJECT_PATH = System.getProperty("user.dir");

    private static final String DIR = System.getProperty("user.dir");
    /**
     * 模板位置
     */
    private static final String TEMPLATE_PATH = "/template";
    /**
     * java文件路径
     */
    private static final String JAVA_PATH = "/src/main/java";
    /**
     * 资源文件路径
     */
    private static final String RESOURCES_PATH = "/src/main/resources";


    /**
     * 项目基础包名称，根据自己公司的项目修改
     */
    private static String BASE_PACKAGE;

    /**
     * 生成的Controller存放路径
     */
    private static String PACKAGE_PATH_CONTROLLER;
    /**
     * 生成的Service存放路径
     */
    private static String PACKAGE_PATH_SERVICE;
    /**
     * 生成的Service实现存放路径
     */
    private static String PACKAGE_PATH_SERVICE_IMPL;

    /**
     * Mapper所在包
     */
    private static String MAPPER_PACKAGE;
    /**
     * Mapper插件基础接口的完全限定名
     */
    private static final String MAPPER_INTERFACE_REFERENCE = "com.grandlynn.bootframe.dao.common.CommonMapper";
    /**
     * Model所在包
     */
    private static String MODEL_PACKAGE;

    private static String BEAN_PACKAGE;


    /**
     * application web service dao model
     * 子项目的名称
     */
    private static String PROJECT_APPLICATION_PATH;
    private static String PROJECT_WEB_PATH;
    private static String PROJECT_SERVICE_PATH;
    private static String PROJECT_DAO_PATH;
    private static String PROJECT_MODEL_PATH;


    /**
     * @author
     */
    private static String AUTHOR;

    /**
     * @date
     */
    private static final String DATE = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date());


    public static void initCodeGenerator(String projectPath, String beanPackage, String application, String web, String service, String dao, String model) {
        PROJECT_PATH = "".equals(projectPath) ? PROJECT_PATH : projectPath;
        PROJECT_APPLICATION_PATH = PROJECT_PATH + "/" + application;
        PROJECT_WEB_PATH = PROJECT_PATH + "/" + web;
        PROJECT_SERVICE_PATH = PROJECT_PATH + "/" + service;
        PROJECT_DAO_PATH = PROJECT_PATH + "/" + dao;
        PROJECT_MODEL_PATH = PROJECT_PATH + "/" + model;
        Properties properties = new Properties();
        try {
            InputStream in = Object.class.getResourceAsStream("/codeGenerator.properties");
            properties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JDBC_URL = properties.getProperty("JDBC_URL");
        JDBC_USERNAME = properties.getProperty("JDBC_USERNAME");
        JDBC_PASSWORD = properties.getProperty("JDBC_PASSWORD");
        JDBC_DIVER_CLASS_NAME = properties.getProperty("JDBC_DIVER_CLASS_NAME");


        BASE_PACKAGE = properties.getProperty("BASE_PACKAGE");

        if ("".equals(beanPackage)) {
            BEAN_PACKAGE = JDBC_USERNAME;
        } else {
            BEAN_PACKAGE = beanPackage;
        }


        PACKAGE_PATH_CONTROLLER = packageConvertPath(properties.getProperty("CONTROLLER_PACKAGE") + "." + BEAN_PACKAGE);

        PACKAGE_PATH_SERVICE = packageConvertPath(properties.getProperty("SERVICE_PACKAGE") + "." + BEAN_PACKAGE);
        PACKAGE_PATH_SERVICE_IMPL = packageConvertPath(properties.getProperty("SERVICE_PACKAGE") + "." + BEAN_PACKAGE + ".impl");

        MAPPER_PACKAGE = properties.getProperty("MAPPER_PACKAGE");

        MODEL_PACKAGE = properties.getProperty("MODEL_PACKAGE");

        AUTHOR = properties.getProperty("AUTHOR");

    }

    /**
     * 通过数据表名称生成代码，Model 名称通过解析数据表名称获得，下划线转大驼峰的形式。
     * 如输入表名称 "t_user_detail" 将生成 TUserDetail、TUserDetailMapper、TUserDetailService ...
     *
     * @param scheam     数据表用户名
     * @param tableNames 数据表名称...
     */
    public static void genCode(String scheam, String... tableNames) {
        if ("".equals(scheam)) {
            scheam = JDBC_USERNAME;
        }
        for (String tableName : tableNames) {
            genCodeByCustomModelName(scheam, tableName, null);
        }
    }


    /**
     * 通过数据表名称，和自定义的 Model 名称生成代码
     * 如输入表名称 "t_user_detail" 和自定义的 Model 名称 "User" 将生成 User、UserMapper、UserService ...
     *
     * @param scheam    数据表用户名
     * @param tableName 数据表名称
     * @param modelName 自定义的 Model 名称
     */
    public static void genCodeByCustomModelName(String scheam, String tableName, String modelName) {
        genModelAndMapper(scheam, tableName, modelName);
        genService(tableName, modelName);
        genRestflController(tableName, modelName);

    }

    public static void genModelAndMapper(String scheam, String tableName, String modelName) {
        Context context = new Context(ModelType.FLAT);
        context.setId("DB2Tables");
        context.setTargetRuntime("MyBatis3Simple");
        context.addProperty(PropertyRegistry.CONTEXT_BEGINNING_DELIMITER, "`");
        context.addProperty(PropertyRegistry.CONTEXT_ENDING_DELIMITER, "`");

        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        jdbcConnectionConfiguration.setConnectionURL(JDBC_URL);
        jdbcConnectionConfiguration.setUserId(JDBC_USERNAME);
        jdbcConnectionConfiguration.setPassword(JDBC_PASSWORD);
        jdbcConnectionConfiguration.setDriverClass(JDBC_DIVER_CLASS_NAME);
        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);

        PluginConfiguration pluginConfiguration = new PluginConfiguration();
        pluginConfiguration.setConfigurationType("tk.mybatis.mapper.generator.MapperPlugin");
        pluginConfiguration.addProperty("mappers", MAPPER_INTERFACE_REFERENCE);
        context.addPluginConfiguration(pluginConfiguration);

        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
        javaModelGeneratorConfiguration.setTargetProject(PROJECT_MODEL_PATH + JAVA_PATH);
        javaModelGeneratorConfiguration.setTargetPackage(MODEL_PACKAGE);
        javaModelGeneratorConfiguration.addProperty("enableSubPackages", "true");
        javaModelGeneratorConfiguration.addProperty("trimStrings", "true");
        context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);

        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
        sqlMapGeneratorConfiguration.setTargetProject(PROJECT_DAO_PATH + RESOURCES_PATH);
        sqlMapGeneratorConfiguration.setTargetPackage("mybatis/mapper");
        sqlMapGeneratorConfiguration.addProperty("enableSubPackages", "true");
        context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);

        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
        javaClientGeneratorConfiguration.setTargetProject(PROJECT_DAO_PATH + JAVA_PATH);
        javaClientGeneratorConfiguration.setTargetPackage(MAPPER_PACKAGE);
//        type="ANNOTATEDMAPPER",生成Java Model 和基于注解的Mapper对象
//        type="MIXEDMAPPER",生成基于注解的Java Model 和相应的Mapper对象
//        type="XMLMAPPER",生成SQLMap XML文件和独立的Mapper接口
        javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
        javaClientGeneratorConfiguration.addProperty("enableSubPackages", "true");
        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);

        TableConfiguration tableConfiguration = new TableConfiguration(context);
        tableConfiguration.setTableName(tableName);
        if (StringUtils.isNotEmpty(modelName)) {
            tableConfiguration.setDomainObjectName(modelName);
        }
        if (StringUtils.isNotEmpty(scheam)) {
            tableConfiguration.setSchema(scheam);
        }
        /**
         * Mysql    参数1="id"，参数2="Mysql"，参数3=true，参数4=null
         * Oracle   参数1="id"，参数2="select SEQ_{1}.nextval from dual"，参数3=false，参数4="pre"
         */
        tableConfiguration.setGeneratedKey(new GeneratedKey("id", "select SEQ_{1}.nextval from dual", false, "pre"));
        context.addTableConfiguration(tableConfiguration);

        List<String> warnings;
        MyBatisGenerator generator;
        try {
            Configuration config = new Configuration();
            config.addContext(context);
            config.validate();

            boolean overwrite = true;
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            warnings = new ArrayList<>();
            generator = new MyBatisGenerator(config, callback, warnings);
            generator.generate(null);
        } catch (Exception e) {
            throw new RuntimeException("生成Model和Mapper失败", e);
        }

        if (generator.getGeneratedJavaFiles().isEmpty() || generator.getGeneratedXmlFiles().isEmpty()) {
            throw new RuntimeException("生成Model和Mapper失败：" + warnings);
        }
        if (StringUtils.isEmpty(modelName)) {
            modelName = tableNameConvertUpperCamel(tableName);
        }
        System.out.println(modelName + ".java 生成成功");
        System.out.println(modelName + "CommonMapper.java 生成成功");
        System.out.println(modelName + "CommonMapper.xml 生成成功");
    }

    public static void genService(String tableName, String modelName) {
        try {
            freemarker.template.Configuration cfg = getConfiguration();

            Map<String, Object> data = new HashMap<>(6);
            data.put("date", DATE);
            data.put("author", AUTHOR);
            String modelNameUpperCamel = StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName;
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", tableNameConvertLowerCamel(tableName));
            data.put("basePackage", BASE_PACKAGE);
            data.put("beanPackage", BEAN_PACKAGE);

            File file = new File(PROJECT_SERVICE_PATH + JAVA_PATH + PACKAGE_PATH_SERVICE + modelNameUpperCamel + "Service.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("service.ftl").process(data,
                new FileWriter(file));
            System.out.println(modelNameUpperCamel + "Service.java 生成成功");

            File file1 = new File(PROJECT_SERVICE_PATH + JAVA_PATH + PACKAGE_PATH_SERVICE_IMPL + modelNameUpperCamel + "ServiceImpl.java");
            if (!file1.getParentFile().exists()) {
                file1.getParentFile().mkdirs();
            }
            cfg.getTemplate("service-impl.ftl").process(data,
                new FileWriter(file1));
            System.out.println(modelNameUpperCamel + "ServiceImpl.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Service失败", e);
        }
    }

    public static void genController(String tableName, String modelName) {
        try {
            freemarker.template.Configuration cfg = getConfiguration();

            Map<String, Object> data = new HashMap<>(7);
            data.put("date", DATE);
            data.put("author", AUTHOR);
            String modelNameUpperCamel = StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName;
            data.put("baseRequestMapping", modelNameConvertMappingPath(modelNameUpperCamel));
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, modelNameUpperCamel));
            data.put("basePackage", BASE_PACKAGE);
            data.put("beanPackage", BEAN_PACKAGE);

            File file = new File(PROJECT_WEB_PATH + JAVA_PATH + PACKAGE_PATH_CONTROLLER + modelNameUpperCamel + "Controller.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("controller.ftl").process(data, new FileWriter(file));

            System.out.println(modelNameUpperCamel + "Controller.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Controller失败", e);
        }

    }

    public static void genRestflController(String tableName, String modelName) {
        try {
            freemarker.template.Configuration cfg = getConfiguration();

            Map<String, Object> data = new HashMap<>(7);
            data.put("date", DATE);
            data.put("author", AUTHOR);
            String modelNameUpperCamel = StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName;
            data.put("baseRequestMapping", modelNameConvertMappingPath(modelNameUpperCamel));
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, modelNameUpperCamel));
            data.put("basePackage", BASE_PACKAGE);
            data.put("beanPackage", BEAN_PACKAGE);

            File file = new File(PROJECT_WEB_PATH + JAVA_PATH + PACKAGE_PATH_CONTROLLER + modelNameUpperCamel + "Controller.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("controller-restful.ftl").process(data, new FileWriter(file));

            System.out.println(modelNameUpperCamel + "Controller.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Controller失败", e);
        }

    }

    private static freemarker.template.Configuration getConfiguration() throws IOException {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
        cfg.setClassForTemplateLoading(CodeGenerator.class, TEMPLATE_PATH);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return cfg;
    }

    private static String tableNameConvertLowerCamel(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName.toLowerCase());
    }

    private static String tableNameConvertUpperCamel(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName.toLowerCase());

    }

    private static String tableNameConvertMappingPath(String tableName) {
        //兼容使用大写的表名
        tableName = tableName.toLowerCase();
        return "/" + (tableName.contains("_") ? tableName.replaceAll("_", "/") : tableName);
    }

    private static String modelNameConvertMappingPath(String modelName) {
        String tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, modelName);
        return tableNameConvertMappingPath(tableName);
    }

    private static String packageConvertPath(String packageName) {
        return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
    }

}
