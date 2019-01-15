package com.example.bootopen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
class BootopenApplication {

//    @Bean
//    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        factory.setConcurrency(10);
//        factory.getContainerProperties().setPollTimeout(1500);
//        return factory;
//    }
//
//    public ConsumerFactory<String, String> consumerFactory() {
//        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
//    }
//
//    public Map<String, Object> consumerConfigs() {
//        Map<String, Object> propsMap = new HashMap<>();
//        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.7.111.170:2181,10.7.111.171:2181,10.7.111.172:2181/mqcluster0");
//        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
//        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 100);
//        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS, 2000);
//        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, "pay-trade-notifier-t1");
//        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
//        return propsMap;
//    }
//    /**
//     * kafka监听
//     * @return
//     */
//    @Bean
//    public TradeKafkaListener listener() {
//        return new TradeKafkaListener();
//    }

    public static void main(String[] args) {
        SpringApplication.run(BootopenApplication.class, args);
    }

}

