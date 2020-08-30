package per.xck.eduorder.service.impl;

import per.xck.commonutils.vo.CourseWebVo;
import per.xck.commonutils.vo.UcenterMember;
import per.xck.eduorder.client.EduClient;
import per.xck.eduorder.client.UcenterClient;
import per.xck.eduorder.entity.Order;
import per.xck.eduorder.mapper.OrderMapper;
import per.xck.eduorder.service.OrderService;
import per.xck.eduorder.utils.OrderNoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author kazemi
 * @since 2020-08-28
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduClient eduClient;

    @Autowired
    private UcenterClient ucenterClient;

    // 1.生成订单
    @Override
    public String createOrders(String courseId, String memberIdByJwtToken) {
        // 通过远程调用
        // 根据课程id查询课程信息
        CourseWebVo courseInfo = eduClient.getCourseInfoOrder(courseId);
        // 根据memberid查询member
        UcenterMember memberInfo = ucenterClient.getMemberInfoById(memberIdByJwtToken);

        // 创建Order对象 set值
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo()); //订单号
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfo.getTitle());
        order.setCourseCover(courseInfo.getCover());
        order.setTeacherName(courseInfo.getTeacherName());
        order.setTotalFee(courseInfo.getPrice());
        order.setMemberId(memberIdByJwtToken);
        order.setMobile(memberInfo.getMobile());
        order.setNickname(memberInfo.getNickname());
        order.setStatus(0);
        order.setPayType(1);
        baseMapper.insert(order);
        // 返回订单号
        return order.getOrderNo();
    }
}
