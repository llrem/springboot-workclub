package com.yu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class FileListParam {
    List<FileParam> folderList;
    List<FileParam> fileList;
}
