//package com.ajaxjs.scheduled.service;
//
//
//import org.springframework.beans.factory.support.BeanDefinitionRegistry;
//import org.springframework.context.EnvironmentAware;
//import org.springframework.core.annotation.AnnotationUtils;
//import org.springframework.core.annotation.MergedAnnotations;
//import org.springframework.core.env.Environment;
//import org.springframework.core.type.AnnotationMetadata;
//import org.springframework.core.type.MethodMetadata;
//import org.springframework.util.StringUtils;
//
//import java.util.Map;
//import java.util.Set;
//
///**
// * @Author: volicy.xu
// * @Date: 2021/9/19
// */
//public class ScheduledRegistrar extends MapperScannerRegistrar implements  EnvironmentAware {
//    private static final String REF = "mybatis.sql.session.ref";
//
//    private static final String BASE_PACKAGE_NAME = "org.volicy.scheduled.dao";
//
//    private Environment environment;
//
//    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
//        super.registerBeanDefinitions(new ScheduledMapperMetadta(), registry);
//    }
//
//
//    @Override
//    public void setEnvironment(Environment environment) {
//        this.environment = environment;
//    }
//
//
//    @MapperScan(basePackages = {BASE_PACKAGE_NAME})
//    public class ScheduledMapperMetadta implements AnnotationMetadata {
//        @Override
//        public MergedAnnotations getAnnotations() {
//            return null;
//        }
//
//        @Override
//        public Map<String, Object> getAnnotationAttributes(String annotationName) {
//            Map<String, Object> map = AnnotationUtils
//                    .getAnnotationAttributes(AnnotationUtils.findAnnotation(ScheduledMapperMetadta.class, MapperScan.class));
//            if (StringUtils.hasText(environment.getProperty(REF))) {
//                map.put("sqlSessionTemplateRef", environment.getProperty(REF));
//            }
//            return map;
//        }
//
//
//        @Override
//        public Set<MethodMetadata> getAnnotatedMethods(String annotationName) {
//            return null;
//        }
//
//        @Override
//        public String getClassName() {
//            return null;
//        }
//
//        @Override
//        public boolean isInterface() {
//            return false;
//        }
//
//        @Override
//        public boolean isAnnotation() {
//            return false;
//        }
//
//        @Override
//        public boolean isAbstract() {
//            return false;
//        }
//
//        @Override
//        public boolean isFinal() {
//            return false;
//        }
//
//        @Override
//        public boolean isIndependent() {
//            return false;
//        }
//
//        @Override
//        public String getEnclosingClassName() {
//            return null;
//        }
//
//        @Override
//        public String getSuperClassName() {
//            return null;
//        }
//
//        @Override
//        public String[] getInterfaceNames() {
//            return new String[0];
//        }
//
//        @Override
//        public String[] getMemberClassNames() {
//            return new String[0];
//        }
//    }
//}
