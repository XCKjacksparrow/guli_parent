package per.xck.eduservice.controller;


import per.xck.commonutils.R;
import per.xck.eduservice.entity.EduChapter;
import per.xck.eduservice.entity.chapter.ChapterVo;
import per.xck.eduservice.service.EduChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/eduservice/chapter")
@Api(description = "课程章节管理")
@CrossOrigin
public class EduChapterController {

    @Autowired
    private EduChapterService chapterService;

    // 课程大纲列表 根据课程id进行查询
    @GetMapping("getChapterVideo/{courseId}")
    public R getChapterVideo(@PathVariable String courseId){
        List<ChapterVo> list = chapterService.getChapterVideosByCourseId(courseId);
        return R.ok().data("allChapterVideos",list);
    }

    // 添加章节
    @PostMapping("addChapter")
    @ApiOperation("添加章节")
    public R addChapter(@RequestBody EduChapter eduChapter){
        chapterService.save(eduChapter);
        return R.ok();
    }

    // 根据id查询
    @ApiOperation("根据id查询")
    @GetMapping("getChapterInfo/{chapterId}")
    public R getChapterInfo(@PathVariable String chapterId){
        EduChapter eduChapter = chapterService.getById(chapterId);
        return R.ok().data("chapter",eduChapter);
    }

    // 修改章节
    @PostMapping("updateChapter")
    @ApiOperation("修改章节")
    public R updateChapter(@RequestBody EduChapter eduChapter){
        chapterService.updateById(eduChapter);
        return R.ok();
    }

    // 根据id删除
    @DeleteMapping("deleteChapter/{chapterId}")
    @ApiOperation("根据id删除")
    public R deleteChapter(@PathVariable String chapterId){
        boolean flag = chapterService.deleteChapter(chapterId);
        if (flag){
            return R.ok();
        }else {
            return R.error();
        }
    }
}

