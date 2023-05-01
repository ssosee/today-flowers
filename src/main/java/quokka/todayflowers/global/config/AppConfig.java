package quokka.todayflowers.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.SpringWebFluxTemplateEngine;
import quokka.todayflowers.global.convert.SimpleConvert;

@Configuration
public class AppConfig {
    @Bean
    public SimpleConvert simpleConvert() {
        return new SimpleConvert();
    }
}
