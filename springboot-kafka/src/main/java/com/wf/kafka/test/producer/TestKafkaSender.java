package com.wf.kafka.test.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wf
 * @Date: 2020-02-29 10:05
 * @describe kafka发送消息
 */
@RestController
public class TestKafkaSender {

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Value("${kafka.test.topic}")
    String testTopic;

    public void sendMsg(String msg) {
    }

    @GetMapping("/send1/{messge}")
    public String send(@PathVariable String messge) {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            System.out.println("send message = " + i);
            kafkaTemplate.send(testTopic, messge + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return messge;
            }
        }
        return "ok";
    }
}