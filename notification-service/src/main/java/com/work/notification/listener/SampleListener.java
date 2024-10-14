package com.work.notification.listener;

import com.work.notification.dto.WinnerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;

@Service
public class SampleListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final JavaMailSender mailSender; // Injected JavaMailSender

    // Constructor injection for JavaMailSender
    public SampleListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.groupId}")
    public void listen(String message) {
        try {
            // Create a Gson instance
            Gson gson = new Gson();

            // Convert the JSON string to WinnerDTO
            WinnerDTO winner = gson.fromJson(message, WinnerDTO.class);

            // Use the WinnerDTO object as needed
            logger.info("Received message from Kafka: {}", message);
            logger.info("Received message from Kafka: {}", winner);

            // Send email notification
            sendEmailNotification(winner.getEmailId(), winner);

            // TODO: call payment service to process payment

            // Add your processing logic here, if any
        } catch (Exception e) {
            logger.error("Error processing message from Kafka: {}", message, e);
            // Optional: handle the exception, e.g., send to a dead-letter topic or log the issue
        }
    }

    private void sendEmailNotification(String emailId, WinnerDTO winner) {
        // Create a simple email message
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(emailId);
        mailMessage.setSubject("Congratulations! You've Won!");
        mailMessage.setText(String.format("Dear Winner,\n\n" +
                "Congratulations! You have won the product with ID: %s.\n" +
                "Your winning bid amount is: %.2f.\n\n" +
                "Please proceed with the payment to claim your prize.\n\n" +
                "Payment instructions will be sent to you shortly.\n\n" +
                "Best regards,\n" +
                "Your Company", winner.getProductId(), winner.getWinningBidAmount()));

        // Send the email
        mailSender.send(mailMessage);
        logger.info("Email sent to: {}", emailId);
    }
}
