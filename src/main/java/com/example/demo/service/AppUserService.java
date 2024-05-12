package com.example.demo.service;

import com.example.demo.config.SecurityConfig;
import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.entity.AppUser;
import com.example.demo.entity.Flight;
import com.example.demo.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class AppUserService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;
    private final static String USER_NOT_FOUND_MSG = "User with email %s not found";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username)));
    }

    public ApiResponse<?> login(@RequestBody LoginRequest loginRequest) {

        if(isCredentialsTrue(loginRequest.getUsername(), loginRequest.getPassword())){
            return ApiResponse
                    .builder()
                    .ok(true)
                    .status(HttpStatus.OK.value())
                    .message("Authentication passed")
                    .build();
        }

        return ApiResponse
                .builder()
                .ok(false)
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("Authentication Failed")
                .build();
    }

    public boolean isCredentialsTrue(String username, String password){
        AppUser user = (AppUser) loadUserByUsername(username);
        return SecurityConfig.passwordEncoder().matches(password, user.getPassword());
    }

    public ResponseEntity<?> getUserId(HttpHeaders headers) {

        Long id = getUserIdFromHeaders(headers);
        if (id != 0L) {

            return new ResponseEntity<>(id, HttpStatus.OK);

        }

        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    public Long getUserIdFromHeaders(HttpHeaders headers){

        if (headers.containsKey(HttpHeaders.AUTHORIZATION)) {
            String authorizationHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
            assert authorizationHeader != null;
            if (authorizationHeader.startsWith("Basic ")) {
                String base64Credentials = authorizationHeader.substring("Basic".length()).trim();
                byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
                String credentials = new String(credDecoded, StandardCharsets.UTF_8);
                // credentials = username:password
                final String[] values = credentials.split(":", 2);
                AppUser user = (AppUser) loadUserByUsername(values[0]);
                return user.getId();
            }
        }

        return 0L;
    }
}
