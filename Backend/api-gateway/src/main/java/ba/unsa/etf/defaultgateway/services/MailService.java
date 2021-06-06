package ba.unsa.etf.defaultgateway.services;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class MailService {

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Qualifier("freeMarkerConfiguration")
    @Autowired
    private Configuration freemarkerConfig;

    @Value("${spring.mail.username}")
    private String mojDoktorMail;

    @Value("${spring.mail.password}")
    private String mojDoktorLozinka;


    public void sendmail(String email, String name, String resetToken) throws MessagingException, IOException, TemplateException {
        mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(mojDoktorMail);
        mailSender.setPassword(mojDoktorLozinka);

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.debug", "true");

        mailSender.setJavaMailProperties(javaMailProperties);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Template t = freemarkerConfig.getTemplate("email-template.ftl");
        Map model = new HashMap();
        model.put("name", name);
        model.put("token", resetToken);
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

        helper.setTo(email);
        helper.setSubject("Reset token");
        helper.setText(html, true);
        mailSender.send(message);
    }

}