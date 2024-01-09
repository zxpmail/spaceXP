import service from '@/utils/request'
export const useTableApi = id => {
    return service.get('/table/info/' + id)
}

export const useTableSubmitApi = dataForm => {
    return service.put('/table/update', dataForm)
}

export const useTableImportSubmitApi = (datasourceId, tableNameList) => {
    return service.post('/table/import/' + datasourceId, tableNameList)
}

export const useTableFieldSubmitApi = (tableId, fieldList) => {
    return service.put('/table/field/' + tableId, fieldList)
}

export const useTableSyncApi = table => {
    return service.post('/table/sync' , table)
}
