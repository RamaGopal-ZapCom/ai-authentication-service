package com.zapcom.model.request;

import com.zapcom.model.RequestFields.AdminDetails;
import com.zapcom.model.RequestFields.Agreements;
import com.zapcom.model.RequestFields.ApiConfiguration;
import com.zapcom.model.RequestFields.BankingDetails;
import com.zapcom.model.RequestFields.Branding;
import com.zapcom.model.RequestFields.ChatbotConfig;
import com.zapcom.model.RequestFields.CustomerProfile;
import com.zapcom.model.RequestFields.LegalAndTaxCompliance;
import com.zapcom.model.RequestFields.Operations;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {
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
