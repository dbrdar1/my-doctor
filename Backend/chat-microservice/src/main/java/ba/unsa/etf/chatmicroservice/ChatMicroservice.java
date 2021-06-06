package ba.unsa.etf.chatmicroservice;

import ba.unsa.etf.chatmicroservice.interceptor.HTTPHandlerInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableScheduling
public class ChatMicroservice {

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
        SpringApplication.run(ChatMicroservice.class, args);
    }

}
