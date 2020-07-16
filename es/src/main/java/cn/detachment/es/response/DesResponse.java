package cn.detachment.es.response;

import lombok.Data;
import org.elasticsearch.action.search.SearchResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/7/15 20:29
 */
@Data
public class DesResponse<T> {
    protected SearchResponse searchResponse;

    protected List<T> dataList;

    protected BigDecimal sum, count, avg, min, max;

    protected Map<String,Object> aggregationResult;
}
