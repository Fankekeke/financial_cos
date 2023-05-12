package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保险信息
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class InsuranceInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 保险编号
     */
    private String code;

    /**
     * 保险名称
     */
    private String name;

    /**
     * 保险类型 1.人身保险 2.养老保险 3.车险
     */
    private Integer type;

    /**
     * 投保人
     */
    private Long policyholder;

    /**
     * 投保金额
     */
    private BigDecimal price;

    /**
     * 备注
     */
    private String content;

    /**
     * 创建时间
     */
    private String createDate;

    private String insurancePeriod;

}
