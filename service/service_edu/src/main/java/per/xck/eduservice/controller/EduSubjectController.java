package per.xck.eduservice.controller;


import per.xck.commonutils.R;
import per.xck.eduservice.entity.subject.OneSubject;
import per.xck.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author kazemi
 * @since 2020-08-02
 */
@RestController
@RequestMapping("/eduservice/subject")
@Api(description = "课程分类管理")
@CrossOrigin
public class EduSubjectController {

    @Autowired
    private EduSubjectService subjectService;

    // 添加课程分类
    // 获取上传的文件，把文件内容读出来
    @PostMapping("addSubject")
    @ApiOperation("添加课程分类")
    public R addSubject(MultipartFile file){
        // 上传的文件
        subjectService.saveSubject(file, subjectService);
        return R.ok();
    }

    // 课程分类列表(树形)
    @GetMapping("getAllSubjects")
    @ApiOperation("课程分类列表(树形)")
    public R getAllSubjects(){
        // list 集合中是一级分类
        List<OneSubject> oneSubjects = subjectService.getAllOneTwoSubjects();
        return R.ok().data("list",oneSubjects);
    }
}

