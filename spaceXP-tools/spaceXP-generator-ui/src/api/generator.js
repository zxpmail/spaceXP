import service from '@/utils/request'

// 生成代码（zip压缩包）
export const useDownloadApi = tableIds => {
    if (!tableIds || tableIds.length === 0) {
        return
    }
    const config = {
        method: 'post',
        url: '/generator/code?piesat.zip',
        data:tableIds,
        responseType: 'blob'
    };
    service(config).then(response => {
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'piesat.zip');
        document.body.appendChild(link);
        link.click();

    })
}

// 生成代码（自定义目录）
export const useGeneratorApi = tableIds => {
    return service.post('/gen/generator/code', tableIds)
}
