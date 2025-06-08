package com.droute.userservice.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmailNotificationRequestDto {

    private String senderEmail;
    private String senderEmailAppPassCode;
    
    private String emailSubject;
    private String emailBody;

    private String receiverEmail;            // Single TO recipient

    private List<String> ccRecipients;       // Optional CC list
    private List<String> bccRecipients;      // Optional BCC list


}
