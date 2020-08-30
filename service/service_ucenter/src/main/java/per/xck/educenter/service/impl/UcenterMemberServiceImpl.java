package per.xck.educenter.service.impl;

import per.xck.commonutils.JwtUtils;
import per.xck.commonutils.MD5;
import per.xck.educenter.entity.UcenterMember;
import per.xck.educenter.entity.vo.RegisterVo;
import per.xck.educenter.mapper.UcenterMemberMapper;
import per.xck.educenter.service.UcenterMemberService;
import per.xck.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author kazemi
 * @since 2020-08-26
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    // 登录
    @Override
    public String login(UcenterMember member) {
        // 获取手机号和密码
        String mobile = member.getMobile();
        String password = member.getPassword();

        // 非空判断
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
            throw new GuliException(20001,"登录失败，手机号或密码不能为空");
        }

        // 判断手机号
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UcenterMember mobileMember = baseMapper.selectOne(wrapper);
        // 判断查询是否为空
        if (null == mobileMember){
            throw new GuliException(20001,"登录失败，用户不存在");
        }
        // 判断密码
        // 把密码先进行加密 MD5加密
        if (!MD5.encrypt(password).equals(mobileMember.getPassword())){
            throw new GuliException(20001,"登录失败，密码错误");
        }
        // 判断用户是否被禁用
        if (mobileMember.getIsDisabled()){
            throw new GuliException(20001,"登录失败，用户被禁用");
        }
        // 登录成功
        // 使用jwt生成token
        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());

        return jwtToken;
    }

    // 注册
    @Override
    public void register(RegisterVo registerVo) {
        // 获取注册的数据
        String code = registerVo.getCode();
        String mobile = registerVo.getMobile();
        String nickName = registerVo.getNickname();
        String password = registerVo.getPassword();

        // 非空判断
        // 非空判断
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)
        || StringUtils.isEmpty(code) || StringUtils.isEmpty(nickName)){
            throw new GuliException(20001,"登录失败，输入不能为空");
        }

        // 判断验证码
        // 先获取 redis中的验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if (!code.equals(redisCode)){
            throw new GuliException(20001,"手机验证码错误");
        }

        // 判断手机号是否重复
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0){
            throw new GuliException(20001,"注册失败");
        }

        // 数据添加到数据库
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickName);
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);    // 用户不禁用
        member.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");
        baseMapper.insert(member);
    }

    // 根据openid查询对象
    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

    @Override
    public Integer countLoginByDay(String day) {
        return baseMapper.selectRegisterCount(day);
    }

    @Override
    public Integer countRegisterByDay(String day) {
        return baseMapper.selectLoginCount(day);
    }
}
