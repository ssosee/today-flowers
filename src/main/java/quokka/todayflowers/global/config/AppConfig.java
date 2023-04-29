package quokka.todayflowers.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import quokka.todayflowers.global.convert.SimpleConvert;

@Configuration
public class AppConfig {
    @Bean
    public SimpleConvert simpleConvert() {
        return new SimpleConvert();
    }
}
