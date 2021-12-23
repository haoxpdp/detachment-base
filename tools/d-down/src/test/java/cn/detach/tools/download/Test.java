package cn.detach.tools.download;

import cn.detach.tools.download.queue.DownTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @author haoxp
 */
public class Test {

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        // 模拟开始下载
        FutureTask<String> futureTask = new FutureTask<>(new DownTask(""));
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(futureTask);
        service.shutdown();

        // 模拟取消暂停事件
        boolean terminate = false;
        while (!terminate) if ((System.currentTimeMillis() - start) / 1000 > 2) {
            System.out.println("cancel");
            futureTask.cancel(true);
            terminate = true;
        }
    }

}
