package com.work.scheduler.producer;
import com.work.scheduler.dto.WinnerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;

@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, WinnerDTO> kafkaTemplate;

    public void sendWinnerNotification(WinnerDTO winner) {
        String message = new Gson().toJson(winner);
        kafkaTemplate.sendDefault("notification-topic",winner);
    }
}
