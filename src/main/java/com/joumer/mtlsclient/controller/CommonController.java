package com.joumer.mtlsclient.controller;

import com.joumer.mtlsclient.client.CommonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController {

    private final CommonService commonService;

    public CommonController(CommonService commonService) {
        this.commonService = commonService;
    }

    @GetMapping("hello")
    public ResponseEntity<String> hello(@RequestParam String deviceId) {
        return commonService.sendHello(deviceId);
    }
}
