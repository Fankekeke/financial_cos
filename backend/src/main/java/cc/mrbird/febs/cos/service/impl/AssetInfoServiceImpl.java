package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.AssetInfo;
import cc.mrbird.febs.cos.dao.AssetInfoMapper;
import cc.mrbird.febs.cos.service.IAssetInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class AssetInfoServiceImpl extends ServiceImpl<AssetInfoMapper, AssetInfo> implements IAssetInfoService {

    @Override
    public IPage<LinkedHashMap<String, Object>> assetInfoByPage(Page page, AssetInfo assetInfo) {
        return baseMapper.assetInfoByPage(page, assetInfo);
    }
}
