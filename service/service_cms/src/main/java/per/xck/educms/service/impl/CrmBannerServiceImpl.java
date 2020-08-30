package per.xck.educms.service.impl;

import per.xck.educms.entity.CrmBanner;
import per.xck.educms.mapper.CrmBannerMapper;
import per.xck.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author kazemi
 * @since 2020-08-09
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    @Override
//    @Cacheable(key = "'selectIndexList'",value = "banner")
    public List<CrmBanner> selectAllBanner() {
        List<CrmBanner> list = baseMapper.selectList(null);
        return list;
    }
}
