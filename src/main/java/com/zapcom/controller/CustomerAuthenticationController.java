package com.zapcom.controller;

import com.zapcom.constants.MessageConstants;
import com.zapcom.entity.BusinessCustomer;
import com.zapcom.model.CustomerServiceDto.CustomerDto;
import com.zapcom.model.CustomerServiceDto.CustomerServiceApiKeyDto;
import com.zapcom.model.request.LoginRequest;
import com.zapcom.model.request.UiBusinessEmailRequest;
import com.zapcom.model.response.CustomerServiceTokenDto;
import com.zapcom.model.response.UiApiKeyResponse;
import com.zapcom.repository.BusinessCustomerApiKeyRepository;
import com.zapcom.repository.BusinessCustomerRepository;
import com.zapcom.service.CustomerAuthenticationService;
import com.zapcom.service.JwtUtilService;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;







@RestController
@Slf4j
@RequestMapping("/auth")
public class CustomerAuthenticationController {
    @Autowired
    private CustomerAuthenticationService customerAuthenticationService;
    @Autowired
    private BusinessCustomerApiKeyRepository businessCustomerApiKeyRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtilService jwtUtilService;
    @Autowired
    private BusinessCustomerRepository br;
    @Value("${spring.profiles.active}")
    private String active;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createBusinessCustomer(
            @RequestPart("bankDocument") MultipartFile bankDocument,
            @RequestPart("businessCustomer") String businessCustomerJson){
        try {
        customerAuthenticationService.registerBusinessCustomer(bankDocument, businessCustomerJson);
        return new ResponseEntity<>(MessageConstants.REGISTRATION_SUCCESSFUL, HttpStatus.OK);
        }catch (IOException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        log.info("Login request received for email: {}", loginRequest.getEmail());
        return customerAuthenticationService.processLogin(loginRequest);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        customerAuthenticationService.logout();
        return ResponseEntity.ok(MessageConstants.LOGOUT_MESSAGE);
    }
    //TODO:endpoint at customer-service
    @PostMapping("/customer/getCustomer")
    public ResponseEntity<?> getCustomer(@RequestPart("bankDocument") MultipartFile bankDocument,
                                         @RequestPart("customerDto") CustomerDto customerDto) {
        log.info("customer-service received the data");
        log.info("Received file: {}", bankDocument.getOriginalFilename());
        log.info("Customer name: {}", customerDto.getCustomerProfile().getCustomerName());
        return new ResponseEntity<>(customerDto, HttpStatus.OK);
    }
    //TODO:endpoint at customer-service
    @PostMapping("/customer/getToken")
    public ResponseEntity<?> getToken(@RequestBody CustomerServiceTokenDto customerTokenDto) {
        return new ResponseEntity<>(customerTokenDto, HttpStatus.OK);
    }
    //TODO:endpoint at customer-service
    @PostMapping("/customer/get_api_key")
    public ResponseEntity<String> getApiKey(@RequestBody CustomerServiceApiKeyDto customerServiceApiKeyDto) {
        log.info("customer-service received the API_KEY");
        return new ResponseEntity<>(MessageConstants.APIKEY_VERIFICATION, HttpStatus.OK);
    }
    @GetMapping("/my-profile")
    public ResponseEntity<?> myProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        BusinessCustomer bc = br.findByCustomerProfileCustomerEmail(email);
        return new ResponseEntity(bc.getCustomerProfile(), HttpStatus.OK);
    }
    @GetMapping("/verify/{customer_id}")
    public ResponseEntity<String> verify(@PathVariable UUID customer_id) {
        String apiKey = customerAuthenticationService.verifyAndGenerateApiKey(customer_id);
        System.out.println("api-key: " + apiKey);
        return new ResponseEntity<>(MessageConstants.VERIFICATION_SUCCESSFUL, HttpStatus.OK);
    }
    @GetMapping("/profile")
    public String getProfile() {
        return active;
    }
    @PostMapping("/api-key-to-ui")
    public ResponseEntity<UiApiKeyResponse> sendApiKeyToUi(@RequestBody UiBusinessEmailRequest request){
        try {
            String businessEmail = request.getBusinessEmail();
            UiApiKeyResponse response = customerAuthenticationService.getValidatedApiKeyForBusinessEmail(businessEmail);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/reset-api-key")
    public ResponseEntity<?> resetApiKeyToUi(@RequestBody UiBusinessEmailRequest request){
        try {
            UiApiKeyResponse response = customerAuthenticationService.resetAndSendApiKey(request.getBusinessEmail());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

