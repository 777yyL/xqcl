import axios from 'axios'
import type { AxiosRequestConfig } from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: '/api',
  timeout: 30000
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    const { data } = response
    if (data.code !== 200) {
      ElMessage.error(data.message || '请求失败')
      return Promise.reject(new Error(data.message || '请求失败'))
    }
    return data
  },
  (error) => {
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export default request

// API 接口定义
export const reqApi = {
  // 分页查询需求列表
  pageQuery: (data: any) => {
    return request.post('/req/list/page', data)
  },

  // 查询需求详情
  getDetail: (reqNo: string) => {
    return request.get(`/req/detail/${reqNo}`)
  },

  // 导入需求列表 Excel
  importList: (file: File) => {
    const formData = new FormData()
    formData.append('file', file)
    return request.post('/req/import/list', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },

  // 导入需求详情 Excel
  importDetail: (file: File) => {
    const formData = new FormData()
    formData.append('file', file)
    return request.post('/req/import/detail', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },

  // 导出单个需求的 Markdown（返回文件路径）
  exportMarkdown: (reqNo: string) => {
    return request.get(`/req/export/markdown/${reqNo}`)
  },

  // 下载单个需求的 Markdown（直接下载文件）
  downloadMarkdown: (reqNo: string) => {
    // 使用原生 axios 发送请求，因为需要处理二进制数据
    return axios.get(`/api/req/download/markdown/${reqNo}`, {
      responseType: 'blob',
      timeout: 60000
    }).then(response => {
      // 创建下载链接
      const blob = new Blob([response.data], { type: 'text/markdown;charset=utf-8' })
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = `需求_${reqNo}.md`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
      return { success: true }
    }).catch(error => {
      ElMessage.error('下载失败: ' + error.message)
      return { success: false }
    })
  },

  // 批量导出 Markdown
  batchExportMarkdown: (reqNos: string[] | null) => {
    return request.post('/req/export/markdown/batch', { reqNos })
  }
}
