package per.xck.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import per.xck.commonutils.R;
import per.xck.servicebase.exceptionhandler.GuliException;
import per.xck.vod.service.VodService;
import per.xck.vod.Utils.ConstantVodUtils;
import per.xck.vod.Utils.InitVodClient;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/eduvod/video")
@CrossOrigin
public class VodController {

    @Autowired
    private VodService vodService;

    // 上传视频
    @PostMapping("uploadAliVideo")
    public R uploadAliVideo(@ApiParam(name = "file", value = "文件", required = true)
                            @RequestParam("file") MultipartFile multipartFile) {
        // 返回上传视频的id
        String videoId = vodService.uploadVideo(multipartFile);
        return R.ok().data("videoId", videoId);
    }

    // 删除阿里云视频
    @DeleteMapping("removeAliVideo/{id}")
    public R removeAliVideo(@PathVariable("id") String id){
        try {
            DefaultAcsClient client = InitVodClient.initVodCLient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECERT);
            DeleteVideoRequest request = new DeleteVideoRequest();
            request.setVideoIds(id);
            client.getAcsResponse(request);
            return R.ok();
        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"删除视频失败");
        }
    }

    // 删除多个ali中的视频
    @DeleteMapping("deleteBatch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList){
        vodService.removeMoreAliVideo(videoIdList);
        return R.ok();
    }

    // 根据视频id获取播放凭证
    @GetMapping("getPlayAuth/{id}")
    public R getPlayAuth(@PathVariable String id){
        try {
            // 1.创建初始化对象
            DefaultAcsClient client = InitVodClient.initVodCLient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECERT);
            // 2.创建获取凭证的request和response对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();

            // 3.向request中设置id
            request.setVideoId(id);

            // 4.得到凭证
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            return R.ok().data("playAuth",playAuth);
        }catch (Exception e){
            throw new GuliException(20001,"获取凭证失败");
        }
    }
}
