package com.ducnt.bigcrawler.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ducnt.bigcrawler.exception.NotCorrectJWTException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class JwtService {
    public String extractTenantID(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null && !authHeader.startsWith("Bearer ")) {
            throw new NotCorrectJWTException("jwt not correct");
        }
        String token = authHeader.substring(7);
        DecodedJWT decodedJWT = JWT.decode(token);
        Map<String, Claim> claims = decodedJWT.getClaims();
        String tenantID = claims.get("tenantID").asString();
        return tenantID;
    }

}
