import service from '@/utils/request'
export const useTableApi = id => {
    return service.get('/table/info/' + id)
}

export const useTableSubmitApi = dataForm => {
    return service.put('/table/update', dataForm)
}

export const useTableImportSubmitApi = (tableList) => {
    return service.post('/table/add',  tableList)
}

export const useTableFieldSubmitApi = (fieldList) => {
    return service.put('/table/field/update', fieldList)
}

export const useTableSyncApi = table => {
    return service.post('/table/sync' , table)
}
