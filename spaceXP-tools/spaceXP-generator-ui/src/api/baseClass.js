import service from '@/utils/request'

export const useBaseClassApi = id => {
    return service.get('/gen/baseclass/' + id)
}

export const useBaseClassListApi = () => {
    return service.get('/gen/baseclass/list')
}

export const useBaseClassSubmitApi = (dataForm) => {
    if (dataForm.id) {
        return service.put('/gen/baseclass', dataForm)
    } else {
        return service.post('/gen/baseclass', dataForm)
    }
}