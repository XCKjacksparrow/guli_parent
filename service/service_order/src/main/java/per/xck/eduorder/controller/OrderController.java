package per.xck.eduorder.controller;


import per.xck.commonutils.JwtUtils;
import per.xck.commonutils.R;
import per.xck.eduorder.entity.Order;
import per.xck.eduorder.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author kazemi
 * @since 2020-08-28
 */
@RestController
@RequestMapping("/eduorder/order")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 1.生成订单
    @PostMapping("createOrder/{courseId}")
    public R createOrder(@PathVariable String courseId, HttpServletRequest request){
        // 返回订单号
        String orderNo = orderService.createOrders(courseId, JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data("orderNo",orderNo);
    }

    // 2.根据订单号查询订单信息
    @GetMapping("getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderId);
        Order order = orderService.getOne(wrapper);
        return R.ok().data("item",order);
    }

    // 根据课程id和用户id查询订单表中的状态
    @GetMapping("isBuyCourse/{courseId}/{member}")
    public boolean isBuyCourse(@PathVariable String courseId,
                               @PathVariable String member){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        wrapper.eq("member_id",member);
        wrapper.eq("status",1);
        int count = orderService.count(wrapper);
        // count > 0已经支付
        return count > 0;
    }
}

