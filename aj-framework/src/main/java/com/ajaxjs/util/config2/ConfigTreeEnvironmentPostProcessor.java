//package com.ajaxjs.util.config2;
//
//import static com.ajaxjs.util.config2.ConfigurationUtils.addConfigPropPrefix;
//import static com.ajaxjs.util.config2.ConfigurationUtils.findAllKeyAndFilesInConfigTree;
//
//import java.io.IOException;
//import java.nio.file.Path;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import org.springframework.core.env.ConfigurableEnvironment;
//import org.springframework.core.env.MutablePropertySources;
//import org.springframework.core.env.PropertySource;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.support.SpringFactoriesLoader;
//
//public class ConfigTreeEnvironmentPostProcessor {
//	/**
//	 * Config file template as PropertySource name
//	 */
//	public static final String ADDITIONAL_PROPERTY_TEMPLATE = "Config resource 'file [%s]' via configTree";
//
//	@Override
//	public void postProcessEnvironment(ConfigurableEnvironment env, SpringApplication application) {
//		List<PropertySourceLoader> loaders = SpringFactoriesLoader.loadFactories(PropertySourceLoader.class, getClass().getClassLoader());
//		MutablePropertySources propertySources = env.getPropertySources();
//		Optional<PropertySource<?>> configTreeProp = propertySources.stream().filter(p -> p instanceof ConfigTreePropertySource).findFirst();
//
//		if (configTreeProp.isPresent()) {
//			ConfigTreePropertySource configTreeSource = (ConfigTreePropertySource) configTreeProp.get();
//			Path source = configTreeSource.getSource();
//			Map<String, Path> allPropertyFiles = findAllKeyAndFilesInConfigTree(source);
//
//			if (allPropertyFiles.size() == 0) {
//				log.debug("no loadable extension in config tree files, won't append additional property sources");
//				return;
//			}
//
//			try {
//				for (Map.Entry<String, Path> entry : allPropertyFiles.entrySet()) {
//					String prefix = entry.getKey();
//					Path path = entry.getValue();
//					String extension = ConfigurationUtils.getFileExtension(path.toString());
//
//					for (PropertySourceLoader loader : loaders) {
//						if (Arrays.asList(loader.getFileExtensions()).contains(extension)) {
//							FileSystemResource resource = new FileSystemResource(path);
//							String propertySourceName = String.format(ADDITIONAL_PROPERTY_TEMPLATE, path);
//							List<PropertySource<?>> newPropsList = loader.load(propertySourceName, resource);
//
//							if (newPropsList.size() > 0) {
//								PropertySource<?> ps = addConfigPropPrefix((OriginTrackedMapPropertySource) newPropsList.get(0), prefix);
//								propertySources.addLast(ps);
//							}
//							break;
//						}
//					}
//				}
//			} catch (IOException ex) {
//				log.error("can not load additional property sources in config tree", ex);
//			}
//		} else
//			log.debug("no ConfigTreePropertySource found, skip environment post processor");
//	}
//}
