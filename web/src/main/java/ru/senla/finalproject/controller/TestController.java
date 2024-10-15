package ru.senla.finalproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "test_api")
@RestController
@AllArgsConstructor
public class TestController {

    @Operation(summary = "Вывод данных на стартовой страницы для проверки работы приложения")
    @RequestMapping("/")
    public String test() {
        return "hello world";

    }


}