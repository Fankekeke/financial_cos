package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.Fund;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface FundMapper extends BaseMapper<Fund> {

    // 分页查询基金
    IPage<LinkedHashMap<String, Object>> getConsumeIncomePage(Page page, @Param("fund") Fund fund);
}
