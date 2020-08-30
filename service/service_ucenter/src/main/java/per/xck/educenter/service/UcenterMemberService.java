package per.xck.educenter.service;

import per.xck.educenter.entity.UcenterMember;
import per.xck.educenter.entity.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author kazemi
 * @since 2020-08-26
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(UcenterMember member);

    void register(RegisterVo registerVo);

    UcenterMember getOpenIdMember(String openid);

    Integer countLoginByDay(String day);

    Integer countRegisterByDay(String day);
}
