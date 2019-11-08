package cn.detachment.core.example.download;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author haoxp
 */
@Component
public class DownManager implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public void addDownTask(){

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
