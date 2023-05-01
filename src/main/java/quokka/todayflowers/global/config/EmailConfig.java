package quokka.todayflowers.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.context.Context;

@Configuration
public class EmailConfig {
    @Bean
    public Context context() {
        return new Context();
    }
}
