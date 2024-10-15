package ru.senla.finalproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.senla.finalproject.dto.JwtRequest;
import ru.senla.finalproject.dto.JwtResponse;
import ru.senla.finalproject.exception.AuthException;
import ru.senla.finalproject.security.JwtTokenUtils;
import ru.senla.finalproject.service.IService.IUserService;



@RestController
@AllArgsConstructor
@Tag(name = "auth_api")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final IUserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;


    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        System.out.println();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        }
        catch (BadCredentialsException e) {
            throw new AuthException("Неправильный логин или пароль");
        }
        catch (RuntimeException e) {
            log.debug("Ошибка при аутентификации пользователя");
            throw new AuthException("Ошибка при попытке аутентификации пользователя");
        }

        UserDetails userDetails = userService.loadUserByUsername(authRequest.getEmail());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }


}