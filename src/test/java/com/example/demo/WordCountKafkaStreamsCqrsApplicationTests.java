package com.example.demo;

import org.apache.kafka.streams.KafkaStreams;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WordCountKafkaStreamsCqrsApplicationTests {

  @Autowired
  KafkaStreams kafkaStreams;
  @Autowired
  private IWriteText iWriteText;
  @Autowired
  private ICountWords iCountWords;

  @BeforeEach
  void setUp() {
    kafkaStreams.cleanUp();
  }

  @AfterEach
  void tearDown() {
    kafkaStreams.close();
  }

  @Test
  void contextLoads() throws InterruptedException {
    iWriteText.writeText("hello hello world");
    Thread.sleep(1000);
    Map<String, Long> count = iCountWords.countWords();
    Map<String, Long> expectedCount = Map.of("hello", 2L, "world", 1L);
    assertThat(count).isSameAs(expectedCount);
  }

}
