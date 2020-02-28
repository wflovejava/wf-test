package com.wf.kafka.test.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SimpleListener {
    @KafkaListener(topics = {"la-topic1-test", "la-topic2-test"})
    public void listen1(String data) {
        System.out.println(data);
    }
}
