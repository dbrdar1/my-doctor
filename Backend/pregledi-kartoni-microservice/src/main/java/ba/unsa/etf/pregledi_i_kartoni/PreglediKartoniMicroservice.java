package ba.unsa.etf.pregledi_i_kartoni;

import ba.unsa.etf.pregledi_i_kartoni.interceptors.HTTPHandlerInterceptor;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableEurekaClient
@SpringBootApplication
@Configuration
public class PreglediKartoniMicroservice {

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


    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        return builder.build();

    }


    public static void main(String[] args) {
        SpringApplication.run(PreglediKartoniMicroservice.class, args);
    }

}
