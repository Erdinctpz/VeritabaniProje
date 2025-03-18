package com.veriTabaniProje.demo.veriTabaniProje.service;

import com.veriTabaniProje.demo.veriTabaniProje.model.User;
import com.veriTabaniProje.demo.veriTabaniProje.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = Optional.ofNullable(userRepository.findByUsername(username));
        User user = userOpt.orElseThrow(() -> {
            logger.error("Kullanıcı bulunamadı: {}", username);
            return new UsernameNotFoundException("Kullanıcı bulunamadı: " + username);
        });

        logger.info("Kullanıcı başarıyla yüklendi: {}", username);
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(getUserRoles(user))
                .build();
    }

    private String[] getUserRoles(User user) {
        List<String> roles = user.getRoles() != null ? user.getRoles() : List.of("USER");
        return roles.toArray(new String[0]);
    }
}