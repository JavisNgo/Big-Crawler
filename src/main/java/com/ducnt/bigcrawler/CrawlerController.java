package com.ducnt.bigcrawler;

import com.ducnt.bigcrawler.jwt.JwtService;
import com.ducnt.bigcrawler.service.CrawlerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crawler")
public class CrawlerController {
    private final JwtService jwtService;
    private final CrawlerService crawlerService;
    @GetMapping("")
    public ResponseEntity<?> CrawlDataFromToken(HttpServletRequest request) {
        String tenantID = jwtService.extractTenantID(request);
        return ResponseEntity.ok(crawlerService.CrawlData(tenantID));
    }
}
