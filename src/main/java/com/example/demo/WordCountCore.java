package com.example.demo;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class WordCountCore implements IWriteText, ICountWords, IDelete {
  @Autowired
  KafkaTemplate<String, String> producer;
  @Value("${app.input-topic}")
  private String inputTopic;
  @Value("${app.store-name}")
  private String storeName;
  @Value("${app.delete-all-topic}")
  private String deleteAllTopic;
  @Value("${app.delete-word-topic}")
  private String deleteWordTopic;
  @Autowired
  private InteractiveQueryService interactiveQueryService;

  @Override
  public void writeText(String text) {
    producer.send(inputTopic, text);
  }

  @Override
  public Map<String, Long> countWords() {
    ReadOnlyKeyValueStore<String, Long> store = interactiveQueryService.getQueryableStore(storeName,QueryableStoreTypes.keyValueStore());
//    ReadOnlyKeyValueStore<String, Long> store = kafkaStreams.store(
//        StoreQueryParameters.fromNameAndType(storeName,QueryableStoreTypes.keyValueStore()));
    Map<String, Long> count = new HashMap<>();
    store.all().forEachRemaining(keyValue -> count.put(keyValue.key, keyValue.value));
    return count;
  }

  @Override
  public void deleteAll() {
    String deletionCommand = "delete-"+ UUID.randomUUID().toString();
    producer.send(deleteAllTopic, deletionCommand);
  }

  @Override
  public void deleteWord(String word) {
    producer.send(deleteWordTopic, word);
  }
}
