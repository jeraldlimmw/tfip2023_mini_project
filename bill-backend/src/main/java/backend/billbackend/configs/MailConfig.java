package backend.billbackend.configs;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
    
    @Value("${spring.mail.host}")
    private String mailHost;
    @Value("${spring.mail.port}")
    private int mailPort;
    @Value("${spring.mail.username}")
    private String mailUser;
    @Value("${spring.mail.password}")
    private String mailPass;
    @Value("${spring.mail.smtp.auth}")
    private String mailSmtpAuth;
    @Value("${spring.mail.smtp.starttls.enable}")
    private String mailSmtpTls;

    @Bean
    public JavaMailSender getJavaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(mailHost);
    mailSender.setPort(mailPort);
    
    mailSender.setUsername(mailUser);
    mailSender.setPassword(mailPass);
    
    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", mailSmtpAuth);
    props.put("mail.smtp.starttls.enable", mailSmtpTls);
    props.put("mail.debug", "true");
    
    return mailSender;
}

}
