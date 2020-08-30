package per.xck.eduservice.controller.front;

import per.xck.commonutils.JwtUtils;
import per.xck.commonutils.R;
import per.xck.eduservice.client.OrderClient;
import per.xck.eduservice.entity.EduCourse;
import per.xck.eduservice.entity.chapter.ChapterVo;
import per.xck.eduservice.entity.frontvo.CourseFrontVo;
import per.xck.eduservice.entity.frontvo.CourseWebVo;
import per.xck.eduservice.service.EduChapterService;
import per.xck.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(value = "课程详情")
@RequestMapping("/eduservice/coursefront")
@RestController
@CrossOrigin
public class CourseFrontController {


    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrderClient orderClient;

    @ApiOperation(value = "条件查询带分页查询课程")
    @PostMapping(value = "getCourseFrontList/{page}/{limit}")
    public R pageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
            @RequestBody(required = false) CourseFrontVo courseFrontVo
    ){
        Page<EduCourse> pageParam = new Page<>(page, limit);
        Map<String, Object> map = courseService.getCourseFrontList(pageParam,courseFrontVo);
        return R.ok().data(map);
    }

    // 课程详情的方法
    @GetMapping("getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(@PathVariable String courseId, HttpServletRequest request){
        // 根据课程id 编写sql查询课程信息
        CourseWebVo courseWebVo = courseService.getBaseCourseInfo(courseId);

        // 根据课程id查询章节和小节
        List<ChapterVo> chapterVideosList = chapterService.getChapterVideosByCourseId(courseId);

        // 根据课程id和用户id查询当前课程是否已经支付过
        boolean buyCourse = orderClient.isBuyCourse(courseId, JwtUtils.getMemberIdByJwtToken(request));

        return R.ok().data("courseWebVo",courseWebVo).data("chapterVideosList",chapterVideosList).data("isBuy",buyCourse);
    }

    @GetMapping("getCourseInfoOrder/{courseId}")
    public per.xck.commonutils.vo.CourseWebVo getCourseInfoOrder(@PathVariable String courseId){
        per.xck.commonutils.vo.CourseWebVo courseWebVo = new per.xck.commonutils.vo.CourseWebVo();
        CourseWebVo baseCourseInfo = courseService.getBaseCourseInfo(courseId);
        BeanUtils.copyProperties(baseCourseInfo,courseWebVo);
        return courseWebVo;
    }

}
