package com.example.Crypto;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CryptoController {

    @GetMapping("/Crypto")
    public String viewHomePage() {
        return "PayByCrypto";
    }
}
