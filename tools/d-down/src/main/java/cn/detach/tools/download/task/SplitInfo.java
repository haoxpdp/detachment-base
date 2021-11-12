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
public class SplitInfo {

    private String taskNo;

    private String taskName;

    private String originUrl;

    private int splitNo;

    private long startPos, endPos;
}
