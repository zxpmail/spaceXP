import service from '@/utils/request'

export const useProjectApi = id => {
    return service.get('/project/info/' + id)
}

export const useProjectSubmitApi = dataForm => {
    if (dataForm.id) {
        return service.put('/project/update', dataForm)
    } else {
        return service.post('/project/add', dataForm)
    }
}

// 源码下载
export const useSourceDownloadApi = id => {
    location.href = import.meta.env.VITE_API_URL + '/gen/project/download/' + id
}