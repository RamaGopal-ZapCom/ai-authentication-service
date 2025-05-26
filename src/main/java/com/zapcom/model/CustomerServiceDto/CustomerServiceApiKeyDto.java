package com.zapcom.model.CustomerServiceDto;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class CustomerServiceApiKeyDto {
    private String api_key;
    private UUID customer_id;
}
