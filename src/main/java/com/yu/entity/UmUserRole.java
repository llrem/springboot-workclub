package com.yu.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author llrem
 * @since 2022-04-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UmUserRole implements Serializable {

    private static final long serialVersionUID=1L;

    private Long id;

    private Long userId;

    private String role;


}
