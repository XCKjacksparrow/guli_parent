package per.xck.eduservice.controller;


import per.xck.commonutils.R;
import per.xck.eduservice.entity.vo.VideoInfoForm;
import per.xck.eduservice.service.EduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author kazemi
 * @since 2020-08-03
 */
@RestController
@RequestMapping("/eduservice/video")
@Api(description="课时管理")
@CrossOrigin
public class EduVideoController {

    @Autowired
    private EduVideoService videoService;

    @ApiOperation(value = "新增课时")
    @PostMapping("addVideo")
    public R save(
            @ApiParam(name = "videoForm", value = "课时对象", required = true)
            @RequestBody VideoInfoForm videoInfoForm){
        videoService.saveVideoInfo(videoInfoForm);
        return R.ok();
    }

    @ApiOperation(value = "根据ID查询课时")
    @GetMapping("getVideoInfoById/{id}")
    public R getVideoInfoById(
            @ApiParam(name = "id", value = "课时ID", required = true)
            @PathVariable String id){
        VideoInfoForm videoInfoForm = videoService.getVideoInfoFormById(id);
        return R.ok().data("item", videoInfoForm);
    }

    // 删除小节
    // TODO 后面这个方法需要完善，删除小节，同时把里面的视频也要删除
    @DeleteMapping("{id}")
    public R deleteVideo(@ApiParam(name = "id", value = "课时ID", required = true)@PathVariable String id){
        boolean result = videoService.removeVideoById(id);
        if(result){
            return R.ok();
        }else{
            return R.error().message("删除失败");
        }
    }

    // 更新课时 TODO
    @ApiOperation(value = "更新课时")
    @PostMapping("updateVideoInfoById")
    public R updateVideoInfoById(
            @ApiParam(name = "VideoInfoForm", value = "课时基本信息", required = true)
            @RequestBody VideoInfoForm videoInfoForm){
        videoService.updateVideoInfoById(videoInfoForm);
        return R.ok();
    }


}

