<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package}.${moduleName}.mapper.${className?cap_first}Mapper">
    <!-- 可根据自己的需求，是否要使用 -->
    <resultMap type="${package}.${moduleName}.model.entity.${className?cap_first}DO" id="${className}Map">
        <#list fieldList as field>
        <result property="${field.attrName}" column="${field.fieldName}"/>
        </#list>
    </resultMap>

</mapper>