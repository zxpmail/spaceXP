import service from '@/utils/request'



export const useDatabaseApi = id => {
	return service.get('/database/info/' + id)
}

export const useDatabaseListApi = () => {
	return service.get('/database/list')
}

export const useDatabaseSubmitApi = (dataForm) => {
	if (dataForm.id) {
		return service.put('/database/update', dataForm)
	} else {
		return service.post('/database/add', dataForm)
	}
}

