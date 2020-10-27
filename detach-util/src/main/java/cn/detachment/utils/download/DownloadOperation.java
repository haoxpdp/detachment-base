package cn.detachment.utils.download;

/**
 * @author haoxp
 * @date 2020-10-13
 */
public interface DownloadOperation {

    /**
     * pause
     */
    void pause();

    /**
     * start
     */
    void start();

    /**
     * cancel
     */
    void cancel();
}
