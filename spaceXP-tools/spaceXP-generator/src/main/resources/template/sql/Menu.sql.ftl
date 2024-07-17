<#assign dbTime = "now()">
<#if dbType=="SQLServer">
    <#assign dbTime = "getDate()">
</#if>

-- 初始化菜单,由于菜单数据变化较大，生成仅供参考
INSERT INTO sys_menu (id,pid,ancestors,name,permission,type,sn,path,icon,component,status,url,visible,deleted,manual,creator,create_time,updater,update_time) VALUES (,,,'${tableComment!}','', 3, 1, '${functionName}', '', NULL, 1,'', 1, 0, 1, 0, ${dbTime}, 0, ${dbTime}, '', '', 0, 0);

INSERT INTO sys_menu (id,pid,ancestors,name,permission,type,sn,path,icon,component,status,url,visible,deleted,manual,creator,create_time,updater,update_time) VALUES (,,,'新增','${moduleName}:${functionName}:add', 4, 1, '', '', NULL, 1,'/${moduleName}/${functionName}/add', 1, 0, 1, 0, ${dbTime}, 0, ${dbTime}, '', '', 0, 0);
INSERT INTO sys_menu (id,pid,ancestors,name,permission,type,sn,path,icon,component,status,url,visible,deleted,manual,creator,create_time,updater,update_time) VALUES (,,,'修改','${moduleName}:${functionName}:update', 4, 2, '', '', NULL, 1,'/${moduleName}/${functionName}/update', 1, 0, 1, 0, ${dbTime}, 0, ${dbTime}, '', '', 0, 0);
INSERT INTO sys_menu (id,pid,ancestors,name,permission,type,sn,path,icon,component,status,url,visible,deleted,manual,creator,create_time,updater,update_time) VALUES (,,,'删除','${moduleName}:${functionName}:delete', 4, 3, '', '', NULL, 1,'/${moduleName}/${functionName}/delete/**', 1, 0, 1, 0, ${dbTime}, 0, ${dbTime}, '', '', 0, 0);
INSERT INTO sys_menu (id,pid,ancestors,name,permission,type,sn,path,icon,component,status,url,visible,deleted,manual,creator,create_time,updater,update_time) VALUES (,,,'分页查询','${moduleName}:${functionName}:list', 4, 4, '', '', NULL, 1,'/${moduleName}/${functionName}/list', 1, 0, 1, 0, ${dbTime}, 0, ${dbTime}, '', '', 0, 0);
INSERT INTO sys_menu (id,pid,ancestors,name,permission,type,sn,path,icon,component,status,url,visible,deleted,manual,creator,create_time,updater,update_time) VALUES (,,,'详情','${moduleName}:${functionName}:info', 4, 5, '', '', NULL, 1,'/${moduleName}/${functionName}/info/**', 1, 0, 1, 0, ${dbTime}, 0, ${dbTime}, '', '', 0, 0);
