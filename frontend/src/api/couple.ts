// 引入已剥包的请求函数
import { request } from '@/utils/request'

// 邀请码返回体，对应后端 InviteVO
export interface InviteVO {
    // 生成的邀请码
    code: string
}
// 绑定请求体，对应后端 BindDTO
export interface BindParams {
    // 对方发来的邀请码
    code: string
}
// 关系实体，字段与后端 CoupleVO 一一对应
export interface CoupleVO {
    id: number
    userAId: number
    userBId: number
    status: number
    bindTime: string
    anniversary: string
    nicknameA: string
    nicknameB: string
}
// 四态返回，对应后端 CoupleStateVO
export interface CoupleStateVO {
    // 是否曾绑定（一生一次锁）
    everBound: boolean
    // 当前关系，未绑定时为 null
    couple: CoupleVO | null
}

// 邀请方生成邀请码
export function inviteApi() {
    return request<InviteVO>({ url: '/couple/invite', method: 'post' })
}
// 申请方提交邀请码
export function bindApi(data: BindParams) {
    return request<void>({ url: '/couple/bind', method: 'post', data })
}
// 邀请方确认（coupleId 走 query）
export function confirmApi(coupleId: number) {
    return request<void>({ url: '/couple/confirm', method: 'post', params: { coupleId } })
}
// 查询四态，前端据此切视图
export function getStateApi() {
    return request<CoupleStateVO>({ url: '/couple/state', method: 'get' })
}
// 解绑
export function unbindApi() {
    return request<void>({ url: '/couple/unbind', method: 'post' })
}
