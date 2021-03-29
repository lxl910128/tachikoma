package club.projectgaia.tachikoma.kafka;

/**
 * @author Phoenix Luo
 * @version 2021/2/18
 **/
public class TestKafka {
    public static void main(String[] args) {
        testConsumer();
    }
    
    private static void testConsumer() {
        KafkaConsumerImpl consumer = new KafkaConsumerImpl();
        consumer.consumeOrder();
        
    }
}
