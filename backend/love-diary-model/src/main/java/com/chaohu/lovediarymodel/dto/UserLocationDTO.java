// 定义包路径：模型层 DTO 包
package com.chaohu.lovediarymodel.dto;
// 导入 Lombok Data
import lombok.Data;
// 导入 BigDecimal，经纬度用高精度
import java.math.BigDecimal;

// 用户位置更新请求体，对应 PUT /user/location
@Data
public class UserLocationDTO {
    // 完整位置名称，如"北京市朝阳区"
    private String locationName;
    // 省份
    private String province;
    // 城市
    private String city;
    // 区县
    private String district;
    // 纬度
    private BigDecimal lat;
    // 经度
    private BigDecimal lng;
}
