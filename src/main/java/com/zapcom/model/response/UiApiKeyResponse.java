package com.zapcom.model.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UiApiKeyResponse {
    String customerEmail;
    String apiKey;
}
