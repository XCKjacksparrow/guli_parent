package per.xck.eduservice.service;

import per.xck.eduservice.entity.EduCourse;
import per.xck.eduservice.entity.frontvo.CourseFrontVo;
import per.xck.eduservice.entity.frontvo.CourseWebVo;
import per.xck.eduservice.entity.vo.CourseInfoVo;
import per.xck.eduservice.entity.vo.CoursePublishVo;
import per.xck.eduservice.entity.vo.CourseQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author kazemi
 * @since 2020-08-03
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVo courseInfoVo);

    CourseInfoVo getCourseInfo(String courseId);

    void updataCourseInfo(CourseInfoVo courseInfoVo);

    CoursePublishVo publishCourseInfo(String id);

    void pageQuery(Page<EduCourse> pageParam, CourseQuery courseQuery);

    boolean removeCourseById(String id);

    Map<String, Object> getCourseFrontList(Page<EduCourse> pageParam, CourseFrontVo courseFrontVo);

    CourseWebVo getBaseCourseInfo(String courseId);
}
