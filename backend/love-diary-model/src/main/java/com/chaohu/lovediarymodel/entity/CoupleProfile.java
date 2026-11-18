// 定义包路径：模型层实体包
package com.chaohu.lovediarymodel.entity;
// 导入 MyBatis-Plus 主键策略枚举
import com.baomidou.mybatisplus.annotation.IdType;
// 导入主键注解
import com.baomidou.mybatisplus.annotation.TableId;
// 导入逻辑删除注解
import com.baomidou.mybatisplus.annotation.TableLogic;
// 导入表名注解
import com.baomidou.mybatisplus.annotation.TableName;
// 导入 Lombok Data，自动生成 getter/setter
import lombok.Data;
// 导入 LocalDate 类型
import java.time.LocalDate;
// 导入 LocalDateTime 类型
import java.time.LocalDateTime;

// 情侣资料实体，对应 couple_profile 表
@Data
// 指定数据库表名
@TableName("couple_profile")
public class CoupleProfile {
    // 主键，数据库自增
    @TableId(type = IdType.AUTO)
    // 主键字段
    private Long id;
    // 关系 ID，对应 couple.id
    private Long coupleId;
    // 情侣空间标题
    private String title;
    // 恋爱宣言，地图卡片兜底文案
    private String motto;
    // 封面图 URL
    private String coverUrl;
    // 主题色
    private String themeColor;
    // A 的个性签名
    private String signatureA;
    // B 的个性签名
    private String signatureB;
    // A 的头像 URL
    private String avatarAUrl;
    // B 的头像 URL
    private String avatarBUrl;
    // A 的生日
    private LocalDate birthdayA;
    // B 的生日
    private LocalDate birthdayB;
    // 逻辑删除标记
    @TableLogic
    // 逻辑删除字段
    private Integer deleted;
    // 创建时间，数据库默认值
    private LocalDateTime createTime;
    // 更新时间，数据库 ON UPDATE
    private LocalDateTime updateTime;
}
