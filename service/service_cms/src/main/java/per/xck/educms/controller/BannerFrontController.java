package per.xck.educms.controller;


import per.xck.commonutils.R;
import per.xck.educms.entity.CrmBanner;
import per.xck.educms.service.CrmBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author kazemi
 * @since 2020-08-09
 */
@RestController
@RequestMapping("/educms/bannerfront")
@CrossOrigin
@Api(description = "网站首页Banner列表")
public class BannerFrontController {

    @Autowired
    private CrmBannerService crmBannerService;

    @ApiOperation(value = "获取首页banner")
    @GetMapping("getAllBanner")
    public R getAllBanner() {
        List<CrmBanner> list = crmBannerService.selectAllBanner();
        return R.ok().data("bannerList", list);
    }

}

