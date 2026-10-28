package com.chaohu.lovediarymodel.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {

    /** 主键。数据库是 BIGINT，所以 Java 侧要用 Long，不要用 Integer */
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    /** 存 BCrypt 密文，永远不要存明文 */
    private String password;

    /** 对应数据库列 nick_name，靠 map-underscore-to-camel-case 自动映射 */
    private String nickName;

    /**
     * 逻辑删除标记，对应数据库列 deleted。
     *
     * @TableLogic 的作用：调用 deleteById() 时，MP 不会真的 DELETE，
     * 而是生成 UPDATE user SET deleted=1 WHERE id=?；
     * 所有查询会自动追加 WHERE deleted=0。
     *
     * ⚠️ 字段名不能叫 delete —— delete 是 MySQL 保留字，
     * 生成的 SQL（WHERE delete=0）会语法报错。
     *
     * ⚠️ 数据库该列必须是 NOT NULL DEFAULT 0：这个字段为 null 时，
     * MP 会把它从 INSERT 里省掉，若列又没有默认值就会报
     * "Field 'deleted' doesn't have a default value"。
     */
    @TableLogic
    private Integer deleted;

    /** 数据库有 DEFAULT CURRENT_TIMESTAMP，插入时留 null 即可 */
    private LocalDateTime createTime;

    /** 数据库有 ON UPDATE CURRENT_TIMESTAMP，更新时留 null 即可 */
    private LocalDateTime updateTime;

    // 情侣关系ID，未绑定时才有值，绑定成功后清空
    private Long coupleId;

    // 邀请码
    private String inviteCode;

    // 是否曾经绑定过;
    private Integer everBound;
}
