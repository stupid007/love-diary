

// key常量，统一放置避免各个文件散写硬编码
const TOKEN_KEY = 'love-diary:token'
const USER_KEY = 'love-diary:user'

//取token:没有就返回空字符串
export function getToken(): string {
return localStorage.getItem(TOKEN_KEY) ?? ''
}

//登录成功后调用
export function setToken(token:string): void {
    localStorage.setItem(TOKEN_KEY,token)
}

//退出登陆时调用
export function removeToken(): void {
    localStorage.removeItem(TOKEN_KEY)
}

//用户信息
//用泛型<T>,存进去什么类型，取出来就是什么类型,调用方自己制定
export function setUserInfo<T>(user:T):void {
    localStorage.setItem(USER_KEY,JSON.stringify(user))
}

//获取用户信息
export function getUserInfo<T>(): T | null {
    const raw = localStorage.getItem(USER_KEY)
    if (!raw) return null
    try {
        return JSON.parse(raw) as T
    } catch {
        removeUserInfo()
        return null
    }
}

//删除用户信息
export function removeUserInfo() : void {
    localStorage.removeItem(USER_KEY)
}

// 全部清理
export function clearAuthStorage(): void {
    removeToken()
    removeUserInfo()
}