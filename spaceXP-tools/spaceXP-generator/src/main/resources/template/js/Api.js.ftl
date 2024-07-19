/**
* <p/>
* {@code @description}  : ${tableComment}
* <p/>
* <b>@create:</b> ${openingTime}
* <b>@email:</b> ${email}
*
* @author   ${author}
* @version   ${version}
*/
import service from '@/utils/request'

/**
* 根据id查询
*/
export const use${functionName?cap_first}InfoApi = id => {
return service.get('/${functionName}/info/' + id)
}
/**
* 当主键Id存在是修改信息 不存在表示新增功能
*/
export const use${functionName?cap_first}SubmitApi = dataForm => {
if (dataForm.id) {
return service.put('/${functionName}/update', dataForm)
} else {
return service.post('/${functionName}/add', dataForm)
}
}
/**
* 分页查询
* @param pageInfo 分页信息 page多少页 size页大小
* @param queryForm 查询条件
*/
export const use${functionName?cap_first}ListApi = (pageInfo, queryForm) => {
return service.post('/${functionName}/list', queryForm, {
params: pageInfo,
})
}
/**
* 根据id删除信息
*/
export const use${functionName?cap_first}DeleteApi = id => {
return service.delete('/${functionName}/delete' + id)
}
/**
* 批量删除信息
* @param ids id 数组
*/
export const use${functionName?cap_first}BatchDeleteApi = ids => {
return service.delete('/${functionName}/delete', ids)
}