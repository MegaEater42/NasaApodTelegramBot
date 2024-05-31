package ru.megaeater42.config;

import com.netflix.discovery.AbstractDiscoveryClientOptionalArgs;
import com.netflix.discovery.shared.transport.jersey.TransportClientFactories;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.cloud.netflix.eureka.http.EurekaClientHttpRequestFactorySupplier;
import org.springframework.cloud.netflix.eureka.http.RestTemplateDiscoveryClientOptionalArgs;
import org.springframework.cloud.netflix.eureka.http.RestTemplateTransportClientFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// When using Eureka and telegrambots-spring-starter, an error occurred caused by dependency on the Jersey, so
// using RestTemplate (it's default in Eureka) instead of Jersey anyway
// P.S. Waiting for telegrambots-spring-starter 7.x on Maven repo
@Configuration
public class DiscoveryClientOptionalArgsConfiguration {
    @Bean
    @ConditionalOnClass(name = { "org.springframework.web.client.RestTemplate", "org.glassfish.jersey.client.JerseyClient" })
    @ConditionalOnMissingBean(value = AbstractDiscoveryClientOptionalArgs.class, search = SearchStrategy.CURRENT)
    public RestTemplateDiscoveryClientOptionalArgs restTemplateDiscoveryClientOptionalArgs(EurekaClientHttpRequestFactorySupplier eurekaClientHttpRequestFactorySupplier) {
        return new RestTemplateDiscoveryClientOptionalArgs(eurekaClientHttpRequestFactorySupplier);
    }

    @Bean
    @ConditionalOnClass(name = { "org.springframework.web.client.RestTemplate", "org.glassfish.jersey.client.JerseyClient" })
    @ConditionalOnMissingBean(value = TransportClientFactories.class, search = SearchStrategy.CURRENT)
    public RestTemplateTransportClientFactories restTemplateTransportClientFactories(RestTemplateDiscoveryClientOptionalArgs optionalArgs) {
        return new RestTemplateTransportClientFactories(optionalArgs);
    }
}
