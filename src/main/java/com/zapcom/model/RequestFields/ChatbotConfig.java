package com.zapcom.model.RequestFields;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class ChatbotConfig {
    private Theme theme;
    private String chatWidgetPosition;
    private List<String> supportedLanguages;
}
