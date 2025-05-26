package com.zapcom.entity;

import com.zapcom.entity.CustomerFields.AdminDetails;
import com.zapcom.entity.CustomerFields.Agreements;
import com.zapcom.entity.CustomerFields.ApiConfiguration;
import com.zapcom.entity.CustomerFields.BankingDetails;
import com.zapcom.entity.CustomerFields.Branding;
import com.zapcom.entity.CustomerFields.ChatbotConfig;
import com.zapcom.entity.CustomerFields.CustomerProfile;
import com.zapcom.entity.CustomerFields.LegalAndTaxCompliance;
import com.zapcom.entity.CustomerFields.Operations;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Data
@NoArgsConstructor
@Document(collection = "business_customer")
@AllArgsConstructor
@CompoundIndex(def = "{'customerProfile.customerEmail':1}", unique = true)
@Builder
public class BusinessCustomer {
    @Id
    @Field("customerId")
    private UUID customerId;
    private CustomerProfile customerProfile;
    private LegalAndTaxCompliance legalAndTaxCompliance;
    private BankingDetails bankingDetails;
    private AdminDetails adminDetails;
    private Map<String, String> metaData;
    private Operations operations;
    private ApiConfiguration apiConfiguration;
    private Branding branding;
    private ChatbotConfig chatbotConfig;
    private Agreements agreements;
}
