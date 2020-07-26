package cn.detachment.zk.executor;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/7/26 22:44
 */

import lombok.*;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.io.FileOutputStream;
import java.io.IOException;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Executor implements Watcher, Runnable, DataMonitorListener {


    @Override
    public void run() {

    }

    @Override
    public void process(WatchedEvent watchedEvent) {

    }

    @Override
    public void exists(byte[] data) {

    }

    @Override
    public void closing(int rc) {
        synchronized (this) {
            notifyAll();
        }
    }
}
