package com.example.demo;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorSupplier;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Properties;

@Configuration
public class WordcountApplicationConfig {

  public static final String INPUT_TOPIC_NAME = "wordcount-input";
  public static final String DELETE_ALL_TOPIC_NAME = "wordcount-delete-all";
  public static final String DELETE_WORD_TOPIC_NAME = "wordcount-delete-word";
  public static final String COUNT_STORE_NAME = "wordcount-store";
  private final String DELETE_ALL_PROCESSOR = "deleteProcessor";
  private final String DELETE_WORD_PROCESSOR = "deleteAllProcessor";

  @Bean
  public KafkaStreams wordcountTopology(@Qualifier("wordcountProperties") Properties wordcountProperties) {
    StreamsBuilder builder = new StreamsBuilder();
    KTable<String, Long> count = builder.<String, String>stream(INPUT_TOPIC_NAME)
        .flatMapValues(line -> Arrays.asList(line.split("\\W+")))
        .groupBy((key, word) -> word)
        .count(Materialized.as(COUNT_STORE_NAME));

    builder.<String,String>stream(DELETE_ALL_TOPIC_NAME).process(DeleteAllProcessor::new, Named.as(DELETE_ALL_PROCESSOR), COUNT_STORE_NAME);
    builder.<String,String>stream(DELETE_WORD_TOPIC_NAME).process(DeleteWordProcessor::new, Named.as(DELETE_WORD_PROCESSOR), COUNT_STORE_NAME);

    Topology topology = builder.build();
    return new KafkaStreams(topology, wordcountProperties);
  }

  @Bean
  public Properties wordcountProperties() {
    Properties properties = new Properties();
    properties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount");
    properties.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094");
    properties.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    properties.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    return properties;
  }

}
