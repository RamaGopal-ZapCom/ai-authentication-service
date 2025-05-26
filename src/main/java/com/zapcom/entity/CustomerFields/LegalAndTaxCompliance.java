package com.zapcom.entity.CustomerFields;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LegalAndTaxCompliance {
private String gstNumber;
private String panCard;
private String fssaiLicense;
private String shopsAndEstablishmentLicense;
private String tradeLicense;
}
