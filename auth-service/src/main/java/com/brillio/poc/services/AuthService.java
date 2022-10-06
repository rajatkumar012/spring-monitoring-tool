package com.brillio.poc.services;

import com.brillio.poc.entities.AuthRequest;
import com.brillio.poc.entities.AuthResponse;
import com.brillio.poc.entities.value_objects.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    private final RestTemplate restTemplate;
    private final JwtUtil jwt;

    @Autowired
    public AuthService(RestTemplate restTemplate,
                       final JwtUtil jwt) {
        this.restTemplate = restTemplate;
        this.jwt = jwt;
    }

    public AuthResponse register(AuthRequest authRequest) {
        //do validation if user already exists
        authRequest.setPassword(BCrypt.hashpw(authRequest.getPassword(), BCrypt.gensalt()));

        UserVO userVO = restTemplate.postForObject("http://user-service/users/", authRequest, UserVO.class);
        Assert.notNull(userVO, "Failed to register user. Please try again later");

        String accessToken = jwt.generate(userVO, "ACCESS");
        String refreshToken = jwt.generate(userVO, "REFRESH");

        return new AuthResponse(accessToken, refreshToken, userVO.getId());

    }

    public AuthResponse login(AuthRequest authRequest) {
        //do validation if user already exists
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserVO userVO = restTemplate.getForObject("http://user-service/users/{emailId}", UserVO.class, authRequest.getEmail());
        Assert.notNull(userVO, "Failed to login user. Please try again later");
        if(passwordEncoder.matches( authRequest.getPassword(), userVO.getPassword())) {
            String accessToken = jwt.generate(userVO, "ACCESS");
            String refreshToken = jwt.generate(userVO, "REFRESH");
            return new AuthResponse(accessToken, refreshToken, userVO.getId());
        }
        return null;
    }
}
