package com.zapcom.repository;

import com.zapcom.entity.BusinessCustomerCredentials;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessCustomerCredentialsRepository extends MongoRepository<BusinessCustomerCredentials, String> {
    BusinessCustomerCredentials findByCustomerEmail(String customerEmail);


}
