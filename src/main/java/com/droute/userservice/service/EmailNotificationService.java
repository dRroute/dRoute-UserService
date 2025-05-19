package com.droute.userservice.service;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {

	private final String fromEmail = "droute.info@gmail.com"; // Sender's email
	private final String appPassword = "hccg ztfg tfrm aoct"; // App password

	private final Logger logger = LoggerFactory.getLogger(EmailNotificationService.class);

	// Send Invitation message to the provided email address
	/*
	 * public boolean sendInviteNotification(String recipientEmail, String
	 * senderName) {
	 * 
	 * Properties properties = new Properties(); properties.put("mail.smtp.host",
	 * "smtp.gmail.com"); properties.put("mail.smtp.port", "587");
	 * properties.put("mail.smtp.auth", "true");
	 * properties.put("mail.smtp.starttls.enable", "true");
	 * 
	 * 
	 * // Set up the session Session session = Session.getInstance(properties, new
	 * Authenticator() {
	 * 
	 * @Override protected PasswordAuthentication getPasswordAuthentication() {
	 * return new PasswordAuthentication(fromEmail, appPassword); } });
	 * 
	 * try { // Create a message Message message = new MimeMessage(session);
	 * message.setFrom(new InternetAddress(fromEmail));
	 * message.setRecipients(Message.RecipientType.TO,
	 * InternetAddress.parse(recipientEmail));
	 * message.setSubject(senderName+" Invited you to to join us on Hisab-Kitab!");
	 * String invitationTemplate = """ Hi ,
	 * 
	 * I’d love for you to join me on Hisab Kitab to simplify tracking and sharing
	 * expenses!
	 * 
	 * 👉 [Sign up here](%s)
	 * 
	 * Let’s make managing expenses easier together.
	 * 
	 * Cheers, %s """;
	 * 
	 * 
	 * 
	 * String signupUrl = "http://localhost:3000/signup"; // Replace with actual
	 * sign-up URL
	 * 
	 * String invitationMessage = String.format(invitationTemplate, signupUrl,
	 * senderName);
	 * 
	 * System.out.println(invitationMessage);
	 * 
	 * 
	 * message.setText(invitationMessage);
	 * 
	 * // Send the email Transport.send(message);
	 * 
	 * // Log success logger.info("Invitation sent to: " + recipientEmail);
	 * 
	 * return true; } catch (MessagingException e) { e.printStackTrace();
	 * logger.error("Failed to send Invitation to: " + recipientEmail); return
	 * false; } }
	 * 
	 */

	public String sendOtpNotification(String recipientEmail) throws SendFailedException, com.sun.mail.smtp.SMTPAddressFailedException {
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
