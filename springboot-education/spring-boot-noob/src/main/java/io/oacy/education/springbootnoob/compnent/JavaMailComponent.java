package io.oacy.education.springbootnoob.compnent;

import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;


import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JavaMailComponent {

    private static final String TEMPLATE = "mail.ftl";

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailProperties mailProperties;

    public void sendMail(String email) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("email", email);
        try {
            // 获取内容
            String text = this.getTextByTemplate(TEMPLATE, map);
            // 发送
            this.send(email, text);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String send(String email, String text) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        InternetAddress from = new InternetAddress();
        from.setAddress(this.mailProperties.getUsername());
        from.setPersonal("Zephyr", "UTF-8");
        helper.setFrom(from);
        helper.setTo(email);
        helper.setSubject("SpringBoot 发送的第一封邮件");
        helper.setText(text, true);
        this.javaMailSender.send(message);
        return text;
    }

    private String getTextByTemplate(String TEMPLATE, Map<String, Object> model) throws Exception {

        return FreeMarkerTemplateUtils
                .processTemplateIntoString(freeMarkerConfigurer.getConfiguration().getTemplate(TEMPLATE), model);
    }

}
