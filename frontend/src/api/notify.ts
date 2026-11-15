// 引入请求函数
import { request } from '@/utils/request'

// 单条通知，对应后端 NotificationVO
export interface NotificationVO {
    id: number
    // 类型字符串：COUPLE_APPLY / COUPLE_CONFIRM / COUPLE_DISSOLVE
    type: string
    title: string
    content: string
    refId: number | null
    isRead: boolean
    createTime: string
}
// 列表+未读，对应后端 NotifyListVO
export interface NotifyListVO {
    list: NotificationVO[]
    unread: number
}

// 拉取通知列表与未读数
export function listNotifyApi() {
    return request<NotifyListVO>({ url: '/notify/list', method: 'get' })
}
// 单条已读
export function readNotifyApi(id: number) {
    return request<void>({ url: '/notify/read', method: 'post', params: { id } })
}
// 全部已读
export function readAllNotifyApi() {
    return request<void>({ url: '/notify/read-all', method: 'post' })
}
