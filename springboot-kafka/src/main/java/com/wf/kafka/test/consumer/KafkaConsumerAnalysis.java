package com.wf.kafka.test.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: wf
 * @Date: 2020-03-02 11:40
 * @describe 消息消费者
 */
public class KafkaConsumerAnalysis {
    public static final String brokerList = "localhost:9092";
    public static final String topic = "topic-demo";
    public static final String groupId = "group.demo";
    public static final AtomicBoolean isRunning = new AtomicBoolean(true);

    /**
     * 初始化kafka配置
     * @return
     */
    public static Properties initConfig(){
        Properties props = new Properties();
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("bootstrap.servers", brokerList);
        props.put("group.id", groupId);
        props.put("client.id", "consumer.client.id.demo");
        return props;
    }

    public static void main(String[] args) {
        Properties props = initConfig();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));

        try {
            while (isRunning.get()) {
                ConsumerRecords<String, String> records =
                        consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("topic = " + record.topic()
                            + ", partition = "+ record.partition()
                            + ", offset = " + record.offset());
                    System.out.println("key = " + record.key()
                            + ", value = " + record.value());
                    //do something to process record.

                }

                //手动提交，用来解决消费重复的现象
                consumer.commitAsync(); //异步提交本地偏移量，如果失败了不可以重试提交，不会影响性能
//                consumer.commitSync();//同步提交本地偏移量，如果失败了可以重试提交，会影响性能

            }
        } catch (Exception e) {
            e.printStackTrace();
            //log.error("occur exception ", e);
        } finally {
            //解决消费者代码挂了，然后同步提交偏移量
            try{
                consumer.commitSync();
            }catch (Exception e){
               e.printStackTrace();
            }finally {
                consumer.close();
            }

        }
    }

}
