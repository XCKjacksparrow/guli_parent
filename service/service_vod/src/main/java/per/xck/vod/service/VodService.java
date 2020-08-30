package per.xck.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {
    String uploadVideo(MultipartFile multipartFile);

    void removeMoreAliVideo(List videoIdList);
}
