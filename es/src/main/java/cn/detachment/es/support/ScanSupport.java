package cn.detachment.es.support;

import org.elasticsearch.client.RestHighLevelClient;

import java.util.Set;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/5/30 23:47
 */
public abstract class ScanSupport {


    protected RestHighLevelClient client;

    /**
     * set restClient
     *
     * @param client
     */
    public void setRestHighLevelClient(RestHighLevelClient client) {
        this.client = client;
    }


}
