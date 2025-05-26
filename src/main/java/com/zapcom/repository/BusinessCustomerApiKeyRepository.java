package com.zapcom.repository;

import com.zapcom.entity.BusinessCustomerApiKey;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BusinessCustomerApiKeyRepository extends MongoRepository<BusinessCustomerApiKey, UUID> {
    BusinessCustomerApiKey findByCustomerEmail(String customerEmail);

}
