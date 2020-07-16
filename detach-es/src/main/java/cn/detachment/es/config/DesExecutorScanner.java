package cn.detachment.es.config;

import cn.detachment.es.exception.ScanClassException;
import cn.detachment.es.executor.DesExecutor;
import cn.detachment.es.factory.DesExecutorFactoryBean;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/5/30 23:06
 */
public class DesExecutorScanner {

    private static Logger logger = LoggerFactory.getLogger(DesExecutorScanner.class);

    @Setter
    private String[] scanPackages;

    /**
     * 直接注入 esclient 注入失败
     */
    @Getter
    @Setter
    private String esClientName;

    @Getter
    @Setter
    private Set<Class<?>> scanClasses;

    private ResourcePatternResolver resolver;

    private MetadataReaderFactory metadataReaderFactory;

    @Getter
    @Setter
    private BeanDefinitionRegistry registry;

    public DesExecutorScanner(String[] scanPackages) {
        this.scanPackages = scanPackages;
    }

    public DesExecutorScanner(String[] scanPackages, ResourcePatternResolver resourcePatternResolver) {
        this.scanPackages = scanPackages;
        this.resolver = resourcePatternResolver;
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resolver);
    }

    public void setResourcePatternResolver(ResourcePatternResolver resourcePatternResolver) {
        this.resolver = resourcePatternResolver;
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resolver);
    }


    public Set<Class<?>> getScanClass() {
        if (scanClasses == null) {
            return doScan();
        }
        return scanClasses;
    }


    /**
     * doScan
     * scan des executor class
     *
     * @return java.util.Set<java.lang.Class < ?>>
     * @author haoxp
     * @date 20/5/30 23:38
     */
    public Set<Class<?>> doScan() {
        Set<Class<?>> classes = new HashSet<>();
        Assert.notEmpty(scanPackages, "scan packages must not empty!");
        for (String path : scanPackages) {
            String scanPackage = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    .concat(ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(path)))
                    .concat("/**/*.class");
            Resource[] resources;
            try {
                resources = resolver.getResources(scanPackage);
            } catch (IOException e) {
                throw new ScanClassException("scan package error" + e.getMessage());
            }
            MetadataReader metadataReader;
            for (Resource r : resources) {
                if (r.isReadable()) {
                    try {
                        metadataReader = metadataReaderFactory.getMetadataReader(r);
                    } catch (IOException e) {
                        throw new ScanClassException("read meta data data error", e);
                    }
                    try {
                        Class<?> api = Class.forName(metadataReader.getClassMetadata().getClassName());
                        if (DesExecutor.class.isAssignableFrom(api)) {
                            classes.add(api);
                        }
                    } catch (ClassNotFoundException e) {
                        throw new ScanClassException(e);
                    }
                }
            }
        }
        this.scanClasses = classes;
        return scanClasses;
    }

    public void scan() {
        doScan();
        for (Class<?> api : this.scanClasses) {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(api);
            GenericBeanDefinition bd = (GenericBeanDefinition) beanDefinitionBuilder.getBeanDefinition();
            bd.setBeanClass(DesExecutorFactoryBean.class);
            bd.setBeanClassName(DesExecutorFactoryBean.class.getName());
            bd.getPropertyValues().addPropertyValue("esClient", new RuntimeBeanReference(esClientName));
            bd.getPropertyValues().addPropertyValue("esAdapter", new RuntimeBeanReference("esAdapter"));
            bd.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            bd.setLazyInit(true);
            bd.getConstructorArgumentValues().addGenericArgumentValue(api);
            registry.registerBeanDefinition(api.getSimpleName(), bd);
        }
    }

}
