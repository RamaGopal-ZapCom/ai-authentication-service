package com.zapcom.repository;

import com.zapcom.entity.BusinessCustomer;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BusinessCustomerRepository extends MongoRepository<BusinessCustomer, UUID> {
    Optional<BusinessCustomer> findByCustomerId(UUID customerId);
    BusinessCustomer findByCustomerProfileCustomerEmail(String customerEmail);

}
