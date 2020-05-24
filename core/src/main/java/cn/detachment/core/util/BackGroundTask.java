package cn.detachment.core.util;

import java.util.concurrent.*;

/**
 * 后台线程处理
 *
 * @author haoxp
 * @version v1.0
 * @date 20/5/25 5:58
 */
public abstract class BackGroundTask<V> implements Runnable, Future<V> {

    private final FutureTask<V> computation = new Computation();

    // 显示线程
    private ExecutorService executorService;

    public <V> BackGroundTask(ExecutorService executorService) {
        this.executorService = executorService;
    }

    private class Computation extends FutureTask<V> {
        public Computation() {
            super(BackGroundTask.this::compute);
        }
    }

    @Override
    public void run() {

    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    /**
     * compute
     * 取消线程
     *
     * @return V
     * @throws Exception 捕获异常
     * @author haoxp
     * @date 20/5/25 6:03
     */
    protected abstract V compute() throws Exception;


    protected void setProgress(int current, int max) {
        executorService.execute(() -> onProgress(current, max));
    }

    protected void onProgress(int current, int max) {

    }
}
