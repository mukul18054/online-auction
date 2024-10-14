package com.work.notification.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController("/v1/auction/notification")
public class SampleKafkaMessageController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private KafkaTemplate<String, String> kafkaSender;

    @PostMapping("/sentMessage")
    public String sendMessage(@RequestParam String kafkaKey, @RequestParam String message) {
        try {
            this.kafkaSender.sendDefault(kafkaKey, message);
            return String.format("kafka message sent successfully with key: %s, message: %s" , kafkaKey, message);
        } catch (Exception e) {
            logger.error("error in sending kafka message", e);
            return "error in sending kafka message";
        }
    }
}
