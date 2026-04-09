package com.sharmi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sharmi.model.ApiLog;
import com.sharmi.service.ApiMonitorService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Important for VS Code frontend connection
public class ApiMonitorController {

    @Autowired
    private ApiMonitorService service;

    /**
     * This endpoint triggers the actual API test.
     * Use it like: http://localhost:8080/api/test?url=https://google.com
     */
    @GetMapping("/test")
    public ApiLog runTest(@RequestParam String url, 
                          @RequestParam(defaultValue = "GET") String method) {
        return service.testApi(url, method);
    }

    /**
     * This fetches all past results from MySQL for your dashboard charts.
     */
    @GetMapping("/history")
    public List<ApiLog> getFullHistory() {
        return service.getHistory();
    }
}