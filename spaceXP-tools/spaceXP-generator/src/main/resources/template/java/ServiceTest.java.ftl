package ${package}.${moduleName}.service;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.framework.test.core.AutoSetPojo;

import ${package}.${moduleName}.model.dto.${className?cap_first}DTO;
import ${package}.${moduleName}.model.query.${className?cap_first}Query;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
* <p/>
* {@code @description}  : ${tableComment}ServiceTest 单元测试类
* <p/>
* <b>@create:</b> ${openingTime}
* <b>@email:</b> ${email}
*
* @author    ${author}
* @version   ${version}
*/
@SpringBootTest
public class ${className?cap_first}ServiceTest {
    @Resource
    private ${className?cap_first}Service ${className}Service;

    @Test
    public void list(){
        PageResult list = ${className}Service.list(new PageBean(), new ${className?cap_first}Query());
        assertNotNull(list);
    }

    @Test
    public void save(){
        ${className?cap_first}DTO ${className}DTO = AutoSetPojo.randomPojo(${className?cap_first}DTO.class);
        Boolean save = ${className}Service.save(${className}DTO);
        assertEquals(save,true);
    }

    @Test
    public void delete(){
        ${className?cap_first}DTO ${className}DTO = AutoSetPojo.randomPojo(${className?cap_first}DTO.class);
        ${className}Service.save(${className}DTO);
        Long id = ${className}DTO.getId();
        Boolean delete = ${className}Service.delete(id);
        assertEquals(delete,true);
    }
}