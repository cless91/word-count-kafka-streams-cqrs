package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class WordCountCommand implements IWriteText, IDeleteWords {
  @Autowired
  KafkaTemplate<String, String> producer;
  @Value("${app.input-topic}")
  private String inputTopic;
  @Value("${app.delete-word-topic}")
  private String deleteWordTopic;
  @Value("${app.delete-all-topic}")
  private String deleteAllTopic;

  @Override
  public void writeText(String text) {
    producer.send(inputTopic, text);
  }

  @Override
  public void deleteWord(String word) {
    producer.send(deleteWordTopic, word);
  }

  @Override
  public void deleteAll() {
    String deletionCommand = "delete-" + UUID.randomUUID().toString();
    producer.send(deleteAllTopic, deletionCommand);
  }
}
