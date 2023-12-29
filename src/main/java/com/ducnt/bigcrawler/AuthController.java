package com.ducnt.bigcrawler;

import com.ducnt.bigcrawler.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final JwtService jwtService;

    @GetMapping("/user")
    public String AuthUser() {
        String user = "I am user";
        return "${user}";
    }
}
