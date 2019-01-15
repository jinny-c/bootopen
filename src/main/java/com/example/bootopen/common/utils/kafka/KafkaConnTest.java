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

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaConnTest implements Runnable {
	public String title;
	public KafkaStream<byte[], byte[]> stream;

	public KafkaConnTest(String title, KafkaStream<byte[], byte[]> stream) {
		this.title = title;
		this.stream = stream;
	}

	@Override
	public void run() {
		ConsumerIterator<byte[], byte[]> it = stream.iterator();
		/**
		 * 不停地从stream读取新到来的消息，在等待新的消息时，hasNext()会阻塞 如果调用
		 * `ConsumerConnector#shutdown`，那么`hasNext`会返回false
		 * */
		while (it.hasNext()) {
			MessageAndMetadata<byte[], byte[]> data = it.next();
			Object topic = data.topic();
			int partition = data.partition();
			long offset = data.offset();
			String msg = new String(data.message());
			System.out
					.println("1111111111111"
							+ String.format(
									"Consumer: [%s],  Topic: [%s],  PartitionId: [%d], Offset: [%d], msg: [%s]",
									title, topic, partition, offset, msg));
		}
		System.out.println(String.format("1111111111111"
				+ "Consumer: [%s] exiting ...", title));
	}

	private static void kafkaSender() throws Exception {
		// Properties props = new Properties();
		// props.put("bootstrap.servers", "localhost:9092");
		// props.put("acks", "all");
		// props.put("retries", 0);
		// props.put("batch.size", 16384);
		// props.put("linger.ms", 1);
		// props.put("buffer.memory", 33554432);
		// props.put("key.serializer",
		// "org.apache.kafka.common.serialization.StringSerializer");
		// props.put("value.serializer",
		// "org.apache.kafka.common.serialization.StringSerializer");
		// CommonClientConfigs ProducerConfig 可以配置的参数都在这些类里面
		Map<String, Object> config = new HashMap<String, Object>();
		// kafka对消息是按照key value形式存储，这里指定key和value的序列化方法
		config.put("key.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");
		config.put("value.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");
		// broker的地址
		config.put("bootstrap.servers",
				"10.7.111.170:2181,10.7.111.171:2181,10.7.111.172:2181/mqcluster0");
		// acks=0 客户端不会等待服务端的确认
		// acks=1 只会等待leader分区的确认
		// acks=all或者acks=-1 等待leader分区和follower分区的确认
		config.put("acks", "all");
		Producer<String, String> producer = new KafkaProducer<String, String>(
				config);

		// 发送业务消息
		// 读取文件 读取内存数据库 读socket端口
		for (int i = 1; i <= 100; i++) {
			Thread.sleep(500);
			// 第一个参数是主题，第二个参数是消息
			producer.send(new ProducerRecord<String, String>("test2", i + ""));
		}
	}

	private static void kafkaConsume() {
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
		props.put("auto.offset.reset", "smallest");
		// props.put("auto.offset.reset", "largest");
		// 配置value的序列化类
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		// 配置key的序列化类
		props.put("key.serializer.class", "kafka.serializer.StringEncoder");
		ConsumerConfig config = new ConsumerConfig(props);
		String topic1 = "pospmtest";

		// 只要ConsumerConnector还在的话，consumer会一直等待新消息，不会自己退出
		ConsumerConnector consumerConn = Consumer
				.createJavaConsumerConnector(config);
		// 定义一个map
		Map<String, Integer> topicCountMap = new HashMap<>();
		topicCountMap.put(topic1, 3);
		// Map<String, List<KafkaStream<byte[], byte[]>> 中String是topic，
		// List<KafkaStream<byte[], byte[]>是对应的流
		Map<String, List<KafkaStream<byte[], byte[]>>> topicStreamsMap = consumerConn
				.createMessageStreams(topicCountMap);
		// 取出 `kafkaTest` 对应的 streams
		List<KafkaStream<byte[], byte[]>> streams = topicStreamsMap.get(topic1);
		// 创建一个容量为4的线程池
		ExecutorService executor = Executors.newFixedThreadPool(1);
		// 创建20个consumer threads
		for (int i = 0; i < streams.size(); i++) {
			executor.execute(new KafkaConnTest("消费者" + (i + 1), streams.get(i)));
		}
	}

	public static void main(String[] args) throws Exception {
		// kafkaSender();
		// kafkaConsume();
	}
}
