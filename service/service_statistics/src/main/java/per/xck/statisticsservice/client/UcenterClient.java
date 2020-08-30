package per.xck.statisticsservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import per.xck.commonutils.R;

@Component
@FeignClient(value = "service-ucenter",fallback = UcenterClientImpl.class)
public interface UcenterClient {

    @GetMapping(value = "/educenter/ucenter/member/countRegister/{day}")
    public R registerCount(@PathVariable("day") String day);

    @GetMapping(value = "/educenter/ucenter/member/countLogin/{day}")
    public R registerLogin(@PathVariable("day") String day);
}
