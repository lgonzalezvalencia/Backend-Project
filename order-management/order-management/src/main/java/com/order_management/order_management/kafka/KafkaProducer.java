package com.order_management.order_management.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

  private static final String TOPIC = "order_topic";

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  public void sendMessage(String message) {
    kafkaTemplate.send(TOPIC, message);
  }
}
