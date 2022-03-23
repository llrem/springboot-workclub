package com.yu.dto;

import com.yu.entity.TmTag;
import com.yu.entity.UmUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class TaskInfoParam {

    private String description;

    private UmUser executor;

    private LocalDateTime startDate;

    private LocalDateTime dueDate;

    private Integer priority;

    private Integer type;

    private List<TmTag> tags;

    private Integer status;
}
