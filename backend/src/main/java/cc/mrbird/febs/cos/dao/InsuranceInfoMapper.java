package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.InsuranceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface InsuranceInfoMapper extends BaseMapper<InsuranceInfo> {

    // 分页获取投保信息
    IPage<LinkedHashMap<String, Object>> insuranceInfoByPage(Page page, @Param("insuranceInfo") InsuranceInfo insuranceInfo);
}
