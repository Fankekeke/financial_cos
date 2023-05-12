package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.AssetInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface AssetInfoMapper extends BaseMapper<AssetInfo> {

    // 分页获取固定资产
    IPage<LinkedHashMap<String, Object>> assetInfoByPage(Page page, @Param("assetInfo") AssetInfo assetInfo);
}
