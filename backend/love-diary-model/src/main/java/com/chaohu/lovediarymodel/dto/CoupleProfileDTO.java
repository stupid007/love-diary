// 定义包路径：模型层 DTO 包
package com.chaohu.lovediarymodel.dto;
// 导入 Lombok Data
import lombok.Data;
// 导入 LocalDate，和 Couple 实体 anniversary 类型统一
import java.time.LocalDate;

// 情侣资料读模型：纪念日与昵称来自 couple，展示字段来自 couple_profile
@Data
public class CoupleProfileDTO {
    // 恋爱纪念日，唯一真相源是 couple.anniversary
    private LocalDate anniversary;
    // A 昵称快照
    private String nicknameA;
    // B 昵称快照
    private String nicknameB;
    // 情侣空间标题
    private String title;
    // 恋爱宣言
    private String motto;
    // 封面图 URL
    private String coverUrl;
    // 主题色
    private String themeColor;
    // A 的签名
    private String signatureA;
    // B 的签名
    private String signatureB;
    // A 的头像
    private String avatarAUrl;
    // B 的头像
    private String avatarBUrl;
    // A 的生日
    private LocalDate birthdayA;
    // B 的生日
    private LocalDate birthdayB;
}
