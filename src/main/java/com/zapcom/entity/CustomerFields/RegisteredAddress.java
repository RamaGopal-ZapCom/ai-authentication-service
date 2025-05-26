package com.zapcom.entity.CustomerFields;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisteredAddress {
private String street;
private String city;
private String state;
private String pinCode;
}
