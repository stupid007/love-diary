import { request } from "@/utils/request"

// 注册，请求参数类型，必须和后端DTO一摸一样
export interface RegisterParams {
    username:string
    password:string
    // 问号表示可传可不传
    nickName?:string
}
// 登录
export interface LoginParams {
    username : string,
    password : string
}
//响应数据类习惯
export interface LoginResult {
    token : string
    userId : number
    nickName : string
}

//注册接口
export function registerApi(data: RegisterParams) {
    return request<void>({
        url:'/auth/register',
        method: 'post',
        data, //data作为请求体json发给后端
    })
}
//登录接口
export function loginApi(data: LoginParams) {
    return request<LoginResult>({
        url:'/auth/login',
        method:'post',
        data
    })
}

// 取当前登录用户（校验token是否有效）
export function getCurrentUserApi() {
    return request<String>({
        url:'/auth/me',
        method:'get',
    })
}