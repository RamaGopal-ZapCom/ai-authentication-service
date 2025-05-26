package com.zapcom.service.impl;

import com.zapcom.entity.BusinessCustomer;
import com.zapcom.entity.BusinessCustomerCredentials;
import com.zapcom.repository.BusinessCustomerCredentialsRepository;
import com.zapcom.service.CustomerCredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class CustomerCredentialsServiceImpl implements CustomerCredentialsService {
    @Autowired
    private BusinessCustomerCredentialsRepository businessCustomerCredentialsRepository;

    public void save(BusinessCustomer businessCustomerCopy) {
        BusinessCustomerCredentials bcc = BusinessCustomerCredentials.builder()
                .password(businessCustomerCopy.getAdminDetails().getPrimary().getPassword())
                .customerEmail(businessCustomerCopy.getCustomerProfile().getCustomerEmail())
                .customerId(businessCustomerCopy.getCustomerId())
                .build();
        businessCustomerCredentialsRepository.save(bcc);
    }

    public UserDetails findByEmail(String username) {
        return (UserDetails) businessCustomerCredentialsRepository.findByCustomerEmail(username);
    }
}
