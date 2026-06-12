package com.learn.fraud.controller;

import com.learn.clients.fraud.FraudCheckResponse;
import com.learn.clients.fraud.FraudRequest;
import com.learn.fraud.service.FraudCheckCustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/fraud-checks")
@Slf4j
public record FraudController(FraudCheckCustomerService fraudCheckCustomerService) {


    @PostMapping()
    public FraudCheckResponse checkCustomer(@RequestBody FraudRequest fraudRequest) {
        boolean isFraudulentCustomer = fraudCheckCustomerService.
                fraudCheckingCustomer(fraudRequest);
        log.info("fraud check request for customer {}", isFraudulentCustomer);

        return new FraudCheckResponse(isFraudulentCustomer);
    }

}