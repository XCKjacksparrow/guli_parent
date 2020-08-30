package per.xck.eduservice.service;

import per.xck.eduservice.entity.EduChapter;
import per.xck.eduservice.entity.chapter.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author kazemi
 * @since 2020-08-03
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> getChapterVideosByCourseId(String courseId);

    boolean deleteChapter(String chapterId);

    boolean removeByCourseId(String courseId);
}
