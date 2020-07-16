package cn.detachment.es.support;

import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/5/30 23:47
 */
public class EsClientSupport {

    protected RestHighLevelClient esClient;

    public RestHighLevelClient getEsClient() {
        return esClient;
    }

    public void setEsClient(RestHighLevelClient esClient) {
        this.esClient = esClient;
    }
}
