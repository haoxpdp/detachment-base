package cn.detachment.zk.executor;

import lombok.*;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

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
public class DataMonitor {

    private ZooKeeper zk;
    private String zNode;
    private Watcher chainedWatcher;

}
