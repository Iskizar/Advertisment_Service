package ru.senla.finalproject.service.IService;

import java.util.Map;

public interface IEmailService {
    void sendForgotPasswordEmail(String to, String subject, Map<String, Object> templateModel) throws Exception;
}
