package per.xck.eduorder.controller;


import per.xck.commonutils.R;
import per.xck.eduorder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author kazemi
 * @since 2020-08-28
 */
@RestController
@RequestMapping("/eduorder/paylog")
@CrossOrigin
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    // 生成二维码支付
    // 参数是订单号
    @GetMapping("createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo){
        // 返回信息，包含二维码地址，还有其他信息
        Map map = payLogService.createNative(orderNo);
        return R.ok().data(map);
    }

    // 查询支付状态 根据订单号
    @GetMapping("queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo){
        Map<String,String> map = payLogService.queryPayStatus(orderNo);
        if (map == null){
            return R.error().message("支付错误了");
        }
        if (map.get("trade_state").equals("SUCCESS")){
            // 表示支付成功
            // 添加记录到支付表里，并且更新状态
            payLogService.updateOrderStatus(map);
            return R.ok().message("支付成功");
        }
        return R.ok().code(25000).message("支付中");
    }
}

