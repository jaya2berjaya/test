package com.arism.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    @PostMapping
    public Map<String, String> checkout() {
        // Simulasi response dari Midtrans Snap API
        Map<String, String> response = new HashMap<>();
        response.put("snap_token", "DUMMY-SNAP-TOKEN-123456");
        response.put("redirect_url", "https://app.sandbox.midtrans.com/snap/v2/vtweb/DUMMY-SNAP-TOKEN-123456");
        return response;
    }
}