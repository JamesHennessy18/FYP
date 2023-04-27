package com.example.Controller;

import com.example.Model.InvoiceRequest;
import com.example.Model.InvoiceResponse;
import com.example.Service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
public class CryptoRestController {

    @Autowired
    CryptoService cryptoService;

    @PostMapping("/invoice")
    public InvoiceResponse createInvoice(@RequestBody InvoiceRequest invoice) throws IOException {
        System.out.println("test");
        return cryptoService.createInvoice(invoice);
    }


    @GetMapping("/invoice")
    public String getInvoice() throws IOException {
        System.out.println("test");
        return "hi";
    }
}
