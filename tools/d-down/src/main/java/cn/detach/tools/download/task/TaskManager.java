package cn.detach.tools.download.task;

import java.util.List;

/**
 * @author haoxp
 */
public interface TaskManager {

    void start(String taskNo);

    void pause(String taskNo);

    void delete(String taskNo);

    void deleteWithOriginFile(String taskNo);

    List<TaskInfo> getTaskList(String state);

    TaskInfo searchTask(String taskName, String startTime, String endTime);


}
