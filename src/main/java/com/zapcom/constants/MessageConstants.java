package com.zapcom.constants;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageConstants {
    public static final String CUSTOMER_ALREADY_EXISTS =
            "Customer with this mail already exists. \nIf you haven't verified your account yet, please check your mail for the verification link.";
    public static final String EMAIL_VERIFICATION =
            "Email is not verified yet";
    public static final String LOGOUT_MESSAGE="You have been logged out";
    public static final String APIKEY_VERIFICATION="API_KEY received";
    public static final String VERIFICATION_SUCCESSFUL = "Verification successful";
    public static final String REGISTRATION_SUCCESSFUL = "Registration successful! Please check your email to verify your account.";
    public static final String EMAIL_MESSAGE = "zapcom-email-verification";
    public static final String FAILED_TO_SEND_EMAIL="Failed to send email ";
    public static final String EMAIL_DOES_NOT_EXIST="The email you provided doesn't exist!!\\n Please check once.";
}
