package per.xck.eduservice.controller;


import per.xck.commonutils.R;
import per.xck.eduservice.entity.EduCourse;
import per.xck.eduservice.entity.vo.CourseInfoVo;
import per.xck.eduservice.entity.vo.CoursePublishVo;
import per.xck.eduservice.entity.vo.CourseQuery;
import per.xck.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author kazemi
 * @since 2020-08-03
 */
@RestController
@RequestMapping("/eduservice/course")
@Api(description = "课程管理")
@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    // 添加课程基本信息
    @PostMapping("addCourseInfo")
    @ApiOperation("添加课程基本信息")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        // 返回添加之后的课程id，为了之后添加课程大纲
        String id = courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId",id);
    }

    // 根据课程id查询课程基本信息
    @GetMapping("getCourseInfo/{courseId}")
    @ApiOperation("根据课程id查询课程基本信息")
    public R getCourseInfo(@PathVariable String courseId){
        CourseInfoVo courseInfoVo = courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo",courseInfoVo);
    }

    // 修改课程信息
    @PostMapping("updateCourseInfo")
    @ApiOperation("修改课程信息")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        courseService.updataCourseInfo(courseInfoVo);
        return R.ok();
    }

    // 根据课程id查询课程确认信息
    @GetMapping("getPublishCourseInfo/{id}")
    @ApiOperation(value = "根据ID获取课程发布信息")
    public R getPublishCourseInfo(@PathVariable String id){
        CoursePublishVo coursePublishVo = courseService.publishCourseInfo(id);
        return R.ok().data("publishCourse",coursePublishVo);
    }

    // 修改课程状态 draft -> publish
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id){
        EduCourse course = new EduCourse();
        course.setId(id);
        course.setStatus("Normal");
        courseService.updateById(course);
        return R.ok();
    }

    @ApiOperation(value = "分页课程列表")
    @PostMapping("{page}/{limit}")
    public R pageQuery(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
            @RequestBody CourseQuery courseQuery){
        Page<EduCourse> pageParam = new Page<>(page, limit);
        courseService.pageQuery(pageParam, courseQuery);
        List<EduCourse> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    @ApiOperation(value = "根据ID删除课程")
    @DeleteMapping("removeById/{id}")
    public R removeById(
            @ApiParam(name = "id", value = "课程ID", required = true)
            @PathVariable String id){
        boolean result = courseService.removeCourseById(id);
        if(result){
            return R.ok();
        }else{
            return R.error().message("删除失败");
        }
    }
}

