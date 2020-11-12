package com.example.demo;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

class DeleteAllProcessor implements Processor<String, String> {
  private KeyValueStore<String, Long> store;

  @SuppressWarnings("unchecked")
  @Override
  public void init(ProcessorContext context) {
    store = (KeyValueStore<String, Long>) context.getStateStore(WordcountApplicationConfig.COUNT_STORE_NAME);
  }

  @Override
  public void process(String key, String value) {
    store.all().forEachRemaining(keyValue -> store.delete(keyValue.key));
  }

  @Override
  public void close() {

  }
}