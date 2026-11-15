package com.chaohu.lovediaryservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.chaohu.lovediarycommon.context.UserContext;
import com.chaohu.lovediarycommon.exception.BizException;
import com.chaohu.lovediarycommon.result.ResultCode;
import com.chaohu.lovediarymodel.dto.BindDTO;
import com.chaohu.lovediarymodel.dto.CoupleProfileDTO;
import com.chaohu.lovediarymodel.entity.Couple;
import com.chaohu.lovediarymodel.entity.User;
import com.chaohu.lovediarymodel.vo.CoupleStateVO;
import com.chaohu.lovediarymodel.vo.CoupleVO;
import com.chaohu.lovediarymodel.vo.InviteVO;
import com.chaohu.lovediaryrepository.mapper.CoupleMapper;
import com.chaohu.lovediaryrepository.mapper.UserMapper;
import com.chaohu.lovediaryservice.CoupleService;
import com.chaohu.lovediaryservice.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

@Service
// final 字段构造注入
@RequiredArgsConstructor
// 继承 MP 的 ServiceImpl 以获得 baseMapper（由 MP 自动注入）及 save/selectById 等
public class CoupleServiceImpl extends ServiceImpl<CoupleMapper, Couple> implements CoupleService {
    // 操作 user（邀请码/everBound/couple_id）
    private final UserMapper userMapper;
    private final NotificationService notificationService;
    // 关系状态：待确认
    private static final int STATUS_PENDING = 0;
    // 关系状态：已绑定
    private static final int STATUS_ACTIVE = 1;
    // 关系状态：已解除
    private static final int STATUS_DISSOLVED = 2;
    // 邀请码字符（去掉易混 0/O/1/I）
    private static final String CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    // 随机数
    private static final Random RANDOM = new Random();


    // 1、邀请方生成邀请码
    @Override
    public InviteVO invite() {
        // 获取当前登录用户
        User me = me();
        // 已有关系
        if(me.getCoupleId() !=null) {
            throw new BizException(ResultCode.COUPLE_EXIST);
        }
        // 曾经绑定过
        if(everBound(me)) {
            throw new BizException(ResultCode.EVER_BOUND_GONE);
        }
        String code = genCode();
        me.setInviteCode(code);
        userMapper.updateById(me);
        InviteVO vo = new InviteVO();
        vo.setCode(code);
        return vo;
    }

    // ===== 2. 申请方提交邀请码绑定 =====
    @Override
    public void bind(BindDTO dto) {
        // 申请方
        User me = me();
        // 已有关系
        if (me.getCoupleId() != null) {
            throw new BizException(ResultCode.COUPLE_EXIST);
        }
        // 机会已尽
        if (everBound(me)) {
            throw new BizException(ResultCode.EVER_BOUND_GONE);
        }
        // 按码找邀请方A
        User inviter = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getInviteCode, dto.getCode()));
        // 码不存在
        if (inviter == null) {
            throw new BizException(ResultCode.INVITE_CODE_INVALID);
        }
        // 自己绑自己
        if (inviter.getId().equals(me.getId())) {
            throw new BizException(ResultCode.CANNOT_BIND_SELF);
        }
        // 对方已绑定
        if (inviter.getCoupleId() != null) {
            throw new BizException(ResultCode.INVITE_CODE_USED);
        }
        // 新建关系
        Couple c = new Couple();
        // 邀请方
        c.setUserAId(inviter.getId());
        // 申请方
        c.setUserBId(me.getId());
        // 待确认
        c.setStatus(STATUS_PENDING);
        // 昵称快照
        c.setNicknameA(inviter.getNickName());
        // 昵称快照
        c.setNicknameB(me.getNickName());
        // 插关系
        this.baseMapper.insert(c);
        // A 指向关系
        inviter.setCoupleId(c.getId());
        // B 指向关系
        me.setCoupleId(c.getId());
        // 更新A
        userMapper.updateById(inviter);
        // 更新B
        userMapper.updateById(me);
        // 通知A有人申请
        notificationService.notify(
                inviter.getId(), "COUPLE_APPLY",
                "有人向你发起羁绊", me.getNickName() + " 想与你缔结契约", c.getId());
    }

    // ===== 3. 邀请方确认 =====
    @Override
    public void confirm(Long coupleId) {
        // 当前用户（应为A）
        User me = me();
        // 查关系
        Couple c = this.baseMapper.selectById(coupleId);
        // 不存在
        if (c == null) {
            throw new BizException(ResultCode.COUPLE_NOT_FOUND);
        }
        // 不是邀请方
        if (!me.getId().equals(c.getUserAId())) {
            throw new BizException(ResultCode.NOT_INVITER);
        }
        // 非待确认
        if (c.getStatus() != STATUS_PENDING) {
            throw new BizException(ResultCode.COUPLE_CONFIRMED);
        }
        // 置已绑定
        c.setStatus(STATUS_ACTIVE);
        // 绑定时间
        c.setBindTime(LocalDateTime.now());
        // 纪念日默认今天（后可改）
        c.setAnniversary(LocalDate.now());
        // 落库
        this.baseMapper.updateById(c);
        // 邀请方
        User a = userMapper.selectById(c.getUserAId());
        // 申请方
        User b = userMapper.selectById(c.getUserBId());
        // 双方 everBound=1（一生一次锁，永不回退）
        setEverBound(a);
        setEverBound(b);
        // 通知B已成
        notificationService.notify(
                b.getId(), "COUPLE_CONFIRM",
                "契约已成", "你们的故事，从此开始", c.getId());
    }

    // ===== 4. 查询四态 =====
    @Override
    public CoupleStateVO state() {
        // 当前用户
        User me = me();
        // 状态视图
        CoupleStateVO vo = new CoupleStateVO();
        // 是否曾绑定
        vo.setEverBound(everBound(me));
        // 有当前关系
        if (me.getCoupleId() != null) {
            Couple c = this.baseMapper.selectById(me.getCoupleId());
            if (c != null) {
                // 装进 VO
                vo.setCouple(toVO(c));
            }
        }
        // everBound=true 且 couple=null → 前端显示"机会已尽"哲学卡片
        return vo;
    }
    // ===== 5. 解绑 =====
    @Override
    public void unbind() {
        // 当前用户
        User me = me();
        // 无关系
        if (me.getCoupleId() == null) {
            throw new BizException(ResultCode.COUPLE_NOT_FOUND);
        }
        // 查关系
        Couple c = this.baseMapper.selectById(me.getCoupleId());
        if (c == null) {
            throw new BizException(ResultCode.COUPLE_NOT_FOUND);
        }
        // 置解除态
        c.setStatus(STATUS_DISSOLVED);
        // 保留记录，仅改状态
        this.baseMapper.updateById(c);
        // 邀请方
        User a = userMapper.selectById(c.getUserAId());
        // 申请方
        User b = userMapper.selectById(c.getUserBId());
        // 清 couple_id（everBound 不动）
        clearCouple(a);
        clearCouple(b);
        // 算对方
        Long otherId = me.getId().equals(a.getId()) ? b.getId() : a.getId();
        // 通知对方已断
        notificationService.notify(
                otherId, "COUPLE_DISSOLVE",
                "羁绊已断", "愿你在没有彼此的日子里，依然发光", c.getId());
    }
    // ===== 6. 情侣资料 =====
    @Override
    public CoupleProfileDTO profile() {
        User me = me();
        if (me.getCoupleId() == null) {
            throw new BizException(ResultCode.COUPLE_NOT_FOUND);
        }
        Couple c = this.baseMapper.selectById(me.getCoupleId());
        if (c == null) {
            throw new BizException(ResultCode.COUPLE_NOT_FOUND);
        }
        CoupleProfileDTO dto = new CoupleProfileDTO();
        // 实体 anniversary 是 LocalDate，DTO 是 LocalDateTime，需转换
        dto.setAnniversary(c.getAnniversary() != null ? c.getAnniversary().atStartOfDay() : null);
        dto.setNicknameA(c.getNicknameA());
        dto.setNicknameB(c.getNicknameB());
        return dto;
    }
    // 私有工具
    private User me() {
        // 获取登录用户
        User u = userMapper.selectById(UserContext.getUserId());
        // 未获取到用户信息
        if( u == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND);
        }
        return u;
    }
    // 是否曾绑定
    private boolean everBound(User u) {
        return u.getEverBound() !=null && u.getEverBound() == 1;
    }
    // 锁一生一次
    private void setEverBound(User u) {
        u.setEverBound(1);
        userMapper.updateById(u);
    }
    // 解除绑定关系
    private void clearCouple(User u) {
        u.setCoupleId(null);
        userMapper.updateById(u);
    }
    // 生成不重复的邀请码
    private String genCode() {
        String code;
        int retry = 0;
        do {
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < 6; i++) {
                sb.append(CODE_CHARS.charAt(RANDOM.nextInt(CODE_CHARS.length())));
            }
            code = sb.toString();
            retry++;
        }
        while (existsCode(code)&& retry< 10);
        return code;
    }
    // 邀请码是否已存在
    private boolean existsCode(String code) {
        return userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getInviteCode, code)) > 0;
    }
    // Couple实体转VO
    private CoupleVO toVO(Couple c) {
        CoupleVO v = new CoupleVO();
        v.setId(c.getId());
        v.setUserAId(c.getUserAId());
        v.setUserBId(c.getUserBId());
        v.setStatus(c.getStatus());
        v.setBindTime(c.getBindTime());
        v.setAnniversary(c.getAnniversary());
        v.setNicknameA(c.getNicknameA());
        v.setNicknameB(c.getNicknameB());
        return v;
    }
}
