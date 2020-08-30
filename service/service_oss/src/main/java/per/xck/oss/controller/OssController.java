package per.xck.oss.controller;

import per.xck.commonutils.R;
import per.xck.oss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("eduoss/fileoss")
@Api(description="阿里云文件管理")
@CrossOrigin
public class OssController {

    @Autowired
    private OssService ossService;

    // 上传头像的方法
    @ApiOperation(value = "文件上传")
    @PostMapping
    public R uploadoss(MultipartFile file){
        // 获取上传的文件  MultipartFile
        // 返回路劲
        String url = ossService.uploadFileAvatar(file);
        return R.ok().data("url",url);
    }
}
