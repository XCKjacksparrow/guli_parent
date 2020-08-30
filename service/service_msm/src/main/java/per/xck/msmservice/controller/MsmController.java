package per.xck.msmservice.controller;

import per.xck.commonutils.R;
import per.xck.msmservice.service.MsmService;
import per.xck.msmservice.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
@CrossOrigin
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @GetMapping(value = "/send/{phone}")
    public R send(@PathVariable String phone){
        // 1.先从redis获取验证码，如果取到则直接返回
        String code = redisTemplate.opsForValue().get("phone");
        if (!StringUtils.isEmpty("")){
            return R.ok();
        }

        // 2.获取不到，进行阿里云发送
        // 生成随机数
        code = RandomUtil.getFourBitRandom();
        Map<String,Object> param = new HashMap<>();
        param.put("code",code);
        // 调用发送短信的方法
        boolean isSend = msmService.send(param,phone);
        if (isSend){
            // 发送成功，写入redis 设置有效时间
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.ok();
        }else
            return R.error().message("短信发送失败");
    }
}
