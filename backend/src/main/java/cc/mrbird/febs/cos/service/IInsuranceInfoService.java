package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.InsuranceInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IInsuranceInfoService extends IService<InsuranceInfo> {

    // 分页获取投保信息
    IPage<LinkedHashMap<String, Object>> insuranceInfoByPage(Page page, InsuranceInfo insuranceInfo);
}
