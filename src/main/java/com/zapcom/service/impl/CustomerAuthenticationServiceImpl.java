package com.zapcom.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zapcom.constants.ErrorMessages;
import com.zapcom.constants.MessageConstants;
import com.zapcom.entity.BusinessCustomer;
import com.zapcom.entity.BusinessCustomerApiKey;
import com.zapcom.entity.CustomerFields.AdminDetails;
import com.zapcom.entity.CustomerFields.BankingDocument;
import com.zapcom.entity.CustomerFields.Primary;
import com.zapcom.exceptions.AccountAlreadyActivated;
import com.zapcom.exceptions.CustomerAlreadyExist;
import com.zapcom.exceptions.CustomerNotFoundException;
import com.zapcom.exceptions.NullPointerException;
import com.zapcom.mapper.CustomerMapper;
import com.zapcom.model.CustomerServiceDto.AdminDetailsWithoutPassword;
import com.zapcom.model.CustomerServiceDto.CustomerDto;
import com.zapcom.model.CustomerServiceDto.CustomerServiceApiKeyDto;
import com.zapcom.model.request.CustomerRequest;
import com.zapcom.model.request.CustomerServiceRequestDto;
import com.zapcom.model.request.LoginRequest;
import com.zapcom.model.response.CustomerServiceTokenDto;
import com.zapcom.model.response.UiApiKeyResponse;
import com.zapcom.repository.BusinessCustomerApiKeyRepository;
import com.zapcom.repository.BusinessCustomerRepository;
import com.zapcom.repository.TokenRepository;
import com.zapcom.service.CustomerAuthenticationService;
import com.zapcom.service.CustomerCredentialsService;
import com.zapcom.service.JwtUtilService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@Slf4j
public class CustomerAuthenticationServiceImpl implements CustomerAuthenticationService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private BusinessCustomerRepository businessCustomerRepository;

    @Value("${spring.mail.username}")
    private String username;
    @Autowired
    private Environment environment;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private BusinessCustomerApiKeyRepository businessCustomerApiKeyRepository;
    @Autowired
    private CustomerCredentialsService customerCredentialsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private JwtUtilService jwtUtilService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    WebClient webClient = WebClient.builder().codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)).baseUrl("http://localhost:8081").build();
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(6);

    public BusinessCustomer saveBusinessCustomer(BusinessCustomer businessCustomer, MultipartFile bankDocument){
        try {
            BankingDocument doc = new BankingDocument();
            businessCustomer.getBankingDetails().setCancelledChequeOrBankStatement(doc);
            businessCustomer.getBankingDetails().getCancelledChequeOrBankStatement().setFileName(bankDocument.getOriginalFilename());
            businessCustomer.getBankingDetails().getCancelledChequeOrBankStatement().setFileType(bankDocument.getContentType());
            businessCustomer.getBankingDetails().getCancelledChequeOrBankStatement().setData(bankDocument.getBytes());
            String encPassword = getEncodedPassword(businessCustomer.getAdminDetails().getPrimary().getPassword());
            businessCustomer.getAdminDetails().getPrimary().setPassword(encPassword);
            final BusinessCustomer businessCustomerCopy = businessCustomerRepository.save(businessCustomer);
            customerCredentialsService.save(businessCustomerCopy);
            return businessCustomer;
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
    public void registerBusinessCustomer(MultipartFile bankDocument, String businessCustomerJson){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CustomerRequest customerRequest = objectMapper.readValue(businessCustomerJson, CustomerRequest.class);

            BusinessCustomer businessCustomer = CustomerMapper.instance.toEntity(customerRequest);
            if (Objects.isNull(businessCustomer.getCustomerProfile()))
                throw new NullPointerException(ErrorMessages.CUSTOMER_PROFILE_ISNULL);
            if (Objects.isNull(businessCustomer.getAdminDetails()))
                throw new NullPointerException(ErrorMessages.ADMIN_DETAILS_ISNULL);
            if (Objects.isNull(businessCustomer.getApiConfiguration()))
                throw new NullPointerException(ErrorMessages.API_CONFIGURATION_ISNULL);
            if (Objects.isNull(businessCustomer.getBankingDetails()))
                throw new NullPointerException(ErrorMessages.BANK_DETAILS_ISNULL);
            if (Objects.isNull(businessCustomer.getBranding()))
                throw new NullPointerException((ErrorMessages.BRANDING_ISNULL));
            if (Objects.isNull(businessCustomer.getAgreements()))
                throw new NullPointerException((ErrorMessages.AGREEMENTS_ISNULL));
            if (Objects.isNull(businessCustomer.getOperations()))
                throw new NullPointerException(ErrorMessages.OPERATIONS_ISNULL);
            if (Objects.isNull(businessCustomer.getLegalAndTaxCompliance()))
                throw new NullPointerException((ErrorMessages.LEGAL_AND_TAX_COMPLIANCE_ISNULL));
            if (Objects.isNull(businessCustomer.getChatbotConfig()))
                throw new NullPointerException(ErrorMessages.CHATBOT_CONFIG_ISNULL);
            if (isCustomerExistFindByCustomerEmail(businessCustomer.getCustomerProfile().getCustomerEmail())) {
                throw new CustomerAlreadyExist(MessageConstants.CUSTOMER_ALREADY_EXISTS);
            }

            UUID uuid = UUID.randomUUID();
            businessCustomer.setCustomerId(uuid);

            saveBusinessCustomer(businessCustomer, bankDocument);
            log.info("Customer data saved to DB");

            sendCustomerDataToCustomerService(bankDocument, businessCustomer);
            log.info("Customer data sent to customer-service");

            sendEmail(businessCustomer, uuid);
            log.info("Verification mail sent to user");
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public ResponseEntity<?> processLogin(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String token = login(loginRequest);

        BusinessCustomerApiKey businessCustomerApiKey = getBusinessCustomerApiKeyByEmail(email);
        if (businessCustomerApiKey == null || businessCustomerApiKey.getApiKey() == null) {
            return new ResponseEntity<>(MessageConstants.EMAIL_VERIFICATION, HttpStatus.CONFLICT);
        }

        sendTokenToCustomerService(email, token);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    public String login(LoginRequest loginRequest) {
        return authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
    }

    private String authenticateUser(String email, String password) {
        log.info("authenticate user " + email);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtilService.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        tokenRepository.storeToken(userDetails.getUsername(), token);
        return token;
    }

    public void logout() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        tokenRepository.removeToken(userDetails.getUsername());
    }

    private String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void sendCustomerDataToCustomerService(MultipartFile bankDocument, BusinessCustomer businessCustomer){
        CustomerDto customerDto = CustomerDto.builder().customer_id(businessCustomer.getCustomerId()).customerProfile(businessCustomer.getCustomerProfile()).apiConfiguration(businessCustomer.getApiConfiguration()).bankingDetails(businessCustomer.getBankingDetails()).agreements(businessCustomer.getAgreements()).branding(businessCustomer.getBranding()).chatbotConfig(businessCustomer.getChatbotConfig()).legalAndTaxCompliance(businessCustomer.getLegalAndTaxCompliance()).metaData(businessCustomer.getMetaData()).operations(businessCustomer.getOperations()).build();
        AdminDetails adminDetails = businessCustomer.getAdminDetails();
        AdminDetailsWithoutPassword adminDetailsWithoutPassword = new AdminDetailsWithoutPassword();
        adminDetailsWithoutPassword.setTechnical(adminDetails.getTechnical());
        Primary primary = adminDetails.getPrimary();
        com.zapcom.model.CustomerServiceDto.Primary primary1 = com.zapcom.model.CustomerServiceDto.Primary.builder().name(primary.getName()).phone(primary.getPhone()).address(primary.getAddress()).timeZone(primary.getTimeZone()).build();
        customerDto.setAdminDetails(adminDetailsWithoutPassword);
        CustomerServiceRequestDto customerServiceRequestDto = new CustomerServiceRequestDto();
        customerServiceRequestDto.setCustomerDto(customerDto);
        customerServiceRequestDto.setBankDocument(bankDocument);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(customerDto);
            MultiValueMap<String, Object> multipartBody = new LinkedMultiValueMap<>();
            ByteArrayResource fileAsResource = new ByteArrayResource(bankDocument.getBytes()) {
                @Override
                public String getFilename() {
                    return bankDocument.getOriginalFilename();
                }
            };
            multipartBody.add("bankDocument", fileAsResource);
            HttpHeaders jsonHeaders = new HttpHeaders();
            jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> jsonPart = new HttpEntity<>(json, jsonHeaders);
            multipartBody.add("customerDto", jsonPart);

            webClient.post().uri("/auth/customer/getCustomer").contentType(MediaType.MULTIPART_FORM_DATA).body(BodyInserters.fromMultipartData(multipartBody)).retrieve().bodyToMono(String.class).doOnSuccess(response -> System.out.println("data sent to customer service")).doOnError(Throwable::printStackTrace).subscribe();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public void sendEmail(BusinessCustomer customer, UUID uuid) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(new InternetAddress(username, MessageConstants.EMAIL_MESSAGE));
            helper.setTo(customer.getCustomerProfile().getCustomerEmail());
            helper.setSubject("Email verification");
            String port = environment.getProperty("local.server.port");
            String link = "http://localhost:" + port + "/auth/verify/" + uuid;
            String htmlContent = "<p>Click the button below to verify your account:</p>" + "<a href=\"" + link + "\" style=\"" + "background-color: #4CAF50;" + "color: white;" + "padding: 10px 20px;" + "text-align: center;" + "text-decoration: none;" + "display: inline-block;" + "border-radius: 5px;" + "\">Activate Account</a>";
            helper.setText(htmlContent, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(MessageConstants.FAILED_TO_SEND_EMAIL + e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (MailException e) {
            throw new RuntimeException(MessageConstants.EMAIL_DOES_NOT_EXIST);
        }
    }

    public void sendApiKeyToCustomerService(CustomerServiceApiKeyDto customerServiceApiKeyDto) {
        webClient.post().uri("/auth/customer/get_api_key").bodyValue(customerServiceApiKeyDto).retrieve().bodyToMono(String.class).doOnSuccess(response -> System.out.println("api key sent to customer service")).doOnError(error -> {
            error.printStackTrace();
        }).subscribe();
    }

    public String generateApiKey(UUID customer_id) {

        BusinessCustomer customer = businessCustomerRepository.findByCustomerId(customer_id).orElseThrow(() -> new CustomerNotFoundException("User not found"));

        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[32];
        secureRandom.nextBytes(keyBytes);
        String apiKey = Base64.getUrlEncoder().withoutPadding().encodeToString(keyBytes);
        return apiKey;
    }

    public String validateApiKey(String customerEmail){
        try {
            BusinessCustomerApiKey businessCustomerApiKey = businessCustomerApiKeyRepository.findByCustomerEmail(customerEmail);
            if (Objects.isNull(businessCustomerApiKey)) throw new RuntimeException("user with this mail not found");
            String apiKey = businessCustomerApiKey.getApiKey();
            return apiKey;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public String verifyAndGenerateApiKey(UUID customerId) {
        if (findByCustomerId(customerId) == null) {
            throw new CustomerNotFoundException("Verification failed. No customer found with this email");
        }

        if (apiKeyAlreadyGeneratedForId(customerId)) {
            throw new AccountAlreadyActivated(ErrorMessages.EMAIL_ALREADY_VERIFIED);
        }

        String apiKey = generateApiKey(customerId);
        setApiKey(customerId, apiKey);

        CustomerServiceApiKeyDto dto = new CustomerServiceApiKeyDto();
        dto.setApi_key(apiKey);
        dto.setCustomer_id(customerId);

        sendApiKeyToCustomerService(dto);

        return apiKey;
    }

    public void setApiKey(UUID customer_id, String apiKey) {
        Optional<BusinessCustomer> businessCustomer = businessCustomerRepository.findByCustomerId(customer_id);
        BusinessCustomerApiKey businessCustomerApiKey = new BusinessCustomerApiKey();
        businessCustomerApiKey.setApiKey(apiKey);
        businessCustomerApiKey.setCustomerId(customer_id);
        businessCustomerApiKey.setCustomerEmail(businessCustomer.get().getCustomerProfile().getCustomerEmail());
        businessCustomerApiKeyRepository.save(businessCustomerApiKey);
    }

    public UiApiKeyResponse getValidatedApiKeyForBusinessEmail(String businessEmail){
        try {
            String apiKey = validateApiKey(businessEmail);

            UiApiKeyResponse uiApiKeyResponse = new UiApiKeyResponse();
            uiApiKeyResponse.setApiKey(apiKey);
            uiApiKeyResponse.setCustomerEmail(businessEmail);

            log.info("API key validated for email: {}", businessEmail);
            return uiApiKeyResponse;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public UiApiKeyResponse resetAndSendApiKey(String businessEmail){
        try {
            String resetApiKey = resetApiKey(businessEmail);

            BusinessCustomerApiKey updatedCustomer = getBusinessCustomerApiKeyByEmail(businessEmail);
            UUID customerId = updatedCustomer.getCustomerId();

            UiApiKeyResponse uiApiKeyResponse = new UiApiKeyResponse();
            uiApiKeyResponse.setCustomerEmail(businessEmail);
            uiApiKeyResponse.setApiKey(resetApiKey);

            CustomerServiceApiKeyDto dto = new CustomerServiceApiKeyDto();
            dto.setCustomer_id(customerId);
            dto.setApi_key(resetApiKey);
            sendApiKeyToCustomerService(dto);

            return uiApiKeyResponse;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    public String resetApiKey(String customerEmail) {
        BusinessCustomerApiKey businessCustomerApiKey = businessCustomerApiKeyRepository.findByCustomerEmail(customerEmail);
        UUID id = businessCustomerApiKey.getCustomerId();
        String apiKey = generateApiKey(id);
        businessCustomerApiKey.setApiKey(apiKey);
        businessCustomerApiKeyRepository.save(businessCustomerApiKey);
        return apiKey;

    }

    public boolean isCustomerExistFindByCustomerEmail(String customerEmail) {
        BusinessCustomer businessCustomer = businessCustomerRepository.findByCustomerProfileCustomerEmail(customerEmail);
        return !Objects.isNull(businessCustomer);
    }


    public BusinessCustomerApiKey getBusinessCustomerApiKeyByEmail(String email) {
        return businessCustomerApiKeyRepository.findByCustomerEmail(email);
    }

    public boolean apiKeyAlreadyGeneratedForId(UUID customerId) {
        BusinessCustomerApiKey businessCustomerApiKey = businessCustomerApiKeyRepository.findById(customerId).orElse(null);
        return !(businessCustomerApiKey == null);
    }

    public BusinessCustomer findByCustomerId(UUID customerId) {
        Optional<BusinessCustomer> businessCustomer = businessCustomerRepository.findByCustomerId(customerId);
        return businessCustomer.orElse(null);
    }

    public void sendTokenToCustomerService(String email, String token) {
        CustomerServiceTokenDto customerServiceTokenDto = new CustomerServiceTokenDto(email, token);
        webClient.post().uri("/auth/customer/getToken").bodyValue(customerServiceTokenDto).retrieve().bodyToMono(String.class).doOnSuccess(response -> System.out.println("Token sent to customer service")).doOnError(error -> {
            error.printStackTrace();
        }).subscribe();
    }
}
