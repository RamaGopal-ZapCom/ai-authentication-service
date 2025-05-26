package com.zapcom.mapper;

import com.zapcom.entity.BusinessCustomer;
import com.zapcom.model.RequestFields.AdminDetails;
import com.zapcom.model.RequestFields.Agreements;
import com.zapcom.model.RequestFields.ApiConfiguration;
import com.zapcom.model.RequestFields.BankingDetails;
import com.zapcom.model.RequestFields.BotPurpose;
import com.zapcom.model.RequestFields.Branding;
import com.zapcom.model.RequestFields.BusinessDetails;
import com.zapcom.model.RequestFields.ChatbotConfig;
import com.zapcom.model.RequestFields.CustomerProfile;
import com.zapcom.model.RequestFields.LegalAndTaxCompliance;
import com.zapcom.model.RequestFields.OperatingHours;
import com.zapcom.model.RequestFields.Operations;
import com.zapcom.model.RequestFields.Primary;
import com.zapcom.model.RequestFields.RegisteredAddress;
import com.zapcom.model.RequestFields.Staff;
import com.zapcom.model.RequestFields.Technical;
import com.zapcom.model.RequestFields.Theme;
import com.zapcom.model.request.CustomerRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface CustomerMapper {
CustomerMapper instance= Mappers.getMapper(CustomerMapper.class);
@Mapping(target = "customerId",ignore = true)
BusinessCustomer toEntity(CustomerRequest request);
com.zapcom.entity.CustomerFields.AdminDetails toEntity(AdminDetails adminDetails);
com.zapcom.entity.CustomerFields.Agreements toEntity(Agreements agreements);
com.zapcom.entity.CustomerFields.ApiConfiguration toEntity(ApiConfiguration apiConfiguration);
com.zapcom.entity.CustomerFields.BankingDetails toEntity(BankingDetails bankingDetails);
com.zapcom.entity.CustomerFields.BotPurpose toEntity(BotPurpose botPurpose);
com.zapcom.entity.CustomerFields.Branding toEntity(Branding branding);
com.zapcom.entity.CustomerFields.BusinessDetails toEntity(BusinessDetails businessDetails);
com.zapcom.entity.CustomerFields.ChatbotConfig toEntity(ChatbotConfig chatbotConfig);
com.zapcom.entity.CustomerFields.CustomerProfile toEntity(CustomerProfile customerProfile);
com.zapcom.entity.CustomerFields.LegalAndTaxCompliance toEntity(LegalAndTaxCompliance legalAndTaxCompliance);
com.zapcom.entity.CustomerFields.OperatingHours toEntity(OperatingHours operatingHours);
com.zapcom.entity.CustomerFields.Operations toEntity(Operations operations);
com.zapcom.entity.CustomerFields.Primary toEntity(Primary primary);
com.zapcom.entity.CustomerFields.RegisteredAddress toEntity(RegisteredAddress registeredAddress);
com.zapcom.entity.CustomerFields.Staff toEntity(Staff staff);
com.zapcom.entity.CustomerFields.Technical toEntity(Technical technical);
com.zapcom.entity.CustomerFields.Theme toEntity(Theme theme);
}
