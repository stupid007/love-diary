import { request } from "@/utils/request";

//保存、更新用户位置：PUT /api/user/location
export function updateLocationApi(data: {
    locationName: string,
    province: string,
    city: string,
    district: string,
    lat: number,
    lng: number
}) {
    return request<void>({
        url:'/api/user/location',
        method :'put',
        data
    })
}