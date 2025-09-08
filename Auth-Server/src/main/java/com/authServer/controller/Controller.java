package com.authServer.controller;

import com.authServer.dtos.SignupRequest;
import com.authServer.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class Controller {

    private final AuthService service;

    @GetMapping("/login")
    public String login(@AuthenticationPrincipal OidcUser principal) {
        if (principal != null) {
            String name = principal.getAttribute("name");
            return "Welcome, " + name ;
        }
        return "Redirecting to login...";
    }

    @PostMapping("/signup")
    public ResponseEntity<String> singUp(@Valid @RequestBody SignupRequest request){
        service.signup(request);
        return ResponseEntity.ok("User created with username"+request.getUsername());
    }
}
