package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.AssetInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IAssetInfoService extends IService<AssetInfo> {

    // 分页获取固定资产
    IPage<LinkedHashMap<String, Object>> assetInfoByPage(Page page, AssetInfo assetInfo);
}
