package cn.detach.tools.download.task;

/**
 * @author haoxp
 */
public class DownLoadTask implements Task {


    private TaskInfo taskInfo;

    public DownLoadTask(TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
    }

    @Override
    public void start() {
        // 任务管理器 判断状态是否可以下载，
        // 读取任务信息文件
        // 判断是否可以继续下载
        // 是继续下载，否提示重新下载
    }

    @Override
    public void pause() {
        // save down info

    }

    @Override
    public void finished() {

    }

    @Override
    public void delete() {

    }

    @Override
    public void deleteWithFile() {

    }

    @Override
    public void saveDownInfo() {

    }
}
