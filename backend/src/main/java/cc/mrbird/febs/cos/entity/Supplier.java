package cc.mrbird.febs.cos.entity;

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
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键·
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 对象名称
     */
    @TableField("BEAU")
    private String beau;

    /**
     * 支出 收入
     */
    @TableField("BELONG")
    private Integer belong;

    /**
     * 删除标识
     */
    @TableLogic
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;


}
