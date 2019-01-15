package com.example.bootopen.common.utils.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.serializer.Decoder;
import kafka.utils.VerifiableProperties;
import lombok.extern.slf4j.Slf4j;

import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TradeKafkaConsumer {

	private static ConsumerConnector consumerCnn;
	private static String topic = "pospmtest";

	private static ConsumerConfig getConCfg() {
		Properties props = new Properties();
		// zookeeper 配置
		props.put("zookeeper.connect",
				"10.7.111.170:2181,10.7.111.171:2181,10.7.111.172:2181/mqcluster0");
		// group 代表一个消费组
		props.put("group.id", "pay-trade-notifier-11t");

		// zk连接超时
		props.put("zookeeper.session.timeout.ms", "4000");
		props.put("zookeeper.sync.time.ms", "2000");
		props.put("auto.commit.interval.ms", "1000");
		// props.put("auto.offset.reset", "smallest");
		props.put("auto.offset.reset", "largest");

		// 配置value的序列化类
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		// 配置key的序列化类
		props.put("key.serializer.class", "kafka.serializer.StringEncoder");

		return new ConsumerConfig(props);
	}

	public static void consume() {
		consumerCnn = Consumer.createJavaConsumerConnector(getConCfg());
		// 指定需要订阅的topic
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, new Integer(20));
		// 指定key的编码格式
		Decoder<String> keyDecoder = new kafka.serializer.StringDecoder(
				new VerifiableProperties());
		// 指定value的编码格式
		Decoder<String> valueDecoder = new kafka.serializer.StringDecoder(
				new VerifiableProperties());
		// 获取topic 和 接受到的stream 集合
		Map<String, List<KafkaStream<String, String>>> map = consumerCnn
				.createMessageStreams(topicCountMap, keyDecoder, valueDecoder);
		// 根据指定的topic 获取 stream 集合
		List<KafkaStream<String, String>> kafkaStreams = map.get(topic);
		ExecutorService executor = Executors.newFixedThreadPool(2);

		// 因为是多个 message组成 message set ， 所以要对stream 进行拆解遍历
		for (final KafkaStream<String, String> kafkaStream : kafkaStreams) {
			executor.submit(new Runnable() {
				@Override
				public void run() {
					// 拆解每个的 stream
					ConsumerIterator<String, String> iterator = kafkaStream
							.iterator();
					while (iterator.hasNext()) {
						// messageAndMetadata 包括了 message ， topic ，
						// partition等metadata信息
						MessageAndMetadata<String, String> messageAndMetadata = iterator
								.next();
						System.out.println("message============= : "
								+ messageAndMetadata.message()
								+ "  partition :  "
								+ messageAndMetadata.partition());
					}
				}
			});
		}

	}

	public static void cnnKafka() {
		int consumerCount = 1;
		try {
			// ConsumerConfig consumerConfig = new
			// kafka.consumer.ConsumerConfig(props);
			ConsumerConnector consumerConnector = kafka.consumer.Consumer
					.createJavaConsumerConnector(getConCfg());
			Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
			topicCountMap.put(topic, consumerCount);
			Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector
					.createMessageStreams(topicCountMap);

			ExecutorService pool = Executors.newFixedThreadPool(2);
			consumerMap.get(topic).stream().forEach(stream -> {
				pool.submit(new Runnable() {
					@Override
					public void run() {
						ConsumerIterator<byte[], byte[]> it = stream.iterator();
						// it.hasNext()取决于consumer.timeout.ms的值,默认为-1
						try {
							while (it.hasNext()) {

								MessageAndMetadata<byte[], byte[]> messageAndMetaData = it
										.next();
								byte[] body = messageAndMetaData.message();
								String topic = messageAndMetaData.topic();
								byte[] key = (byte[]) messageAndMetaData.key();
								// byte[] messageValue =
								// (byte[])messageAndMetaData.message();
								// Long offset = messageAndMetaData.offset();
								// int partitionNum =
								// messageAndMetaData.partition();

								String message = StringUtils
										.newStringUtf8(body);
								String key1 = (key != null && key.length > 0) ? StringUtils
										.newStringUtf8(key) : null;

								System.out.println("===================="
										+ message);
								System.out.println("===================="
										+ topic);
								System.out.println("===================="
										+ key1);

								// System.out.println(Thread.currentThread().getName()+" hello");
								// 是hasNext抛出异常,而不是next抛出
								// System.out.println(Thread.currentThread().getName()+":"+new
								// String(it.next().message()));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						System.out.println(Thread.currentThread().getName()
								+ " end");
					}
				});

			});

		} catch (Exception e) {
			log.error("Exception", e);
		}
	}

	public static void main(String[] args) {
		consume();
		// cnnKafka();
	}

}
