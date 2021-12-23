package cn.detach.tools.download.task;

import cn.detach.tools.download.exception.TaskInitException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author haoxp
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskInfo implements Serializable {

    private static final long serialVersionUID = -7133378543625237289L;

    private String taskNo;

    private String taskName;

    private String originUrl;

    private String progress;

    private Long totalSize;

    private Long downloadSize;

    private String etag;

    private String fileMd5;

    private Long fileSize;

    private String location;

    private String state;

    private Date startTime;

    private Date endTime;

    private Integer threadCount = 4;

    public void setThreadCount(Integer threadCount) {
        if (threadCount > 50) throw new TaskInitException("设置线程数过多");
        this.threadCount = threadCount;
    }

    // todo
    private String proxy;

    private Integer order;


}
