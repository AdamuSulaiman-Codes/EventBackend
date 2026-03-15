package com.achlys20.EventBackend.Email;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendTicketEmail(
            String toEmail,
            String attendeeName,
            String eventTitle,
            LocalDateTime eventDateTime,
            byte[] qrCodeBytes
    ) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Set recipient and subject
            helper.setTo(toEmail);
            helper.setSubject("Your Ticket - " + eventTitle);

            // Format event date and time nicely
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy 'at' hh:mm a");
            String formattedDateTime = eventDateTime.format(formatter);

            // Email body with event time and signature
            String emailBody = "Hi " + attendeeName + ",\n\n" +
                    "You're registered for " + eventTitle + ".\n" +
                    "Event Date & Time: " + formattedDateTime + "\n\n" +
                    "Show the attached QR code at the entrance.\n\n" +
                    "See you there!\n\n" +
                    "Sent by Event Hub Achlys20";

            helper.setText(emailBody, false); // false = plain text

            // Attach QR code image
            helper.addAttachment(
                    "ticket-qr.png",
                    new ByteArrayResource(qrCodeBytes),
                    "image/png"
            );

            // Send the email
            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send ticket email", e);
        }
    }
}