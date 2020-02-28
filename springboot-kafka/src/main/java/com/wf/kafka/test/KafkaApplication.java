package com.wf.kafka.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class KafkaApplication {
    public static void main(String[] args) {

        SpringApplication.run(KafkaApplication.class);
        System.out.println("KafkaApplication 服务启动成功！");
    }
}
