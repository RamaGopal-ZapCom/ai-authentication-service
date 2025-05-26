package com.zapcom.service.impl;

import com.zapcom.configuration.UserDetails;
import com.zapcom.constants.ErrorMessages;
import com.zapcom.entity.BusinessCustomerCredentials;
import com.zapcom.repository.BusinessCustomerCredentialsRepository;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private BusinessCustomerCredentialsRepository businessCustomerCredentialsRepository;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String email) {
        BusinessCustomerCredentials customer = businessCustomerCredentialsRepository.findByCustomerEmail(email);
        if (Objects.isNull(customer))
            throw new UsernameNotFoundException(ErrorMessages.CUSTOMER_NOT_FOUND);
        return new UserDetails(customer);
    }
}

