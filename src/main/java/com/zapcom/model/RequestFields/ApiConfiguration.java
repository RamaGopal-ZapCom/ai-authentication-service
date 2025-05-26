package com.zapcom.model.RequestFields;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiConfiguration {
    private int estimatedMonthlyRequests;
    private int requestsPerMinute;
    private String peakUsageHours;
    private BotPurpose botPurpose;
    private List<String> complianceStandards;
}
