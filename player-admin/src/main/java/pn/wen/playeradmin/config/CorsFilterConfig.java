package pn.wen.playeradmin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsFilterConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许的前端域名
        config.addAllowedOrigin("https://flyerhome.github.io");
        // 允许的请求方法
        config.addAllowedMethod("GET");
        config.addAllowedMethod("HEAD");
        // 允许的请求头
        config.addAllowedHeader("*");
        // 缓存时间
        config.setMaxAge(3600L);

        // 配置路径匹配规则：所有路径（包含静态资源）
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
