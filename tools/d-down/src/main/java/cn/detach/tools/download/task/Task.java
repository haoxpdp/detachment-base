package cn.detach.tools.download.task;

/**
 * @author haoxp
 */
public interface Task {

    String tmpFile = ".d-down.tmp";

    void start();

    void pause();

    void finished();

    void delete();

    void deleteWithFile();

    void saveDownInfo();

}
