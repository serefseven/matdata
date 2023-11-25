package com.logicpeaks.security.service;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${mat-data.url}")
    String matdataApplicationUrl;
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;
    @Autowired
    private MessageSource messageSource;

    public void sendPasswordResetEmail(String to, String token) throws TemplateException, IOException, MessagingException {
        Locale locale = LocaleContextHolder.getLocale();

        Map<String, Object> templateModel = new HashMap();
        templateModel.put("hello",messageSource.getMessage("EMAIL.PASWORD_RESET.HELLO", null, locale));
        templateModel.put("message1",messageSource.getMessage("EMAIL.PASWORD_RESET.MESSAGE_1", null, locale));
        templateModel.put("buttonTitle",messageSource.getMessage("EMAIL.PASWORD_RESET.BUTTON", null, locale));
        templateModel.put("message2",messageSource.getMessage("EMAIL.PASWORD_RESET.MESSAGE_2", null, locale));
        templateModel.put("message3",messageSource.getMessage("EMAIL.PASWORD_RESET.MESSAGE_3", null, locale));
        templateModel.put("bestRegards",messageSource.getMessage("EMAIL.PASWORD_RESET.BEST_REGARDS", null, locale));
        templateModel.put("link",matdataApplicationUrl);
        templateModel.put("resetLink",String.format(matdataApplicationUrl+"/auth/password-reset/%s/%s",token,to));

        Template freemarkerTemplate = freemarkerConfigurer
                .getConfiguration()
                .getTemplate("password-reset.ftl");
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
        String subject = messageSource.getMessage("EMAIL.PASWORD_RESET.SUBJECT", null, locale);
        sendSimpleMessage(to,subject,htmlBody);
    }

    private void sendSimpleMessage(
            String to, String subject, String text) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("info@mat-data.com", "Mat-Data");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text,true);
        emailSender.send(message);
    }
}
