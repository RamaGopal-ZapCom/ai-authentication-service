package com.zapcom.model.RequestFields;

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
