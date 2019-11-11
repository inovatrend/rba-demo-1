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

        // Advanced configs
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        config.put(ProducerConfig.RETRIES_CONFIG, 100);
        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        config.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy"); // none | gzip | lz4 | snappy
        config.put(ProducerConfig.LINGER_MS_CONFIG, 20);
        config.put(ProducerConfig.BATCH_SIZE_CONFIG, 32768); //32 kb


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
