package club.projectgaia.tachikoma.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @author Phoenix Luo
 * @version 2021/2/18
 **/
public class KafkaProducerImpl implements Producer {
    private Properties properties;
    private KafkaProducer<String, String> producer;
    private final String topic = "test32";
    
    public KafkaProducerImpl() {
        properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
//        properties.put("queue.enqueue.timeout.ms", -1);
//        properties.put("enable.idempotence", true);
//        properties.put("transactional.id", "transactional_1");
//        properties.put("acks", "all");
//        properties.put("retries", "3");
//        properties.put("max.in.flight.requests.per.connection", 1);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(properties);
        //producer.initTransactions();
    }
    
    @Override
    public void send(String key, String msg) {
        try {
            //producer.beginTransaction();
            ProducerRecord record = new ProducerRecord(topic, key, msg);
            producer.send(record);
            //producer.commitTransaction();
            
        } catch (Throwable e) {
            e.printStackTrace();
            //producer.abortTransaction();
        }
        //System.out.println("************" + json + "************");
    }
    
    @Override
    public void close() {
        producer.close();
    }
    
    public static void main(String[] args) {
        KafkaProducerImpl producer = new KafkaProducerImpl();
        producer.send("1","test1");
        // 测试时
        // close时才会发送数据
        producer.close();
    }
}
