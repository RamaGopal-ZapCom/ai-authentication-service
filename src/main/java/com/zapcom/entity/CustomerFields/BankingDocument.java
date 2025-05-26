package com.zapcom.entity.CustomerFields;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BankingDocument {
private byte[] data;
private String fileName;
private String fileType;
}
