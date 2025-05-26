package com.zapcom.model.RequestFields;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerProfile {
private String CustomerName;
private String industryType;
private String businessCategory;
private RegisteredAddress registeredAddress;
private String country;
private String gstNumber;
private String customerWebsite;
private String customerRegistrationNumber;
private String ownerName;
private String contactNumber;
private String customerEmail;
}
