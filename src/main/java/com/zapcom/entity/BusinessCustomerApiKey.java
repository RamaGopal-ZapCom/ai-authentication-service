package com.zapcom.entity;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "api_keys")
public class BusinessCustomerApiKey {
    @Id
    private UUID customerId;
    private String apiKey;
    private String customerEmail;
}
