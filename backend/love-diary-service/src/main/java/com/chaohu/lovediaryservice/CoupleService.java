package com.chaohu.lovediaryservice;

import com.chaohu.lovediarymodel.dto.BindDTO;
import com.chaohu.lovediarymodel.dto.CoupleProfileDTO;
import com.chaohu.lovediarymodel.entity.User;
import com.chaohu.lovediarymodel.vo.CoupleStateVO;
import com.chaohu.lovediarymodel.vo.InviteVO;

// 情侣关系业务接口；所有方法内部自行从UserContext取当前用户，不收username形参
public interface CoupleService {
    //邀请方生成邀请码
    InviteVO invite();
    // 申请方提交邀请码，建待确认关系并且通知对方
    void bind(BindDTO dto);
    // 邀请方确认并且关系生效“一生一次”
    void confirm(Long coupleId);
    // 查询四态(everBound+当前关系)，前端据此切换视图
    CoupleStateVO state();
    //解绑，关系置解除态，清空双方couple_id(everBound保留)
    void unbind();
    // 情侣资料，纪念日与双方昵称
    CoupleProfileDTO profile();
}
