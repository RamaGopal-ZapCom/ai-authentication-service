package com.zapcom.model.RequestFields;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Branding {
private String logoUrl;
private String brandName;
private String greetingMessage;
private String fallBackResponse;
}
