// 定义包路径：模型层 DTO 包
package com.chaohu.lovediarymodel.dto;
// 导入 Lombok Data
import lombok.Data;
// 导入 LocalDate
import java.time.LocalDate;

// 情侣资料更新请求体；共享字段双方可改，A/B 私有字段由 Service 按身份校验
@Data
public class CoupleProfileUpdateDTO {
    // 恋爱纪念日，写回 couple.anniversary
    private LocalDate anniversary;
    // 情侣空间标题
    private String title;
    // 恋爱宣言
    private String motto;
    // 封面图 URL
    private String coverUrl;
    // 主题色
    private String themeColor;
    // A 的签名，仅 A 可改
    private String signatureA;
    // B 的签名，仅 B 可改
    private String signatureB;
    // A 的头像，仅 A 可改
    private String avatarAUrl;
    // B 的头像，仅 B 可改
    private String avatarBUrl;
    // A 的生日，仅 A 可改
    private LocalDate birthdayA;
    // B 的生日，仅 B 可改
    private LocalDate birthdayB;
}
