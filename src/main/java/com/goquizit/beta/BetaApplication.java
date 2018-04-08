package com.goquizit.beta;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BetaApplication {

	public static void main(final String[] args) {
		SpringApplication.run(BetaApplication.class, args);
	}

	@Bean
	public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
		return new BeanFactoryPostProcessor() {

			@Override
			public void postProcessBeanFactory(
					final ConfigurableListableBeanFactory beanFactory) throws BeansException {
				final BeanDefinition bean = beanFactory.getBeanDefinition(
						DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);

				bean.getPropertyValues().add("loadOnStartup", 1);
			}
		};
	}
}
