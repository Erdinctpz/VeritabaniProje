package com.veriTabaniProje.demo.veriTabaniProje.service;

import com.veriTabaniProje.demo.veriTabaniProje.model.User;
import com.veriTabaniProje.demo.veriTabaniProje.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.warn("Kullanıcı bulunamadı: {}", username);
        }
        return Optional.ofNullable(user);
    }

    public User saveUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            logger.info("Kullanıcı eklendi: {}, Hashlenmiş Şifre: {}", savedUser.getUsername(), savedUser.getPassword());
            return savedUser;
        } catch (Exception e) {
            logger.error("Kullanıcı kaydedilemedi: {}, Hata: {}", user.getUsername(), e.getMessage());
            throw new RuntimeException("Kullanıcı kaydedilemedi", e);
        }
    }
}