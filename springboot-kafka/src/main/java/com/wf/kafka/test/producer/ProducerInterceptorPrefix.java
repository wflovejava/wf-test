package com.wf.kafka.test.producer;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * @Author: wf
 * @Date: 2020-03-02 10:16
 * @describe 生产者拦截器，给消息统一添加前缀
 */
public class ProducerInterceptorPrefix implements ProducerInterceptor {
    private volatile long sendSuccess = 0; //发送成功消息数
    private volatile long sendFailure = 0; //发送失败消息数
    @Override
    public ProducerRecord onSend(ProducerRecord record) {

        String modifiedValue = "prefix1-" + record.value();

        return new ProducerRecord<>(record.topic(),
                record.partition(), record.timestamp(),
                record.key(), modifiedValue, record.headers());
    }

    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
        if (e == null) {
            sendSuccess++;
        } else {
            sendFailure ++;
        }
    }

    @Override
    public void close() {
        double successRatio = (double)sendSuccess / (sendFailure + sendSuccess);
        System.out.println("[INFO] 发送成功率="
                + String.format("%f", successRatio * 100) + "%");
    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
