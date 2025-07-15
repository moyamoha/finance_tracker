package com.finance_tracker.service.mailing;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public class MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${APP_MAIL_SENDER}")
    private String from;

    public MailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendHtmlEmailFromTemplate(String template, EmailPayload payload) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(payload.receiver());
        helper.setFrom(this.from);
        helper.setSubject(payload.subject());

        Context context = new Context();
        payload.variables().forEach(context::setVariable);
        String htmlContent = templateEngine.process(template, context);
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }
}
