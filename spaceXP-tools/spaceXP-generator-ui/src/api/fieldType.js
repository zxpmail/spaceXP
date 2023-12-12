import service from '@/utils/request'

export const useFieldTypeApi = id => {
    return service.get('/gen/fieldtype/' + id)
}

export const useFieldTypeListApi = () => {
    return service.get('/gen/fieldtype/list')
}

export const useFieldTypeSubmitApi = dataForm => {
    if (dataForm.id) {
        return service.put('/gen/fieldtype', dataForm)
    } else {
        return service.post('/gen/fieldtype', dataForm)
    }
}
