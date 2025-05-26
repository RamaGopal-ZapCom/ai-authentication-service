package com.zapcom.model.RequestFields;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Operations {
    private OperatingHours operatingHours;
    private List<String> cuisineType;
    private String deliveryRadiusPreference;
    private Staff staff;
    private List<String> websiteOrSocialMediaLinks;
}
