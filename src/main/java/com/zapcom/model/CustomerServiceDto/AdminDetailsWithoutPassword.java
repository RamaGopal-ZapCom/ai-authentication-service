package com.zapcom.model.CustomerServiceDto;

import com.zapcom.entity.CustomerFields.Primary;
import com.zapcom.entity.CustomerFields.Technical;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminDetailsWithoutPassword {
private Technical technical;
private Primary primary;
}
