package cn.detachment.es.support;

import lombok.Getter;
import lombok.Setter;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.Set;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/5/30 23:47
 */
public abstract class ScanSupport {

    @Getter
    @Setter
    protected RestHighLevelClient esClient;


}
