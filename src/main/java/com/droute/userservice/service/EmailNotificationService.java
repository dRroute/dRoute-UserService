package com.droute.userservice.service;

import java.util.Objects;
import java.util.Properties;
import java.util.Random;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.droute.userservice.dto.request.EmailNotificationRequestDto;

@Service
public class EmailNotificationService {

	private final String fromEmail = "droute.info@gmail.com"; // Sender's email
	private final String appPassword = "hccg ztfg tfrm aoct"; // App password

	private final Logger logger = LoggerFactory.getLogger(EmailNotificationService.class);

	public String sendOtpNotification(String recipientEmail)
			throws SendFailedException, com.sun.mail.smtp.SMTPAddressFailedException {
		// SMTP server configuration
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

		// Generate OTP
		String otp = generateOtp();

		// Set up the session
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, appPassword);
			}
		});

		try {
			// Create a message
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
			message.setSubject(" Your 4-digit OTP(One time Password) to join 🚚🚚 dRoute 🚚🚚!");

			// Create the email body
			String emailBody = """
					Hi,

					Your One-Time Password (OTP) for joining 🚚🚚 dRoute 🚚🚚 is: %s

					Use this OTP to complete your signup process.

					Cheers,
					%s
					""";

			String formattedMessage = String.format(emailBody, otp, "🚚🚚 dRoute 🚚🚚");
			message.setText(formattedMessage);

			// Send the email
			Transport.send(message);

			// Log success
			logger.info("OTP sent to: " + recipientEmail);

			// Return the generated OTP
			return otp;
		} catch (MessagingException e) {
			e.printStackTrace();
			logger.error("Failed to send OTP to: " + recipientEmail);
			return null;
		}
	}

	public void sendCustomEmailNotification(
			EmailNotificationRequestDto data) throws SendFailedException, com.sun.mail.smtp.SMTPAddressFailedException {

		// SMTP configuration
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(data.getSenderEmail(), data.getSenderEmailAppPassCode());
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(data.getSenderEmail()));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(data.getReceiverEmail()));

			// CC recipients
			if (data.getCcRecipients() != null && !data.getCcRecipients().isEmpty()) {
				Address[] ccAddresses = data.getCcRecipients().stream()
						.map(email -> {
							try {
								return new InternetAddress(email);
							} catch (AddressException e) {
								logger.warn("Invalid CC address: " + email);
								return null;
							}
						})
						.filter(Objects::nonNull)
						.toArray(Address[]::new);
				message.setRecipients(Message.RecipientType.CC, ccAddresses);
			}

			// BCC recipients
			if (data.getBccRecipients() != null && !data.getBccRecipients().isEmpty()) {
				Address[] bccAddresses = data.getBccRecipients().stream()
						.map(email -> {
							try {
								return new InternetAddress(email);
							} catch (AddressException e) {
								logger.warn("Invalid BCC address: " + email);
								return null;
							}
						})
						.filter(Objects::nonNull)
						.toArray(Address[]::new);
				message.setRecipients(Message.RecipientType.BCC, bccAddresses);
			}

			message.setSubject(data.getEmailSubject());

			message.setText(data.getEmailBody());

			Transport.send(message);
			logger.info("Email successfully sent to" + data.getReceiverEmail());

		} catch (MessagingException e) {
			logger.error("Failed to send email = : " + data.getReceiverEmail(), e);

		}
	}

	private String generateOtp() {
		Random random = new Random();
		int otp = 1000 + random.nextInt(9000); // Generate 4-digit OTP
		return String.valueOf(otp);
	}

}

// App Password Steps :-
// App password will created from manage your google account->security->2-Step
// Verification->App Password-> Create Specific App-> Name="JavaMail
// App"->Create->Copy the 16-character password-> Paste in final String
// appPassword.
// you are good to go then.
