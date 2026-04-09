package com.sharmi.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sharmi.model.ApiLog;
import com.sharmi.repository.ApiLogRepository;

@Service
public class ApiMonitorService {

    @Autowired
    private ApiLogRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    // The core logic to test an API
    public ApiLog testApi(String url, String method) {
        ApiLog log = new ApiLog();
        log.setUrl(url);
        log.setMethod(method);

        long startTime = System.currentTimeMillis();

        try {
            // Actively pinging the external API
            var response = restTemplate.getForEntity(url, String.class);
            log.setStatusCode(response.getStatusCode().value());
        } catch (Exception e) {
            log.setStatusCode(500); // Error or Timeout
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        log.setResponseTimeMs(duration);
        
        // DETECTION LOGIC: If it takes more than 500ms, mark it as slow
        log.setIsSlow(duration > 500);
        log.setCreatedAt(LocalDateTime.now());

        // Save result to MySQL
        return repository.save(log);
    }

    // To show the history trend later
    public List<ApiLog> getHistory() {
        return repository.findAll();
    }
}