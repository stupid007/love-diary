import axios  from "axios"
// AxiosRequestConfig；请求配置的类型(URL,method,data,params)都在里面
import type {AxiosRequestConfig} from 'axios'

import {getToken} from '@/utils/storage'

// 后端统一返回数据的格式
export interface ApiResult<T> {
    code :number
    message : string
    data : T
}

//和后端success对齐，只有它才算请求成功
const SUCCESS_CODE = 200

//创建axios实例，不污染全局axios
const instance = axios.create({
     // 前缀，配合第 2 步的代理：/api/auth/login → http://127.0.0.1:8090/auth/login
    baseURL :'/api',
    timeout : 10000,
})

// 请求拦截器，发送请求前统一加工
instance.interceptors.request.use(
    config => {
        const token = getToken()
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }
        //必须return回去不然请求发不出去
        return config
    }
)


//响应拦截器，收到后端响应后统一拆包
instance.interceptors.response.use (response  => {
    const res =response.data as ApiResult<unknown>
    if(res.code !== SUCCESS_CODE) {
        return Promise.reject(new Error(res.message || '请求失败'))
    }
    // 关键：直接返回内层data
    return res.data as any
})
//对外暴露这一个函数
//泛型T是你期待返回的数据类型，比如request<LoginResult>({...})
export function request<T>(config:AxiosRequestConfig): Promise<T> {
    //第二个泛型制定返回值的类型，配合上面的拦截器，拿到的就是剥好的data
    return instance.request<unknown,T>(config) as unknown as Promise<T>
}