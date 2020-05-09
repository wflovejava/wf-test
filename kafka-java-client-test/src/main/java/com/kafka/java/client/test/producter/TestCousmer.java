package com.kafka.java.client.test.producter;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

/**
 * @Author: wf
 * @Date: 2020-03-03 14:58
 * @describe 消费消息
 */
public class TestCousmer {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers","127.0.0.1:9092");
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer",StringDeserializer.class.getName());
        properties.setProperty("group.id","1111");
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Collections.singletonList("test-topic"));
        try {
            while (true){
                ConsumerRecords<String, String> poll = consumer.poll(500);
                for (ConsumerRecord<String, String> stringStringConsumerRecord : poll) {
                    System.out.println(stringStringConsumerRecord);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //kafka优雅关闭，比如kafka关闭了在执行分区再平衡
            consumer.close();
        }
    }
}
