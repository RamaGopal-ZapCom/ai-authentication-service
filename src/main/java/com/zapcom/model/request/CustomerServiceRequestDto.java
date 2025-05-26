package com.zapcom.model.request;

import com.zapcom.model.CustomerServiceDto.CustomerDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerServiceRequestDto {
private CustomerDto customerDto;
private MultipartFile bankDocument;
}
