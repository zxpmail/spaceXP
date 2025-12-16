<#assign dbTime = "now()">
<#if dbType=="SQLServer">
    <#assign dbTime = "getDate()">
</#if>

-- 初始化菜单,由于菜单数据变化较大，生成仅供参考
INSERT INTO sys_menu (id,pid,ancestors,name,permission,type,sn,url) VALUES (,,,'${tableComment!}', null , 2, 1,null);
INSERT INTO sys_menu (id,pid,ancestors,name,permission,type,sn,url) VALUES (,,,'新增','${moduleName}:${functionName}:add', 4, 1,'/${moduleName}/${functionName}/add');
INSERT INTO sys_menu (id,pid,ancestors,name,permission,type,sn,url) VALUES (,,,'修改','${moduleName}:${functionName}:update', 4, 1, '/${moduleName}/${functionName}/update');
INSERT INTO sys_menu (id,pid,ancestors,name,permission,type,sn,url) VALUES (,,,'删除','${moduleName}:${functionName}:delete', 4, 1,'/${moduleName}/${functionName}/delete/**');
INSERT INTO sys_menu (id,pid,ancestors,name,permission,type,sn,url) VALUES (,,,'分页查询','${moduleName}:${functionName}:list', 4, 1,'/${moduleName}/${functionName}/list');
INSERT INTO sys_menu (id,pid,ancestors,name,permission,type,sn,url) VALUES (,,,'详情','${moduleName}:${functionName}:info', 4, 1, '/${moduleName}/${functionName}/info/**');
