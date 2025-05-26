package com.zapcom.model.RequestFields;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BusinessDetails {
private String companyName;
private String customerEmail;
private String companyRegNo;
private String pincode;
private String city;
private String ownerName;
private long contactNumber;
private String industryType;
private String  businessCategory;
private String state;
}
