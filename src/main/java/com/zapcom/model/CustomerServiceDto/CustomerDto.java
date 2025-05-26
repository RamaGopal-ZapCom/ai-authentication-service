package com.zapcom.model.CustomerServiceDto;


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


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
    private UUID customer_id;
    private CustomerProfile customerProfile;
    private LegalAndTaxCompliance legalAndTaxCompliance;
    private BankingDetails bankingDetails;
    private AdminDetailsWithoutPassword adminDetails;
    private Map<String, String> metaData;
    private Operations operations;
    private ApiConfiguration apiConfiguration;
    private Branding branding;
    private ChatbotConfig chatbotConfig;
    private Agreements agreements;

}
