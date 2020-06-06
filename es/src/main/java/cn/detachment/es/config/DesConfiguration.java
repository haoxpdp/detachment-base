package cn.detachment.es.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.util.StringUtils;

/**
 * @author haoxp
 */
public class DesConfiguration implements BeanDefinitionRegistryPostProcessor, BeanNameAware, ApplicationContextAware, ResourceLoaderAware {

    private static Logger logger = LoggerFactory.getLogger(DesConfiguration.class);


    private ApplicationContext applicationContext;

    private String esClientName;

    private String beanName;

    private String[] scanPackages;

    private boolean processProperty = false;

    private ResourcePatternResolver resolver;


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if (!processProperty) {
            processPropertiesPlaceHolder();
        }
        DesExecutorScanner scanner = new DesExecutorScanner(scanPackages, resolver);
        scanner.setRegistry(registry);
        scanner.setEsClientName(esClientName);
        scanner.scan();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void processPropertiesPlaceHolder() {
        this.esClientName = getEnvironment().getProperty("detach-es.es-client-name");
        if (StringUtils.isEmpty(esClientName)) {
            this.esClientName = "esClientName";
        }
        this.scanPackages = getEnvironment().getProperty("detach-es.scan-packages", String[].class);
        this.processProperty = true;
    }

    public Environment getEnvironment() {
        return applicationContext.getEnvironment();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
    }
}
