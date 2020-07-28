package cn.detach.api.annoation;

import cn.detach.api.factory.RemoteApiFactoryBean;
import cn.detach.api.support.DefaultHttpApiSupport;
import cn.detach.api.support.HttpUtilApi;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.util.*;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/7/28 11:33
 */
@SuppressWarnings("unused")
public class RemoteApiImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    private static final Logger logger = LoggerFactory.getLogger(RemoteApiImportBeanDefinitionRegistrar.class);

    private ResourceLoader resourceLoader;

    private ResourcePatternResolver resolver;

    private MetadataReaderFactory metadataReaderFactory;


    private HttpUtilApi httpApiSupport;

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resolver);

    }

    @SneakyThrows
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        StandardAnnotationMetadata annotationMetadata = (StandardAnnotationMetadata) importingClassMetadata;
        Package basePackage = annotationMetadata.getIntrospectedClass().getPackage();
        System.out.println(basePackage.getClass());
        Set<Class<?>> classes = getRemoteApiClasses(Objects.requireNonNull(AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(RemoteApiScanner.class.getName()))));
        httpApiSupport = new DefaultHttpApiSupport();
        registerRemoteApi(classes, registry);
    }

    private Set<Class<?>> getRemoteApiClasses(@NonNull AnnotationAttributes annotationAttributes) throws IOException, ClassNotFoundException {

        Set<String> scanPackages = new HashSet<>(Arrays.asList(annotationAttributes.getStringArray("basePackages")));
        Set<Class<?>> classes = new HashSet<>(Arrays.asList(annotationAttributes.getClassArray("scanClasses")));
        classes.addAll(scanPackages(scanPackages));
        if (CollectionUtils.isEmpty(classes)) {
            logger.warn("can't find any remote api.");
        }
        return classes;
    }

    private void registerRemoteApi(Set<Class<?>> classes, BeanDefinitionRegistry registry) {
        for (Class<?> api : classes) {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(api);
            GenericBeanDefinition bd = (GenericBeanDefinition) beanDefinitionBuilder.getBeanDefinition();
            bd.setBeanClass(RemoteApiFactoryBean.class);
            bd.setBeanClassName(RemoteApiFactoryBean.class.getName());
            bd.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            bd.setLazyInit(true);
            bd.getConstructorArgumentValues().addGenericArgumentValue(api);
            bd.getPropertyValues().addPropertyValue("apiSupport", httpApiSupport);
            registry.registerBeanDefinition(getRemoteApiBeanName(api), bd);
        }
    }

    private String getRemoteApiBeanName(Class<?> api) {
        ApiSupport apiSupport = api.getAnnotation(ApiSupport.class);
        if (StringUtils.isEmpty(apiSupport.value())) {
            return api.getSimpleName();
        }
        return apiSupport.value();
    }

    private Set<Class<?>> scanPackages(Collection<String> packages) throws IOException, ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<>();
        if (CollectionUtils.isEmpty(packages)) {
            return classes;
        }
        for (String path : packages) {
            String scanPackage = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    .concat(ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(path)))
                    .concat("/**/*.class");
            Resource[] resources = resolver.getResources(scanPackage);
            MetadataReader metadataReader;
            for (Resource r : resources) {
                if (r.isReadable()) {
                    metadataReader = metadataReaderFactory.getMetadataReader(r);
                    Class<?> api = Class.forName(metadataReader.getClassMetadata().getClassName());
                    if (api.getAnnotation(ApiSupport.class) != null) {
                        classes.add(api);
                    }
                }
            }

        }
        return classes;
    }

    private void verifyClasses(Set<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            if (clazz.getAnnotation(ApiSupport.class) == null) {
                throw new BeanCreationException("remote api must have ApiSupport annotation");
            }
        }
    }

}
