package per.xck.eduorder.client;

import per.xck.commonutils.vo.UcenterMember;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-ucenter")
@Component
public interface UcenterClient {
    // 根据memberid查询会员信息
    @GetMapping("/educenter/ucenter/member/getMemberInfoById/{id}")
    public UcenterMember getMemberInfoById(@PathVariable("id") String id);
}
