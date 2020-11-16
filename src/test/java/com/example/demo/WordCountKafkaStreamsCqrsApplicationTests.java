package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = SpringBootEntryPoint.class
)
@Slf4j
class WordCountKafkaStreamsCqrsApplicationTests {

  private final RestTemplate restTemplate = new RestTemplate();
  @LocalServerPort
  private int port;

  @BeforeEach
  void setUp() throws Exception {
    URI uri = URI.create(String.format("http://localhost:%d/all", port));
    log.info("sending delete REST request to " + uri);
    restTemplate.delete(uri);
  }

  @Test
  void appWorks() throws Exception {
    URI writeUri = URI.create(String.format("http://localhost:%d/write", port));
    URI countUri = URI.create(String.format("http://localhost:%d/count", port));
    ResponseEntity<Void> writeEntity = restTemplate.postForEntity(writeUri, "hello hello world", Void.TYPE);
    assertThat(writeEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    Thread.sleep(500);
    ResponseEntity<Map> countEntity = restTemplate.getForEntity(countUri, Map.class);
    assertThat(countEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    Map<String, Integer> countResponse = countEntity.getBody();
    Map<String, Integer> expectedCountResponse = Map.of("hello", 2, "world", 1);
    assertThat(countResponse).containsExactlyInAnyOrderEntriesOf(expectedCountResponse);
  }

}
