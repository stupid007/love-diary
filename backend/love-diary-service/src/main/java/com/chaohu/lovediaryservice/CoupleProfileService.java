// 定义包路径：业务层接口包
package com.chaohu.lovediaryservice;
// 导入情侣资料读模型
import com.chaohu.lovediarymodel.dto.CoupleProfileDTO;
// 导入情侣资料更新请求体
import com.chaohu.lovediarymodel.dto.CoupleProfileUpdateDTO;
// 导入地图点位 VO
import com.chaohu.lovediarymodel.vo.MapPointVO;
// 导入 List
import java.util.List;

// 情侣资料业务接口；身份从 UserContext 取，不收 userId 形参
public interface CoupleProfileService {
    // 读取情侣资料，未绑定抛 COUPLE_NOT_FOUND
    CoupleProfileDTO profile();
    // 更新资料：共享字段双方可改，私有字段按 A/B 校验
    void save(CoupleProfileUpdateDTO dto);
    // 聚合 GIS 点位：已绑定两点，未绑定/已解除只返回我的点
    List<MapPointVO> mapPoints();
}
