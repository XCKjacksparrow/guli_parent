package per.xck.vod.Utils;

import com.aliyun.oss.ClientException;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;

public class InitVodClient {

    public static DefaultAcsClient initVodCLient(String accessKeyId, String accessKeySecret) throws ClientException{
        String reginId = "cn-shanghai";
        DefaultProfile profile = DefaultProfile.getProfile(reginId,accessKeyId,accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);

        return client;
    }
}
