# Download confluent platform zip from www.confluent.io
# Unzip confluent and add it's bin folder to system path

# Start kafka
confluent start kafka


# Create topic
kafka-topics --bootstrap-server localhost:9092 --create --topic rba-demo-1 --partitions 3 --replication-factor 1

# List all topics
kafka-topics --bootstrap-server localhost:9092 --list

# Describe topic
kafka-topics --bootstrap-server localhost:9092 --describe --topic rba-demo-1



# Produce some messages
kafka-console-producer --broker-list localhost:9092 --topic rba-demo-1

# Produce some messages with keys
kafka-console-producer --broker-list localhost:9092 --topic rba-demo-1 --property "parse.key=true" --property "key.separator=:"



# Consume all messages from topic
kafka-console-consumer --bootstrap-server localhost:9092 --topic rba-demo-1 --from-beginning

# Consume all messages from topic showing the keys
kafka-console-consumer --bootstrap-server localhost:9092 --topic rba-demo-1 --from-beginning --property print.key=true

# Consume all messages from topic using specific consumer group
kafka-console-consumer --bootstrap-server localhost:9092 --topic rba-demo-1 --from-beginning --group consumer-group-1


# Increase number of partitions
kafka-topics --bootstrap-server localhost:9092 --alter --topic rba-demo-1 --partitions 4





# kafka-configs can used to change broker or topic configs. Some properties can be changed withour broker restart. See DYNAMIC UPDATE MODE column in configs documentation

# Change retention of a topic
kafka-configs --zookeeper localhost:2181 --entity-type topics --entity-name rba-demo-1 --alter --add-config retention.ms=10000000





