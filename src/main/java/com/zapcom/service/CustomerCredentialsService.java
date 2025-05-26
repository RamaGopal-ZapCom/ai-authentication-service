package com.zapcom.service;

import com.zapcom.entity.BusinessCustomer;
import org.springframework.security.core.userdetails.UserDetails;

public interface CustomerCredentialsService {
    void save(BusinessCustomer businessCustomerCopy);

    UserDetails findByEmail(String username);
}
