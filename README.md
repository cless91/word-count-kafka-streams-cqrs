reset an application:
```
docker exec -it word-count-kafka-streams-cqrs_kafka_1 kafka-streams-application-reset --bootstrap-servers kafka:9092 --application-id wordcount
```

list topics:
```
docker exec -it word-count-kafka-streams-cqrs_kafka_1 kafka-topics --bootstrap-server kafka:9092 --list
```