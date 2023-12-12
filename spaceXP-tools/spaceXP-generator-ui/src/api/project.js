import service from '@/utils/request'

export const useProjectApi = id => {
    return service.get('/gen/project/' + id)
}

export const useProjectSubmitApi = dataForm => {
    if (dataForm.id) {
        return service.put('/gen/project', dataForm)
    } else {
        return service.post('/gen/project', dataForm)
    }
}

// 源码下载
export const useSourceDownloadApi = id => {
    location.href = import.meta.env.VITE_API_URL + '/gen/project/download/' + id
}