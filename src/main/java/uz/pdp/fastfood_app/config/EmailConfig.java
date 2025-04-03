package uz.pdp.fastfood_app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

@Configuration
public class
EmailConfig {
    @Value("${spring.mail.username}")
    private String myEmail;
    @Value("${spring.mail.password}")
    private String myPassword;
    @Value("${spring.mail.host}")
    private String myHost;
    @Value("${spring.mail.port}")
    private Integer myPort;
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(myHost);
        mailSender.setPort(myPort);
        mailSender.setUsername(myEmail);
        mailSender.setPassword(myPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true"); // Optional, for debugging purposes

        return mailSender;
    }

}
