package com.wf.kafka.test.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SimpleListener2 {
    @KafkaListener(topics = {"${kafka.test.topic}"},groupId = "wf2")
    public void listen1(String data) {
        System.out.println("groupId = wf2, message = " +data);
    }
}
