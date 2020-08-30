package per.xck.statisticsservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import per.xck.commonutils.R;
import per.xck.statisticsservice.service.StatisticsDailyService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author kazemi
 * @since 2020-08-29
 */
@RestController
@RequestMapping("/statisticsservice/statisticsdaily")
@CrossOrigin
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService dailyService;

    @PostMapping("{day}")
    public R createStatisticsByDate(@PathVariable String day) {
        dailyService.createStatisticsByDay(day);
        return R.ok();
    }

    @GetMapping("showChart/{begin}/{end}/{type}")
    public R showChart(@PathVariable String begin,
                       @PathVariable String end,
                       @PathVariable String type){
        Map<String,Object> map = dailyService.getChartData(type,begin,end);
        return R.ok().data(map);
    }
}

