package cn.detachment.gateway.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * @author haoxp
 */
public interface RouteStorage extends RouteDefinitionRepository, ApplicationEventPublisherAware, InitializingBean {
}
