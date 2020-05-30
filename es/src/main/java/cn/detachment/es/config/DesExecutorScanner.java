package cn.detachment.es.config;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
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

    @Getter
    @Setter
    private Set<Class<?>> scanClasses;

    @Getter
    private ResourcePatternResolver resolver;

    private MetadataReaderFactory metadataReaderFactory;

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

    public void doScan() {
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
                logger.error("scan package io error " + e.getMessage(), e);
                throw new BeanDefinitionValidationException("scan package error" + e.getMessage());
            }
            MetadataReader metadataReader;
            for (Resource r : resources) {
                if (r.isReadable()) {
                    try {
                        metadataReader = metadataReaderFactory.getMetadataReader(r);
                    } catch (IOException e) {
                        logger.error(e.getMessage(), e);
                        throw new BeanDefinitionValidationException("read meta data data error", e);
                    }


                }
            }
        }
    }

}
