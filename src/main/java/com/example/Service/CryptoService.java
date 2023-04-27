package com.example.Service;

import com.example.Model.InvoiceResponse;
import com.example.Model.Item;
import com.example.Model.InvoiceRequest;
import com.example.Repo.ItemRepository;
import com.example.serviceImp.ItemServiceImp;
import com.example.serviceImp.TransactionServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class CryptoService {

    @Autowired
    ItemServiceImp itemServiceImp;

    @Autowired
    TransactionServiceImpl transactionService;

    @Autowired
    ItemRepository itemRepository;

    public InvoiceResponse createInvoice(InvoiceRequest invoice) throws IOException {
        URL url = new URL("https://api-sandbox.coingate.com/v2/orders");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Set the request method and headers
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer 8EgADzTZbJScLGV3otKS9zziHERphqw8rgeoxLux");

        Item item= itemServiceImp.findById(Integer.valueOf(invoice.getOrderId()));

        // Create the request body
        String requestBody = "{\n" +
                "    \"title\": \"Paying for " + item.getItemName() +"\",\n" +
                "    \"price_amount\": " + item.getItemPrice() +",\n" +
                "    \"price_currency\": \"EUR\",\n" +
                "    \"receive_currency\": \"EUR\",\n" +
                "    \"callback_url\":\"http://127.0.0.1:8080/invoice/webhook\",\n" +
                "    \"success_url\": \"http://localhost:8080/home_page\",\n" +
                "    \"cancel_url\": \"http://localhost:8080/home_page\",\n" +
                "    \"order_id\": " + item.getItemId() +",\n" +
                "    \"description\": " +  "\"" + item.getItemDesc() +"\"" + "\n" +
                "}";


        System.out.println(requestBody);
        item.setAvailable(false);
        itemRepository.save(item);

        // Send the request
        conn.setDoOutput(true);
        conn.getOutputStream().write(requestBody.getBytes());
        // Read the response
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            responseBuilder.append(line);
        }
        reader.close();
        conn.disconnect();

        // Map the response to a Java class using ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        InvoiceResponse response = objectMapper.readValue(responseBuilder.toString(), InvoiceResponse.class);
        transactionService.saveTransaction(invoice,response,item);
        return response;
    }


}
