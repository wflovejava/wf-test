package com.wf.kafka.test.producer;

import org.apache.kafka.clients.producer.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * @Author: wf
 * @Date: 2020-02-29 16:44
 * @describe
 */
public class ProducerFastStart {
    public static final String brokerList = "localhost:9092";
    public static final String topic = "topic-demo";

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("bootstrap.servers", brokerList);
        properties.put(ProducerConfig.RETRIES_CONFIG,10);//如果重试了10次之后还没有恢复，那么仍会抛出异常，进而发送的外层逻辑就要处理这些异常了。

        properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,
                ProducerInterceptorPrefix.class.getName());//添加拦截器

        KafkaProducer<String, String> producer =
                new KafkaProducer<>(properties);
        ProducerRecord<String, String> record =
                new ProducerRecord<>(topic, "hello, Kafka!");
        try {

            // 发送消息主要有三种模式：发后即忘（fire-and-forget）、同步（sync）及异步（async）。
            /**
             * 同步（sync）: 同步发送的方式可靠性高 要么消息被发送成功 ,不过同步发送的方式的性能会差很多，需要阻塞等待一条消息发送完之后才能发送下一条。
             * 异步（async）:
             * 发后即忘（fire-and-forget）: 直接造成消息的丢失,但是性能高
             */

            //使用案例=====================================================
            //1.同步发送 api
            Future<RecordMetadata> send = producer.send(record);
            RecordMetadata metadata = send.get();//获取消息元数据，需要阻塞等待一条消息发送完之后才能发送下一条。
            System.out.println(metadata.topic() + "-" +
                    metadata.partition() + ":" + metadata.offset() + " timestamp: "+metadata.timestamp());

            //2.异步发送 api。  Kafka 有响应时就会回调，要么发送成功，要么抛出异常。
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception exception) {
                    if (exception != null) {
                        exception.printStackTrace();
                    } else {
                        System.out.println(metadata.topic() + "-" +
                                metadata.partition() + ":" + metadata.offset());
                    }
                }
            });

            //3. 发后即忘 api
                producer.send(record);

        } catch (Exception e) {
            e.printStackTrace();
        }
        producer.close();
    }

    public static void test(Long time){
        Timestamp ts = new Timestamp(time);
        String tsStr = "";
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            //方法一
            tsStr = sdf.format(ts);
            System.out.println(tsStr);
            //方法二
            tsStr = ts.toString();
            System.out.println(tsStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
