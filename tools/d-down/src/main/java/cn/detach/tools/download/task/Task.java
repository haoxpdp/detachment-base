package cn.detach.tools.download.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author haoxp
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Task {

    private String taskName;

    private Integer taskNo;

    private Integer order;

    private String progress;

    private Long totalSize;

    private Long currentSize;

    private String location;

}
