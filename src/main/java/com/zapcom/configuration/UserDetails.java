package com.zapcom.configuration;

import com.zapcom.entity.BusinessCustomerCredentials;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;

;

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private final BusinessCustomerCredentials customer;

    public UserDetails(BusinessCustomerCredentials customer) {
        this.customer = customer;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public String getUsername() {
        return customer.getCustomerEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

