package per.xck.educenter.controller;


import per.xck.commonutils.JwtUtils;
import per.xck.commonutils.R;
import per.xck.educenter.entity.UcenterMember;
import per.xck.educenter.entity.vo.RegisterVo;
import per.xck.educenter.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author kazemi
 * @since 2020-08-26
 */
@RestController
@RequestMapping("/educenter/ucenter/member")
@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    // 登录
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember member) {
        // 调用service的方法实现登录
        // 返回token值，使用jwt生成
        String token = memberService.login(member);
        return R.ok().data("token", token);
    }

    // 注册
    @PostMapping("register")
    public R registerUser(@RequestBody RegisterVo registerVo) {
        memberService.register(registerVo);
        return R.ok();
    }

    // 根据token获取用户信息
    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request) {
        // 调用jwt工具类的方法，根据request对象获取头信息，返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        // 查询数据库获取用户信息
        UcenterMember member = memberService.getById(memberId);
        return R.ok().data("userInfo", member);
    }

    // 根据id获取用户信息
    @GetMapping("getMemberInfoById/{id}")
    public per.xck.commonutils.vo.UcenterMember getMemberInfoById(@PathVariable String id) {
        UcenterMember member = memberService.getById(id);
        per.xck.commonutils.vo.UcenterMember ucenterMember = new per.xck.commonutils.vo.UcenterMember();
        BeanUtils.copyProperties(member, ucenterMember);
        return ucenterMember;
    }

    @GetMapping(value = "countRegister/{day}")
    public R registerCount(@PathVariable String day) {
        Integer count = memberService.countRegisterByDay(day);
        return R.ok().data("countRegister", count);
    }

    @GetMapping(value = "countLogin/{day}")
    public R registerLogin(@PathVariable String day) {
        Integer count = memberService.countLoginByDay(day);
        return R.ok().data("countLogin", count);
    }
}

