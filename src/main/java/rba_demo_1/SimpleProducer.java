package rba_demo_1;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.LocalDateTime;
import java.util.Properties;

public class SimpleProducer {


    public static void main(String[] args) throws InterruptedException {

        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        KafkaProducer<String, String> producer = new KafkaProducer<>(config);

        String topic = "rba-demo-1";
        int msgCount = 10;
        for (int i = 0; i < msgCount; i++) {
            String key = "key-" + RandomStringUtils.randomNumeric(3, 4);
            String message = "Message from Java app created at: " + LocalDateTime.now();
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, message);
            producer.send(record);
            System.out.println("Produced message: " + message);
            Thread.sleep(500);
        }
        producer.flush();
        producer.close();
        System.out.println("Done");
    }
}
