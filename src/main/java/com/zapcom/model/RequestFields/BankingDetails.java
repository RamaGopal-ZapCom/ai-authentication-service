package com.zapcom.model.RequestFields;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BankingDetails {
private long bankAccountNumber;
private String bankName;
private String ifscCode;
private BankingDocument cancelledChequeOrBankStatement;
}
