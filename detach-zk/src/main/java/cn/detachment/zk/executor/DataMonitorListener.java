package cn.detachment.zk.executor;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/7/26 22:51
 */
public interface DataMonitorListener {


    /**
     * exists
     * The existence status of the node has changed.
     *
     * @param data data
     * @return void
     * @author haoxp
     * @date 20/7/26 22:53
     */
    void exists(byte[] data);

    /**
     * The ZooKeeper session is no longer valid.
     *
     * @param rc the ZooKeeper reason code
     */
    void closing(int rc);
}
