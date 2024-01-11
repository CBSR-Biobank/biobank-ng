package edu.ualberta.med.biobank.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.ualberta.med.biobank.dtos.UserDTOClient;
import edu.ualberta.med.biobank.services.TokenService;
import edu.ualberta.med.biobank.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
public class AuthController {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final TokenService tokenService;

    private UserService userService;

    public AuthController(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/auth")
    public ResponseEntity<UserDTOClient> isAuthorized(Authentication authentication) {
        var username = authentication.getName();
        var userInfoMaybe = userService.findByLogin(username);

        if (userInfoMaybe.isLeft()) {
            throw new UsernameNotFoundException("User not found for this username: " + username);
        }

        var userInfo = UserDTOClient.fromUserDTO(userInfoMaybe.getRight().get());
        String token = tokenService.generateToken(authentication);
        return ResponseEntity.ok().header(
            HttpHeaders.AUTHORIZATION,
            token
        ).body(userInfo);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping("/token")
    public ResponseEntity<UserDTOClient> token(Authentication authentication) {
        var username = authentication.getName();
        var userInfoMaybe = userService.findByLogin(username);

        if (userInfoMaybe.isLeft()) {
            throw new UsernameNotFoundException("User not found for this username: " + username);
        }

        var userInfo = UserDTOClient.fromUserDTO(userInfoMaybe.getRight().get());
        String token = tokenService.generateToken(authentication);
        return ResponseEntity.ok().header(
            HttpHeaders.AUTHORIZATION,
            token
        ).body(userInfo);
    }
}
