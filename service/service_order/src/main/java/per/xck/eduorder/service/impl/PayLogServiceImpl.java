package per.xck.eduorder.service.impl;

import com.alibaba.fastjson.JSONObject;
import per.xck.eduorder.entity.Order;
import per.xck.eduorder.entity.PayLog;
import per.xck.eduorder.mapper.PayLogMapper;
import per.xck.eduorder.service.OrderService;
import per.xck.eduorder.service.PayLogService;
import per.xck.eduorder.utils.HttpClient;
import per.xck.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author kazemi
 * @since 2020-08-28
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;

    @Override
    public Map createNative(String orderNo) {
        try {
            // 1.根据订单号查询订单信息
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no", orderNo);
            Order order = orderService.getOne(wrapper);

            // 2.使用map设置生成二维码需要的参数
            Map m = new HashMap();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            m.put("body", order.getCourseTitle());
            m.put("out_trade_no", orderNo);
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue() + "");
            m.put("spbill_create_ip", "127.0.0.1");
            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n");
            m.put("trade_type", "NATIVE");

            // 3.发送httpclient请求，传递xml格式参数
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            // 设置xml格式参数
            client.setXmlParam(WXPayUtil.generateSignedXml(m,
                    "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            // 执行请求
            client.post();

            // 4.得到发送请求返回的结果
            // 返回的内容是xml格式
            String xml = client.getContent();

            // 把xml转换成map
            Map<String,String> resultMap = WXPayUtil.xmlToMap(xml);

            // 最终返回数据的封装
            Map map = new HashMap();
            map.put("out_trade_no", orderNo);
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());
            map.put("result_code", resultMap.get("result_code")); // 返回二维码操作状态码
            map.put("code_url", resultMap.get("code_url")); // 二维码地址

            return map;
        } catch (Exception e) {
            throw new GuliException(20001,"生成二维码失败");
        }
    }

    // 根据订单号查询支付状态
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {

        try {
            // 1.设置参数
            Map m = new HashMap();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            // 2. 发送HttpClient
            HttpClient client = new
                    HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m,
                    "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);

            client.post();

            // 3.得到返回的内容
            String xml = client.getContent();

            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            // 4、转成Map
            // 5、返回
            return resultMap;
        }catch (Exception e){
            throw new GuliException(20001,"查询状态失败");
        }
    }

    @Override
    public void updateOrderStatus(Map<String, String> map) {
        // 添加记录到支付表里，并且更新状态
        //获取订单号
        String orderNo = map.get("out_trade_no");
        // 根据订单号获取订单
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        Order order = orderService.getOne(wrapper);

        // 更新订单表里的支付状态
        if (order.getStatus().intValue() == 1){return;}

        order.setStatus(1); // 1代表已经支付
        orderService.updateById(order);

        // 向支付表中添加记录
        PayLog payLog = new PayLog();
        payLog.setOrderNo(orderNo);
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型 微信
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));// 流水号
        payLog.setAttr(JSONObject.toJSONString(map));
        baseMapper.insert(payLog);
    }
}
