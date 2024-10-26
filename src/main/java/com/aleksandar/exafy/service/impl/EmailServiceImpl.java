package com.aleksandar.exafy.service.impl;

import com.aleksandar.exafy.data.entities.Task;
import com.aleksandar.exafy.exception.EmailSendingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.aleksandar.exafy.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.format.DateTimeFormatter;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private final JavaMailSender javaMailSender;

    @Autowired
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String defaultSender;

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    public EmailServiceImpl(
            JavaMailSender javaMailSender,
            SpringTemplateEngine springTemplateEngine
    )
    {
        this.javaMailSender = javaMailSender;
        this.templateEngine = springTemplateEngine;
    }

    @Override
    public void sendNewTaskEmail(String recipient, Task task) {

        String htmlContent = templateEngine.process("new-task", createContextData(task));
        trySendEmail(
                htmlContent,
                "[Exafy Exam] You've been assigned a task",
                task.getTitle(),
                recipient
        );
    }

    @Override
    public void sendReassignedTaskEmail(String recipient, Task task) {

        String htmlContent = templateEngine.process("reassigned-task", createContextData(task));
        trySendEmail(
                htmlContent,
                "[Exafy Exam] You've been reassigned a task",
                task.getTitle(),
                recipient
        );
    }

    @Override
    public void notifyAboutExistingTask(String recipient, Task task) {
        String htmlContent = templateEngine.process("existing-task-notification", createContextData(task));
        trySendEmail(
                htmlContent,
                "[Exafy Exam] You have a active task that needs addressing",
                task.getTitle(),
                recipient
        );
    }

    private Context createContextData(Task task)
    {
        Context context = new Context();
        context.setVariable("title", task.getTitle());
        context.setVariable("description", task.getDescription());
        context.setVariable("dueDate", task.getDueDate().format(DateTimeFormatter.ISO_DATE));

        return context;
    }

    private void trySendEmail(String htmlContent, String subject, String taskTitle, String recipient)
    {
        MimeMessage content = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(content);

        try
        {
            helper.setTo(recipient);
            helper.setSubject(subject + " - " + taskTitle);
            helper.setText(htmlContent, true);

            javaMailSender.send(content);
        }
        catch (MessagingException | MailException ex)
        {
            logger.error("Error sending email to {}: {}", recipient, ex.getMessage());
            throw new EmailSendingException("Failed to send email", ex);
        }
    }
}
