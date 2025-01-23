package com.fortune.app.healthcheck;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    @GetMapping
    public String healthCheck(HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();
        return "당신의 IP는: " + clientIp;
    }
}
