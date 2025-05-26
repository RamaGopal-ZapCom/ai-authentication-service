package com.zapcom.model.CustomerServiceDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Primary {
public String name;
public String phone;
public String address;
public String timeZone;
}
