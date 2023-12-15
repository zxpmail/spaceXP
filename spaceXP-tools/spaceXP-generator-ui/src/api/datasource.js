import service from '@/utils/request'

export const useDataSourceTestApi = (id) => {
	return service.get('/datasource/test/' + id)
}

export const useDataSourceApi = (id) => {
	return service.get('/datasource/' + id)
}

export const useDataSourceListApi = () => {
	return service.post('/datasource/list')
}

export const useDataSourceSubmitApi = (dataForm) => {
	if (dataForm.id) {
		return service.put('/datasource/update', dataForm)
	} else {
		return service.post('/datasource/add', dataForm)
	}
}

export const useDataSourceTableListApi = (id) => {
	return service.get('/datasource/table/list/' + id)
}
