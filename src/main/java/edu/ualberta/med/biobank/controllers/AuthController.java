package edu.ualberta.med.biobank.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ualberta.med.biobank.services.TokenService;

@RestController
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/token")
    public String token(Authentication authentication) {
        logger.info("Token requested for user: '{}'", authentication.getName());
        String token = tokenService.generateToken(authentication);
        logger.info("Token granted: {}", token);
        return token;
    }

}
