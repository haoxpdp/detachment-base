package cn.detachment.gateway.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * @author haoxp
 */
public abstract class AbstractRouteStorage implements RouteStorage, RouteDefinitionRepository, ApplicationEventPublisherAware, InitializingBean {


    protected ApplicationEventPublisher eventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        load();
    }

    public void refreshRegisterRoutes() {
        eventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

    /**
     * 加载配置文件
     */
    protected abstract void load();
}
