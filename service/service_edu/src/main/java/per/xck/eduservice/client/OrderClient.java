package per.xck.eduservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-order",fallback = OrderClientImpl.class)
@Component
public interface OrderClient {

    @GetMapping("/eduorder/order/isBuyCourse/{courseId}/{member}")
    public boolean isBuyCourse(@PathVariable("courseId") String courseId,
                               @PathVariable("member") String member);
}
