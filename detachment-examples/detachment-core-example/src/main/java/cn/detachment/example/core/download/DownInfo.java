package cn.detachment.example.core.download;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author haoxp
 */
@Data
public class DownInfo {
    private String name;
    private String url;
    private String desc;
    private String saveDir;
    private Long currentSize = 0L;
    private Long totalSize;
    private LocalDateTime startTime = LocalDateTime.now();
    private LocalDateTime endTime;

}
