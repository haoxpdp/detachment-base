package cn.detachment.es.support;

import cn.detachment.es.adapter.EsAdapter;

/**
 * @author haoxp
 */
public class EsAdapterSupport extends EsClientSupport {

    protected EsAdapter esAdapter;

    public EsAdapter getEsAdapter() {
        return esAdapter;
    }

    public void setEsAdapter(EsAdapter esAdapter) {
        this.esAdapter = esAdapter;
    }

}
