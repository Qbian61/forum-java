import axios from 'axios'
import iView from 'iview'
// import Vue from 'vue'

// axios默认配置
axios.defaults.timeout = 100000 // 超时时间
axios.defaults.baseURL = '' // 默认为当前访问地址，部署到服务器时使用该地址
if (location.href.includes('127.0.0.1') || location.href.includes('localhost')) {
  axios.defaults.baseURL = 'http://localhost:8080' // 本地 forum-java 应用服务地址
}

// 整理数据
// axios.defaults.transformRequest = function (data) {
//   // data = Qs.stringify(data);
//   data = JSON.stringify(data)
//   return data
// }

// axios.defaults.headers['Content-Type'] = 'application/json;charset=UTF-8'
// axios.defaults.headers['token'] = localStorage.getItem('token')

// 路由请求拦截
// http request 拦截器
axios.interceptors.request.use(
  config => {
    if (config.headers['Content-Type'] !== 'multipart/form-data') {
      config.headers['Content-Type'] = 'application/json;charset=UTF-8'
    }
    // config.data = JSON.stringify(config.data) '19a9390e08b44b49926003e3bc866950'
    config.headers.token = localStorage.getItem('token')
    return config
  },
  error => {
    return Promise.reject(error.response)
  }
)

// 路由响应拦截
// http response 拦截器
axios.interceptors.response.use(
  response => {
    // console.info('response info ===>', response) 80008998
    if (response.data.code === 80008998) {
      iView.Message.error(response.data.message)
      window.location.href = window.location.origin + '/?toast=' + response.data.message
    }
    return response.data
  },
  error => {
    return Promise.reject(error.response) // 返回接口返回的错误信息
  }
)

export default axios
