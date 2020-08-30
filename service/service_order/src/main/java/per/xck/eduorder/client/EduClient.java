package per.xck.eduorder.client;

import per.xck.commonutils.vo.CourseWebVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-edu")
@Component
public interface EduClient {
    // 根据课程id查询课程信息
    @GetMapping("/eduservice/coursefront/getCourseInfoOrder/{courseId}")
    public CourseWebVo getCourseInfoOrder(@PathVariable("courseId") String courseId);
}
