package com.zapcom.model.RequestFields;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BankingDocument {
private byte[] data;
private String fileName;
private String fileType;
}
