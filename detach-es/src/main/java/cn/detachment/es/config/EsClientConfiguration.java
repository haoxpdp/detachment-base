package cn.detachment.es.config;

import cn.detachment.es.adapter.DefaultEsAdapterFactory;
import cn.detachment.es.adapter.EsAdapter;
import cn.detachment.es.factory.EsClientFactoryBean;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/5/31 22:46
 */
@EnableConfigurationProperties(EsProperties.class)
public class EsClientConfiguration implements ApplicationContextAware,
        BeanDefinitionRegistryPostProcessor {

    private static Logger logger = LoggerFactory.getLogger(EsClientConfiguration.class);

    private String esClientName;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Environment environment = applicationContext.getEnvironment();
        this.esClientName = environment.getProperty("detach-es.es-client-name");

        if (StringUtils.isEmpty(this.esClientName)) {
            esClientName = "esClient";
        }

    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        registerEsClient(registry);
        registerEsAdapter(registry);
    }

    private void registerEsClient(BeanDefinitionRegistry registry) {
        if (registry.isBeanNameInUse(esClientName)) {
            logger.info("{} has already been defined!", esClientName);
            return;
        }
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(RestHighLevelClient.class);
        GenericBeanDefinition bd = (GenericBeanDefinition) beanDefinitionBuilder.getBeanDefinition();
        bd.setBeanClass(EsClientFactoryBean.class);
        bd.setBeanClassName(EsClientFactoryBean.class.getName());
        registry.registerBeanDefinition(esClientName, bd);
    }

    private void registerEsAdapter(BeanDefinitionRegistry registry) {
        if (registry.isBeanNameInUse("esAdapter")) {
            logger.info("{} has already been defined!", esClientName);
            return;
        }
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(EsAdapter.class);
        GenericBeanDefinition bd = (GenericBeanDefinition) beanDefinitionBuilder.getBeanDefinition();
        bd.setBeanClass(DefaultEsAdapterFactory.class);
        bd.setBeanClassName(DefaultEsAdapterFactory.class.getName());

        registry.registerBeanDefinition("esAdapter", bd);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
