package com.wf.kafka.test.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {
      @Autowired
     KafkaTemplate<Object, Object> kafkaTemplate;

    @GetMapping("/send/{messge}")
    public String send(@PathVariable String messge) {
        kafkaTemplate.send("la-topic1-test", "la-topic1-test:" + messge);
        kafkaTemplate.send("la-topic2-test", "la-topic2-test:" + messge);
        return messge;
    }

}
