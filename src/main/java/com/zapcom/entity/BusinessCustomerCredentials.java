package com.zapcom.entity;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "customer_credentials")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessCustomerCredentials {
    @Id
    private String id;
    @Indexed(unique = true)
    private String customerEmail;
    private String password;
    private UUID customerId;
}
