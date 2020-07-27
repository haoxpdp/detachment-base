package cn.detachment.zk.executor;

import lombok.*;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/7/26 22:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class DataMonitor implements Watcher, AsyncCallback.StatCallback {

    private static final Logger logger = LoggerFactory.getLogger(DataMonitor.class);

    ZooKeeper zk;
    String zNode;
    Watcher chainedWatcher;
    boolean dead;
    DataMonitorListener listener;
    byte[] prevData;

    @Override
    public void processResult(int rc, String path, Object o, Stat stat) {
        boolean exists;
        logger.info("rc {}", rc);
        switch (rc) {
            case KeeperException.Code.Ok:
                exists = true;
                break;
            case KeeperException.Code.NoNode:
                exists = false;
                break;
            case KeeperException.Code.SessionExpired:
            case KeeperException.Code.NoAuth:
                dead = true;
                listener.closing(rc);
                return;
            default:
                // Retry errors
                zk.exists(zNode, true, this, null);
                return;
        }

        byte b[] = null;
        if (exists) {
            try {
                b = zk.getData(zNode, false, null);
            } catch (KeeperException e) {
                // We don't need to worry about recovering now. The watch
                // callbacks will kick off any exception handling
                e.printStackTrace();
            } catch (InterruptedException e) {
                return;
            }
        }
        if ((b == null && b != getPrevData())
                || (b != null && !Arrays.equals(getPrevData(), b))) {
            listener.exists(b);
            prevData = b;
        }
    }

    @Override
    public void process(WatchedEvent event) {
        String path = event.getPath();
        if (event.getType() == Event.EventType.None) {
            // We are are being told that the state of the
            // connection has changed
            switch (event.getState()) {
                case SyncConnected:
                    // In this particular example we don't need to do anything
                    // here - watches are automatically re-registered with
                    // server and any watches triggered while the client was
                    // disconnected will be delivered (in order of course)
                    break;
                case Expired:
                    // It's all over
                    dead = true;
                    listener.closing(KeeperException.Code.SessionExpired);
                    break;
            }
        } else {
            if (path != null && path.equals(zNode)) {
                // Something has changed on the node, let's find out
                zk.exists(zNode, true, this, null);
            }
        }
        if (chainedWatcher != null) {
            chainedWatcher.process(event);
        }
    }
}
