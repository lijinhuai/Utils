package ${basePackage}.service.${beanPackage}.impl;

import ${basePackage}.dao.mapper.${beanPackage}.${modelNameUpperCamel}Mapper;
import ${basePackage}.model.${beanPackage}.${modelNameUpperCamel};
import ${basePackage}.service.${beanPackage}.${modelNameUpperCamel}Service;
import ${basePackage}.service.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * @author ${author}
 * @date ${date}
 */
@Service
@Transactional
public class ${modelNameUpperCamel}ServiceImpl extends AbstractService<${modelNameUpperCamel}> implements ${modelNameUpperCamel}Service {

    @Resource
    private ${modelNameUpperCamel}Mapper ${modelNameLowerCamel}Mapper;

}
