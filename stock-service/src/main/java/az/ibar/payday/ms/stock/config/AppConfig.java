package az.ibar.payday.ms.stock.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    private final InterceptorConfig interceptorConfig;

    public AppConfig(InterceptorConfig interceptorConfig) {
        this.interceptorConfig = interceptorConfig;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptorConfig);
    }
}
