import service from '@/utils/request'
export const useTableApi = id => {
    return service.get('/gen/table/' + id)
}

export const useTableSubmitApi = dataForm => {
    return service.put('/gen/table', dataForm)
}

export const useTableImportSubmitApi = (datasourceId, tableNameList) => {
    return service.post('/gen/table/import/' + datasourceId, tableNameList)
}

export const useTableFieldSubmitApi = (tableId, fieldList) => {
    return service.put('/gen/table/field/' + tableId, fieldList)
}

export const useTableSyncApi = id => {
    return service.post('/gen/table/sync/' + id)
}
