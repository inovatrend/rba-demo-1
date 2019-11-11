package rba_demo_1;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.serialization.StringSerializer;

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

        config.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my-producer-instance-1");
        config.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, 2000);

        KafkaProducer<String, String> producer = new KafkaProducer<>(config);

        String topic1 = "rba-demo-1";
        String topic2 = "rba-demo-2";

        producer.initTransactions();

        producer.beginTransaction();

        try {
            String key = "key-" + RandomStringUtils.randomNumeric(3, 4);
            String message = "Transactional message , random text: " + RandomStringUtils.randomAlphabetic(20, 21).toUpperCase();

            System.out.println("Producing transactional message to two topics: " + message);

            ProducerRecord<String, String> record = new ProducerRecord<>(topic1, key, message);
            producer.send(record);

            System.out.println("Intentional exception: " + (50 / 0));

            ProducerRecord<String, String> record2 = new ProducerRecord<>(topic2, key, message);
            producer.send(record2);

            producer.commitTransaction();
        } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
            // We cant recover from these exceptions
            e.printStackTrace();
            producer.close();
        } catch (Exception e) {
            // For all other exceptions, just abort the transaction and try again.
            producer.abortTransaction();
            e.printStackTrace();
        }

        producer.flush();
        producer.close();
        System.out.println("Done");
    }
}
