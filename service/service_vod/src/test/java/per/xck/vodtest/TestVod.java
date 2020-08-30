package per.xck.vodtest;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import sun.security.acl.AclEntryImpl;

import java.util.List;

public class TestVod {
    public static void main(String[] args) {
        // 上传视频
        String accessKeyId = "LTAI4G8Ez7AiGcH4KK5LPkSB";
        String accessKeySecret = "QfM3bFPX8S0nzt0D1n5UNQm3v86l4p";
        String title = "6 - What If I Want to Move Faster - upload by skd";  // 上传后文件的名字
        String fileName = "C:\\Users\\kazemi\\Videos\\6 - What If I Want to Move Faster.mp4";   // 本地文件路径名称

        //本地文件上传
        UploadVideoRequest request = new UploadVideoRequest(accessKeyId,
                accessKeySecret, title, fileName);
        /* 可指定分片上传时每个分片的大小，默认为1M字节 */
        request.setPartSize(1 * 1024 * 1024L);
         /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务
器情况指定）*/
        request.setTaskNum(1);
        /* 是否开启断点续传, 默认断点续传功能关闭。当网络不稳定或者程序崩溃时，再次发起相同上
传请求，可以继续未完成的上传任务，适用于超时3000秒仍不能上传完成的大文件。
20 注意: 断点续传开启后，会在上传过程中将上传位置写入本地磁盘文件，影响文件上传速
度，请您根据实际情况选择是否开启*/
        request.setEnableCheckpoint(false);

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);

        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
             /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情
况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
    }

    public static void getPlayUrl() throws ClientException {
        // 1. 根据视频id获取视频播放地址
        // 创建初始化对象
        DefaultAcsClient client = InitObject.initVodCLient("LTAI4G8Ez7AiGcH4KK5LPkSB", "QfM3bFPX8S0nzt0D1n5UNQm3v86l4p");

        // 创建获取视频地址request对象和response
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        GetPlayInfoResponse response = new GetPlayInfoResponse();

        // 向request对象设置id
        request.setVideoId("daf611931f1148ae8146ba54948514ac");

        // 调用初始化对象里面的方法，传递request，获取数据
        response = client.getAcsResponse(request);

        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
        // 播放地址
        for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
            System.out.println("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
        }
        // Base信息
        System.out.println("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");

    }

    public static void getPlayAuth() throws ClientException {
        // 根据视频id 获取播放凭证
        // 创建初始化对象
        DefaultAcsClient client = InitObject.initVodCLient("LTAI4G8Ez7AiGcH4KK5LPkSB", "QfM3bFPX8S0nzt0D1n5UNQm3v86l4p");
        // 创建获取视频凭证的request和response对象
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();

        // 向request中设置id
        request.setVideoId("daf611931f1148ae8146ba54948514ac");
        // 调用初始化对象得到凭证
        response = client.getAcsResponse(request);
        System.out.println("playauth : " + response.getPlayAuth());
    }
}
