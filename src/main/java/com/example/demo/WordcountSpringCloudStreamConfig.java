package com.example.demo;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class WordcountSpringCloudStreamConfig {

  @Value("${app.input-topic}")
  private String inputTopic;
  @Value("${app.store-name}")
  private String storeName;
  @Value("${app.delete-all-topic}")
  private String deleteAllTopic;
  @Value("${app.delete-word-topic}")
  private String deleteWordTopic;

  private final String DELETE_ALL_PROCESSOR = "deleteProcessor";
  private final String DELETE_WORD_PROCESSOR = "deleteAllProcessor";

  @Autowired
  DeleteAllProcessor deleteAllProcessor;
  @Autowired
  DeleteWordProcessor deleteWordProcessor;

  @Bean
  public Function<KStream<Object, String>, Function<KStream<Object, String>, Consumer<KStream<Object, String>>>> wordcount() {
    return input ->
        deleteAll ->
            deleteWord -> {
              input
                  .flatMapValues(line -> Arrays.asList(line.split("\\W+")))
                  .groupBy((key, word) -> word)
                  .count(Materialized.as(storeName));
              deleteAll
                  .process(() -> deleteAllProcessor, Named.as(DELETE_ALL_PROCESSOR), storeName);
              deleteWord
                  .process(() -> deleteWordProcessor, Named.as(DELETE_WORD_PROCESSOR), storeName);
            };
  }
}