package cn.detach.tools.download.queue;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author haoxp
 */
public class DownThread implements Callable<String> {

    public static void main(String[] args) {
        FutureTask<String> futureTask = new FutureTask<>(new DownThread());
        System.out.println(futureTask.isCancelled());
        System.out.println(futureTask.isDone());
    }

    @Override
    public String call() throws Exception {
        return "hello";
    }


}
