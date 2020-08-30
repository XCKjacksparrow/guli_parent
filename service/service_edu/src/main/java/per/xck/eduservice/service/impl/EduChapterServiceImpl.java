package per.xck.eduservice.service.impl;

import per.xck.eduservice.entity.EduChapter;
import per.xck.eduservice.entity.EduVideo;
import per.xck.eduservice.entity.chapter.ChapterVo;
import per.xck.eduservice.entity.chapter.VideoVo;
import per.xck.eduservice.mapper.EduChapterMapper;
import per.xck.eduservice.service.EduChapterService;
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
 * 课程 服务实现类
 * </p>
 *
 * @author kazemi
 * @since 2020-08-03
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService videoService;

    // 课程大纲列表 根据课程id进行查询
    @Override
    public List<ChapterVo> getChapterVideosByCourseId(String courseId) {
        //1 根据课程id 查询课程里面的所有章节
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id",courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);

        //2 根据课程id 查询课程里面的所有小节
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperChapter.eq("course_id",courseId);
        List<EduVideo> eduVideoList = videoService.list(wrapperVideo);

        // 创建结果list
        List<ChapterVo> finalList = new ArrayList<>();

        //3 遍历章节list进行封装
        for (EduChapter eduChapter : eduChapterList) {
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            finalList.add(chapterVo);

            // 创建list用于封装
            List<VideoVo> videoList = new ArrayList<>();
            //4 遍历小节list进行判断
            for (EduVideo eduVideo : eduVideoList) {
                // 判断
                if (eduVideo.getChapterId().equals(chapterVo.getId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    videoList.add(videoVo);
                }
            }
            chapterVo.setChildren(videoList);
        }
        return finalList;
    }

    // 删除章节
    @Override
    public boolean deleteChapter(String chapterId) {
        // 根据chapterid去查询小节表，如果有小节，则不删除、
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id", chapterId);
        int count = videoService.count(wrapper);
        if (count > 0){
            // 有小节 不删除
            throw new GuliException(20001, "不能删除");
        }else {
            // 进行删除
            int result = baseMapper.deleteById(chapterId);
            return result > 0;
        }
    }

    @Override
    public boolean removeByCourseId(String courseId) {
        QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        Integer count = baseMapper.delete(queryWrapper);
        return null != count && count > 0;
    }
}
