package com.veriTabaniProje.demo.veriTabaniProje.controller;

import com.veriTabaniProje.demo.veriTabaniProje.model.User;
import com.veriTabaniProje.demo.veriTabaniProje.service.UserService;
import com.veriTabaniProje.demo.veriTabaniProje.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.annotation.PostConstruct;

import java.util.List;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public LoginController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostConstruct
    public void initAdminUser() {
        if (userService.findByUsername("admin").isEmpty()) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword("password123");
            user.setRoles(List.of("ADMIN", "USER"));
            userService.saveUser(user);
            logger.info("Admin kullanıcısı oluşturuldu: {}", user.getUsername());
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/welcome")
    public String showWelcomePage(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Kullanıcı doğrulanamadı, login sayfasına yönlendiriliyor");
            return "redirect:/login";
        }

        String username = authentication.getName();
        if (username == null) {
            logger.error("Username null, authentication nesnesi: {}", authentication);
            return "redirect:/login";
        }

        String token = jwtUtil.generateToken(username);
        model.addAttribute("username", username);
        model.addAttribute("token", token);
        logger.info("Hoş geldin sayfası gösteriliyor, kullanıcı: {}", username);
        return "welcome";
    }
}