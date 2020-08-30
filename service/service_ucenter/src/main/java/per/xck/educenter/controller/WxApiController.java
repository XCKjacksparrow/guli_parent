package per.xck.educenter.controller;

import per.xck.commonutils.JwtUtils;
import per.xck.commonutils.R;
import per.xck.educenter.entity.UcenterMember;
import per.xck.educenter.service.UcenterMemberService;
import per.xck.educenter.utils.ConstantPropertiesUtil;
import per.xck.educenter.utils.HttpClientUtils;
import per.xck.servicebase.exceptionhandler.GuliException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URLEncoder;
import java.util.HashMap;

@Controller
@CrossOrigin
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;

    // 2.获取扫描人的信息，添加数据
    @GetMapping("callback")
    public String callback(String code, String state) {
        try {
            // 1. 获取code值，临时票据，类似于验证码

            // 2. 拿这code请求微信地址，得到accessToken和openid
            String baseAccessTokenUrl =
                    "https://api.weixin.qq.com/sns/oauth2/access_token" +
                            "?appid=%s" +
                            "&secret=%s" +
                            "&code=%s" +
                            "&grant_type=authorization_code";
            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    ConstantPropertiesUtil.WX_OPEN_APP_ID,
                    ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                    code);
            // 请求地址，
            // 使用HttpClient
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            // 把accessTokenInfo转换成Map 使用Gson
            Gson gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String) mapAccessToken.get("access_token");
            String openid = (String) mapAccessToken.get("openid");



            // 把扫码人信息添加到数据库
            // 判断数据库中是否有相同信息，根据openid
            UcenterMember member = memberService.getOpenIdMember(openid);
            if (null == member){ //member是空

                // 3.拿着accessToken和openid 才能获取扫码人的信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl,
                        access_token,
                        openid);
                String userInfo = HttpClientUtils.get(userInfoUrl);
                // 获取返回userInfo字符串扫描人的信息
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                String nickname = (String) userInfoMap.get("nickname"); // 昵称
                String headimgurl = (String) userInfoMap.get("headimgurl"); // 头像

                member = new UcenterMember();
                member.setOpenid(openid);
                member.setNickname(nickname);
                member.setAvatar(headimgurl);
                memberService.save(member);
            }
            // 返回首页面
            // 使用jwt根据member对象生成token字符串，通过路径传递
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());

            return "redirect:http://localhost:3000?token=" + jwtToken;

        } catch (Exception e) {
            throw new GuliException(20001,"登录失败");
        }
    }

    // 1.生成微信扫描二维码
    @GetMapping("login")
    public String getWxCode() {

        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        // 对redirect_url 进行编码
        String redirect_url = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL;
        try {
            redirect_url = URLEncoder.encode(redirect_url, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 设置%s里的参数
        String url = String.format(
                baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirect_url,
                "kazemi");
        return "redirect:" + url;
    }
}
