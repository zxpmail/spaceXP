import service from '@/utils/request'

export const useDataSourceTestApi = (data) => {
	return service.post('/datasource/test' , data)
}

export const useDataSourceApi = (id) => {
	return service.get('/datasource/info/' + id)
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
