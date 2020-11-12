package com.example.demo;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class WordCountCore implements IWriteText, ICountWords, IDelete {
  @Autowired
  KafkaTemplate<String, String> producer;
  @Autowired
  private KafkaStreams kafkaStreams;

  @Override
  public void writeText(String text) {
    producer.send(WordcountApplicationConfig.INPUT_TOPIC_NAME, text);
  }

  @Override
  public Map<String, Long> countWords() {
    ReadOnlyKeyValueStore<String, Long> store = kafkaStreams.store(
        StoreQueryParameters.fromNameAndType(WordcountApplicationConfig.COUNT_STORE_NAME,
            QueryableStoreTypes.keyValueStore()));
    Map<String, Long> count = new HashMap<>();
    store.all().forEachRemaining(keyValue -> count.put(keyValue.key, keyValue.value));
    return count;
  }

  @Override
  public void deleteAll() {
    String deletionCommand = "delete-"+ UUID.randomUUID().toString();
    producer.send(WordcountApplicationConfig.DELETE_ALL_TOPIC_NAME, deletionCommand);
  }

  @Override
  public void deleteWord(String word) {
    producer.send(WordcountApplicationConfig.DELETE_WORD_TOPIC_NAME, word);
  }
}
