package cn.detach.tools.download.queue;

import cn.detach.tools.download.task.TaskInfo;
import cn.detach.tools.download.utils.DSerialize;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.awt.SystemColor.info;

/**
 * @author haoxp
 */
public class DownTask implements Callable<String> {

    private ExecutorService downThreadPool;

    private TaskInfo taskInfo;

    private String infoPath;

    private DownTask(TaskInfo info) {
        downThreadPool = Executors.newFixedThreadPool(info.getThreadCount());
        taskInfo = info;
        // TODO: 2021/12/23 tmp info path
        infoPath = "";
    }

    public DownTask(String infoPath) throws Exception {
        this.infoPath = infoPath;
        File file = new File(infoPath);
        new DownTask((TaskInfo) DSerialize.deSerialize(new File(infoPath)));
    }


    @Override
    public String call() throws Exception {
        Exception e = null;
        // 解析taskInfo


        int i = 0;
        while (i < 10) try {

            System.out.println(Thread.currentThread().getName() + i);
            Thread.sleep(1000);
            i++;
        } catch (InterruptedException ex) {
            // TODO: 2021/12/23 save task info
            DSerialize.serialize(info, new File(infoPath));
        }

        DSerialize.serialize(info, new File(infoPath));
        return "success";
    }


}
