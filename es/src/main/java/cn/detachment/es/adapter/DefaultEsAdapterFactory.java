package cn.detachment.es.adapter;

import cn.detachment.es.support.EsClientSupport;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author haoxp
 */
public class DefaultEsAdapterFactory extends EsClientSupport implements FactoryBean<EsAdapter> {
    @Override
    public EsAdapter getObject() throws Exception {
        DefaultEsAdapter esAdapter = new DefaultEsAdapter();
        esAdapter.setEsClient(this.getEsClient());

        return esAdapter;
    }

    @Override
    public Class<?> getObjectType() {
        return EsAdapter.class;
    }
}
