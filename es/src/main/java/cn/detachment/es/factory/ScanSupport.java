package cn.detachment.es.factory;

import lombok.Getter;
import lombok.Setter;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/5/30 23:47
 */
public class ScanSupport {

    @Getter
    @Setter
    protected RestHighLevelClient esClient;


}
