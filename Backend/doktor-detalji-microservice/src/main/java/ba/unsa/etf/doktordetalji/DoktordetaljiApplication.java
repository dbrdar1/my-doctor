package ba.unsa.etf.doktordetalji;

import ba.unsa.etf.doktordetalji.interceptors.HTTPHandlerInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@EnableEurekaClient
@SpringBootApplication
@Configuration
public class DoktordetaljiApplication {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public HTTPHandlerInterceptor httpHandlerInterceptor() {
        return new HTTPHandlerInterceptor();
    }

    @Bean
    public WebMvcConfigurer adapter() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(httpHandlerInterceptor());
            }
        };
    }


    public static void main(String[] args) {
        SpringApplication.run(DoktordetaljiApplication.class, args);
    }

}
