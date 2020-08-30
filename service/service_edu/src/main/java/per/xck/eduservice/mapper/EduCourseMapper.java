package per.xck.eduservice.mapper;

import per.xck.eduservice.entity.EduCourse;
import per.xck.eduservice.entity.frontvo.CourseWebVo;
import per.xck.eduservice.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author kazemi
 * @since 2020-08-03
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    public CoursePublishVo getPublishCourseInfo(String courseId);

    CourseWebVo getBaseCourseInfo(String courseId);
}
