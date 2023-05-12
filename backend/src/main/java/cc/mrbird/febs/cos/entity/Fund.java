package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Fund implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 基金名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 基金编号
     */
    @TableField("CODE")
    private String code;

    /**
     * 净值
     */
    @TableField("WORTH")
    private BigDecimal worth;

    /**
     * 增值
     */
    @TableField("RECENT")
    private BigDecimal recent;

    /**
     * 基金类型
     */
    @TableField("TYPE")
    private Integer type;

    /**
     * 风险
     */
    @TableField("RISK")
    private Integer risk;

    /**
     * 标签
     */
    @TableField("TAG")
    private String tag;

    @TableField("SHARE")
    private String share;

    @TableField("USER_ID")
    private Long userId;

    /**
     * 删除标识
     */
    @TableLogic
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;


}
