// 定义包路径：模型层 VO 包
package com.chaohu.lovediarymodel.vo;
// 导入 Lombok Data
import lombok.Data;
// 导入 BigDecimal，经纬度用高精度
import java.math.BigDecimal;
// GIS 地图点位读模型，由 /couple/map-points 返回
@Data
public class MapPointVO {
    // 用户 ID
    private Long userId;
    // 昵称，取 user.nickName 实时值
    private String name;
    // 是否当前登录用户
    private Boolean isMe;
    // 头像地址，优先 couple_profile，再回退 user.avatarUrl
    private String avatar;
    // 城市，取 user.city
    private String city;
    // 完整位置名称，取 user.locationName
    private String locationName;
    // 纬度
    private BigDecimal lat;
    // 经度
    private BigDecimal lng;
    // 卡片上的一句话，取本人签名，为空则取宣言
    private String snippet;
    // 点位颜色：A 青蓝，B 粉珊瑚
    private String color;
    // 在一起天数，从 couple.anniversary 算
    private Integer daysTogether;
    // 是否已设置位置，未设置时前端不画连线
    private Boolean hasLocation;
}
