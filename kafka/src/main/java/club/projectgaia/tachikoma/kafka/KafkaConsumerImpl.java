package club.projectgaia.tachikoma.kafka;

import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

/**
 * @author Phoenix Luo
 * @version 2021/2/18
 **/
public class KafkaConsumerImpl implements Consumer {
    private Properties properties;
    private KafkaConsumer<String, String> consumer;
    private final String topic = "test32";
    private volatile boolean flag = true;
    
    public KafkaConsumerImpl() {
        properties = new Properties();
//        properties.put("enable.auto.commit", false);
//        properties.put("isolation.level", "read_committed");
//        properties.put("auto.offset.reset", "latest");
        // 组
        properties.put("group.id", "group1");
        properties.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer(properties);
    }
    
    @Override
    public void consumeOrder() {
        consumer.subscribe(Collections.singletonList(topic));
        try {
            while (true) { //拉取数据
                ConsumerRecords<String, String> poll = consumer.poll(1000L);
                for (ConsumerRecord o : poll) {
                    ConsumerRecord<String, String> record = (ConsumerRecord) o;
                    System.out.println("key=" + record.key() + " value=" + record.value());
//                    deduplicationOrder(order);
//                    currentOffsets.put(new TopicPartition(record.topic(), record.partition()),
//                            new OffsetAndMetadata(record.offset() + 1, "no matadata"));
//                    consumer.commitAsync(currentOffsets, new OffsetCommitCallback() {
//                        @Override
//                        public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
//                            if (exception != null) {
//                                exception.printStackTrace();
//                            }
//                        }
//                    });
                }
            }
        } catch (CommitFailedException e) {
            e.printStackTrace();
        } finally {
            try {
                // consumer.commitSync();//currentOffsets);
            } catch (Exception e) {
                consumer.close();
            }
        }
    }
    
    @Override
    public void close() {
        if (this.flag) {
            this.flag = false;
        }
        consumer.close();
    }
    
    public static void main(String[] args) {
        KafkaConsumerImpl consumer = new KafkaConsumerImpl();
        consumer.consumeOrder();
    }
    
}
