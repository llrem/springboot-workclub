package com.yu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author llrem
 * @since 2022-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PmTaskUndone implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long projectId;

    private LocalDate date;

    /**
     * 项目中进行中和未完成任务的数量
     */
    private Integer number;


}
