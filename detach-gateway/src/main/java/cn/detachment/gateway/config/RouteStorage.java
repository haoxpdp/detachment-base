package cn.detachment.gateway.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisherAware;

import java.util.ArrayList;
import java.util.List;

/**
 * @author haoxp
 */
public interface RouteStorage extends RouteDefinitionRepository, ApplicationEventPublisherAware, InitializingBean {

    List<RouteDefinition> routeDefinitions = new ArrayList<>();

}
