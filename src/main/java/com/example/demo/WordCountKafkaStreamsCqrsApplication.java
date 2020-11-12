package com.example.demo;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.streams.KafkaStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.Properties;

import static com.example.demo.WordcountApplicationConfig.*;

@SpringBootApplication
public class WordCountKafkaStreamsCqrsApplication implements ApplicationRunner {

  @Autowired
  private KafkaStreams kafkaStreams;
  @Autowired
  @Qualifier("wordcountProperties")
  private Properties properties;

  public static void main(String[] args) {
    SpringApplication.run(WordCountKafkaStreamsCqrsApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    Properties config = new Properties();
    config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094");
    AdminClient admin = AdminClient.create(config);
    ListTopicsResult listTopicsResult = admin.listTopics();
    listTopicsResult.names().thenApply(topics -> {
      if (!topics.contains(INPUT_TOPIC_NAME)) {
        admin.createTopics(Collections.singleton(new NewTopic(INPUT_TOPIC_NAME, 1, (short) 1)));
      }
      if (!topics.contains(DELETE_ALL_TOPIC_NAME)) {
        admin.createTopics(Collections.singleton(new NewTopic(DELETE_ALL_TOPIC_NAME, 1, (short) 1)));
      }
      if (!topics.contains(DELETE_WORD_TOPIC_NAME)) {
        admin.createTopics(Collections.singleton(new NewTopic(DELETE_WORD_TOPIC_NAME, 1, (short) 1)));
      }
      return null;
    }).thenApply(o -> {
      kafkaStreams.start();
      return null;
    });

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.out.println("coucou2");
      kafkaStreams.close();
    }));

  }

  @PreDestroy
  public void shutdownHook() {
    if (kafkaStreams != null) {
      kafkaStreams.close();
    }
  }
}
