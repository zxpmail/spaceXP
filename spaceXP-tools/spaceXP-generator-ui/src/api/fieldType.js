import service from '@/utils/request'
import {ElMessage, ElMessageBox} from "element-plus";

export const useFieldTypeApi = id => {
    return service.get('/fieldType/info/' + id)
}
export const useFieldTypeIsListApi = row => {
    ElMessageBox.confirm('确定进行修改列表显示操作?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(() => {
        service.get('/fieldType/updateIsList/' + row.id).then(() => {
            ElMessage.success('修改成功')
        })
    })
        .catch(() => {
        })
}
export const useFieldTypeListApi = () => {
    return service.get('/fieldType/list')
}
export const useFieldTypeData = () => {
    return service.get('/fieldType/fieldType')
}

export const useFieldTypeSubmitApi = dataForm => {
    if (dataForm.id) {
        return service.put('/fieldType/update', dataForm)
    } else {
        return service.post('/fieldType/add', dataForm)
    }
}
