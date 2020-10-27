package cn.detachment.utils.download;

/**
 * @author haoxp
 * @date 20/10/13
 */
public interface DownloadListener {

    /**
     * return percent
     * @param progress
     */
    void getProgress(int progress);

    /**
     * complete download
     */
    void onComplete();

    /**
     * download failed
     */
    void onFailure();

}
