package per.xck.eduservice.client;

import per.xck.commonutils.vo.UcenterMember;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-ucenter",fallback = UcenterClientImpl.class)
@Component
public interface UcenterClient {

    @GetMapping("/educenter/ucenter/member/getMemberInfoById/{id}")
    public UcenterMember getMemberInfoById(@PathVariable("id") String id);
}
