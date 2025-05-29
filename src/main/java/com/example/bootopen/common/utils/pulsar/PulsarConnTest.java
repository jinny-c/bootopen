package com.example.bootopen.common.utils.pulsar;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;

/**
 * @author chaijd
 * @description TODO
 * @date 2024-07-03
 */
@Slf4j
public class PulsarConnTest {

    static String pulsarCluster = "pulsar://192.168.130.199:6650";
    static String topicName = "persistent://public/demo/persistent";
    //接入前需要与后台申请token
    static String token = "";

    static void produceObjectDemo() {
        try {
            PulsarClient pulsarClient = PulsarClient
                    .builder()
                    .serviceUrl(pulsarCluster)
                    //.authentication(AuthenticationFactory.token(token))
                    .build();

            //Customer为java中一个对象类可以自定义
            Producer<Customer> producer = pulsarClient.newProducer(Schema.AVRO(Customer.class)).topic(topicName).create();
            //发送100条消息
            for (int i = 0; i < 10; i++) {
                Customer c = new Customer(i, "名字" + i, "上海市", i);
                MessageId messageId = producer.newMessage().value(c).send();
                log.info("Send message success, messageId: {}", messageId);
            }
            producer.close();
            pulsarClient.close();
            //使用完后，千万记得关闭！
            producer.close();
            pulsarClient.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("PulsarProducer error", e);
            System.out.println("PulsarProducer error");
        }
    }

    static void produceByteDemo() {
        try {
            PulsarClient pulsarClient = PulsarClient
                    .builder()
                    .serviceUrl(pulsarCluster)
                    //.authentication(AuthenticationFactory.token(token))
                    .build();

            Producer<byte[]> producer  = pulsarClient.newProducer().topic(topicName).create();
            for (int i = 0; i< 10 ; i++) {
                MessageId messageId = producer.newMessage().value(String.format("测试数据%s" , i).getBytes()).send();
                log.info("Send message success, messageId: {}", messageId);
            }

            producer.close();
            pulsarClient.close();
            //使用完后，千万记得关闭！
            producer.close();
            pulsarClient.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("PulsarProducer error", e);
            System.out.println("PulsarProducer error");
        }
    }

    public static void main(String[] args) {
        produceObjectDemo();
        produceByteDemo();
        System.out.println("PulsarProducer success");
    }

}
