package com.example.demo;

import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WordCountQuery implements ICountWords {

  @Value("${app.store-name}")
  private String storeName;
  @Autowired
  private InteractiveQueryService interactiveQueryService;

  @Override
  public Map<String, Long> countWords() {
    ReadOnlyKeyValueStore<String, Long> store = interactiveQueryService.getQueryableStore(storeName,QueryableStoreTypes.keyValueStore());
    Map<String, Long> count = new HashMap<>();
    store.all().forEachRemaining(keyValue -> count.put(keyValue.key, keyValue.value));
    return count;
  }
}
