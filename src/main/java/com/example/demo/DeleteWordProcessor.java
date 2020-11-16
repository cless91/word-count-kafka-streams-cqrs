package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class DeleteWordProcessor implements Processor<Object, String> {
  private KeyValueStore<String, Long> store;
  @Value("${app.store-name}")
  private String storeName;

  @SuppressWarnings("unchecked")
  @Override
  public void init(ProcessorContext context) {
    store = (KeyValueStore<String, Long>) context.getStateStore(storeName);
  }

  @Override
  public void process(Object key, String value) {
    store.delete(value);
  }

  @Override
  public void close() {
    log.info("closing");
  }
}
