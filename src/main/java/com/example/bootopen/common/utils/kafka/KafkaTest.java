package com.example.bootopen.common.utils.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * @author chaijd
 * @description TODO
 * @date 2024-07-05
 */
public class KafkaTest {
    static void producerTest() {
        // 配置Kafka生产者属性
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.130.199:1306");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // 创建生产者
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        // 发送消息
        ProducerRecord<String, String> record = new ProducerRecord<>("test-topic", "key11", "value111");
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception == null) {
                    System.out.printf("Sent message to topic %s partition %d offset %d%n", metadata.topic(), metadata.partition(), metadata.offset());
                } else {
                    exception.printStackTrace();
                }
            }
        });

        // 关闭生产者
        producer.close();
    }

    static void consumerTest() {
        // 配置Kafka消费者属性
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.130.199:1306");
        props.put("group.id", "test-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // 创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("test-topic"));

        // 拉取消息
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("Received message: key = %s, value = %s, topic = %s, partition = %d, offset = %d%n",
                        record.key(), record.value(), record.topic(), record.partition(), record.offset());
            }
        }
    }

    public static void main(String[] args) {
//        producerTest();
        consumerTest();
    }
}
