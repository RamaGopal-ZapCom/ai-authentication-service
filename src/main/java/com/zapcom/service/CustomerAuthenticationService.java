package com.zapcom.service;

import com.zapcom.entity.BusinessCustomer;
import com.zapcom.entity.BusinessCustomerApiKey;
import com.zapcom.model.CustomerServiceDto.CustomerServiceApiKeyDto;
import com.zapcom.model.request.LoginRequest;
import com.zapcom.model.response.UiApiKeyResponse;
import java.io.IOException;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;



public interface CustomerAuthenticationService {
    BusinessCustomer saveBusinessCustomer(BusinessCustomer businessCustomer, MultipartFile bankDocument) throws IOException;

    String login(LoginRequest loginRequest);

    void logout();

    void sendCustomerDataToCustomerService(MultipartFile bankDocument, BusinessCustomer businessCustomer) throws IOException;

    void sendEmail(BusinessCustomer customer, UUID uuid);

    void sendApiKeyToCustomerService(CustomerServiceApiKeyDto customerServiceApiKeyDto);

    String generateApiKey(UUID customer_id);

    String validateApiKey(String customerEmail) throws Exception;

    void setApiKey(UUID customer_id, String apiKey);

    String resetApiKey(String customerEmail);

    boolean isCustomerExistFindByCustomerEmail(String customerEmail);

    BusinessCustomerApiKey getBusinessCustomerApiKeyByEmail(String email);

    boolean apiKeyAlreadyGeneratedForId(UUID customerId);

    BusinessCustomer findByCustomerId(UUID customerId);

    void sendTokenToCustomerService(String email, String token);

    UiApiKeyResponse resetAndSendApiKey(String businessEmail) throws Exception;

    UiApiKeyResponse getValidatedApiKeyForBusinessEmail(String businessEmail) throws Exception;

    String verifyAndGenerateApiKey(UUID customerId);

    void registerBusinessCustomer(MultipartFile bankDocument, String businessCustomerJson) throws IOException;

    ResponseEntity<?> processLogin(LoginRequest loginRequest);
}
