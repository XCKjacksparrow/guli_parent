package per.xck.eduservice.controller.front;

import per.xck.commonutils.JwtUtils;
import per.xck.commonutils.R;
import per.xck.commonutils.vo.UcenterMember;
import per.xck.eduservice.client.UcenterClient;
import per.xck.eduservice.entity.EduComment;
import per.xck.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/eduservice/commentfront")
public class CommentFrontController {

    @Autowired
    private EduCommentService commentService;

    @Autowired
    private UcenterClient ucenterClient;

    @ApiOperation(value = "评论分页列表")
    @GetMapping("{page}/{limit}")
    public R index(@ApiParam(name = "page", value = "当前页码", required = true) @PathVariable Long page,
                   @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable Long limit,
                   @ApiParam(name = "courseId", value = "当前课程", required = true) @RequestParam("courseId")  String courseId){
        Page<EduComment> pageParam = new Page<>(page,limit);
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        commentService.page(pageParam,wrapper);

        List<EduComment> commentList = pageParam.getRecords();
        Map<String, Object> map = new HashMap<>();
        map.put("items", commentList);
        map.put("current", pageParam.getCurrent());
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize());
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());
        return R.ok().data("map",map);
    }

    @ApiOperation(value = "添加评论")
    @PostMapping("save")
    public R save(@RequestBody EduComment comment, HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)){
            return R.error().code(20004).message("请先登录");
        }
        comment.setMemberId(memberId);

        UcenterMember member = ucenterClient.getMemberInfoById(memberId);

        comment.setNickname(member.getNickname());
        comment.setAvatar(member.getAvatar());

        commentService.save(comment);
        return R.ok();
    }
}
