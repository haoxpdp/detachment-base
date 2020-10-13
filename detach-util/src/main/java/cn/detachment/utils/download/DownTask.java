package cn.detachment.utils.download;

/**
 * down load task describe
 *
 * @author haoxp
 * @date 20/10/13
 */
public class DownTask implements DownloadOperation {


    /**
     * 下载的4种状态，init,pause,downloading,finish
     */
    public static final String INIT = "INIT";

    public static final String PAUSE = "PAUSE";

    public static final String DOWNING = "DOWNLOADING";

    public static final String FINISH = "FINISH";

    /**
     * 当前状态
     */
    private volatile String currentState;

    /**
     * 当前下载大小
     */
    private volatile int currentSize;

    /**
     * 下载的线程数
     */
    private volatile int runningThreadCnt;


    private DownloadListener downloadListener;

    public DownTask(DownloadListener listener) {
        this.downloadListener = listener;
        this.currentState = INIT;
    }


    @Override
    public void pause() {

    }

    @Override
    public void start() {

    }

    @Override
    public void cancel() {

    }
}
