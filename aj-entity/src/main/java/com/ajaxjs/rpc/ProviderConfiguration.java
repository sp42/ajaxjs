package com.ajaxjs.rpc;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.ProviderConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDubbo(scanBasePackages = "com.ajaxjs")
public class ProviderConfiguration {
	@Bean // #2
	public ApplicationConfig applicationConfig() {
		ApplicationConfig app = new ApplicationConfig();
		app.setName("dubbo-annotation-provider");

		return app;
	}

	@Bean // #1
	public ProviderConfig providerConfig() {
		ProviderConfig provider = new ProviderConfig();
		provider.setTimeout(1000);

		return provider;
	}

	@Bean // #3
	public RegistryConfig registryConfig() {
		RegistryConfig registry = new RegistryConfig();
		registry.setAddress("N/A"); // 直连

		return registry;
	}

	@Bean // #4
	public ProtocolConfig protocolConfig() {
		ProtocolConfig protocol = new ProtocolConfig();
		protocol.setName("dubbo");
		protocol.setPort(20880);

		return protocol;
	}
}
