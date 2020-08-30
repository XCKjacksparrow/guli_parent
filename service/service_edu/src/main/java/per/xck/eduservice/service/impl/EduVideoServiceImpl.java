package per.xck.eduservice.service.impl;

import com.alibaba.excel.util.StringUtils;
import per.xck.commonutils.R;
import per.xck.eduservice.client.VodClient;
import per.xck.eduservice.entity.EduVideo;
import per.xck.eduservice.entity.vo.VideoInfoForm;
import per.xck.eduservice.mapper.EduVideoMapper;
import per.xck.eduservice.service.EduVideoService;
import per.xck.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author kazemi
 * @since 2020-08-03
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VodClient vodClient;

    @Override
    public void saveVideoInfo(VideoInfoForm videoInfoForm) {
        EduVideo video = new EduVideo();
        BeanUtils.copyProperties(videoInfoForm, video);
        video.setIsFree(videoInfoForm.getFree());
        boolean result = this.save(video);
        if(!result){
            throw new GuliException(20001, "课时信息保存失败");
        }
    }

    @Override
    public VideoInfoForm getVideoInfoFormById(String id) {
        //从video表中取数据
        EduVideo video = baseMapper.selectById(id);
        if(video == null){
            throw new GuliException(20001, "数据不存在");
        }
        //创建videoInfoForm对象
        VideoInfoForm videoInfoForm = new VideoInfoForm();
        BeanUtils.copyProperties(video, videoInfoForm);
        videoInfoForm.setFree(video.getIsFree());
        return videoInfoForm;
    }

    @Override
    public void updateVideoInfoById(VideoInfoForm videoInfoForm) {
        //保存课时基本信息
        EduVideo video = new EduVideo();
        BeanUtils.copyProperties(videoInfoForm, video);
        video.setIsFree(videoInfoForm.getFree());
        boolean result = this.updateById(video);
        if(!result){
            throw new GuliException(20001, "课时信息保存失败");
        }
    }

    @Override
    public boolean removeVideoById(String id) {
        //删除视频资源 TODO 删除阿里云中的视频

        // 根据小节id获取到视频id
        EduVideo eduVideo = baseMapper.selectById(id);
        if (!StringUtils.isEmpty(eduVideo.getVideoSourceId())){
            R r = vodClient.removeAliVideo(eduVideo.getVideoSourceId());
            if (r.getCode() == 20001){
                throw new GuliException(20001,"删除视频失败");
            }
        }
        Integer result = baseMapper.deleteById(id);
        return null != result && result > 0;
    }

    @Override
    // 根据课程id 删除小节
    public boolean removeByCourseId(String courseId) {
        // 1根据课程id查询出所有视频id
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        wrapper.select("video_source_id");
        List<EduVideo> eduVideoList = baseMapper.selectList(wrapper);

        // List<EduVideo> => List<String>
        List<String> videoIdList = new ArrayList<>();
        for (EduVideo eduVideo : eduVideoList) {
            if(!StringUtils.isEmpty(eduVideo.getVideoSourceId())){
                videoIdList.add(eduVideo.getVideoSourceId());
            }
        }
        // 多个视频id删除
        if (videoIdList.size() > 0){
            vodClient.deleteBatch(videoIdList);
        }

        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        Integer count = baseMapper.delete(queryWrapper);
        return null != count && count > 0;
    }

}
