package ru.senla.finalproject.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.senla.finalproject.service.IService.IEmailService;
import ru.senla.finalproject.service.IService.IUserService;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@Tag(name = "email_api")
public class EmailController {
    IEmailService emailService;
    IUserService userService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/forgot_password")
    public String forgotPassword(@RequestParam String email) {
        // Данные для шаблона
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("email", email);
        templateModel.put("password", userService.getPasswordByEmail(email));

        try {
            emailService.sendForgotPasswordEmail(email, "Восстановление пароля", templateModel);
            return "Вам на почту отправлено письмо!";
        } catch (MessagingException e) {
            return "Ошибка при отправке сообщения.";
        } catch (Exception e) {
            return "Неизвестная ошибка при попытке отправки сообщения";
        }
    }
}
