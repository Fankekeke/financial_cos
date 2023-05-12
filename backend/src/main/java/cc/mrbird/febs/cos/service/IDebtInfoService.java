package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.DebtInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IDebtInfoService extends IService<DebtInfo> {

    IPage<LinkedHashMap<String, Object>> debtInfoByPage(Page page, DebtInfo debtInfo);
}
