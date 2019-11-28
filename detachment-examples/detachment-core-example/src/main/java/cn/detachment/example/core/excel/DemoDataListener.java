package cn.detachment.example.core.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class DemoDataListener extends AnalysisEventListener<DemoData> {

    private static final Logger logger = LoggerFactory.getLogger(DemoDataListener.class);

    private Long readCnt = 0L;

    @Override
    public void invoke(DemoData data, AnalysisContext context) {
        readCnt++;
        logger.info("解析 ： {}",data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        logger.info("解析完毕");
    }
}
