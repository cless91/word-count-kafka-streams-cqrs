package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RestControllerQuery {

  private final ICountWords iCountWords;

  @GetMapping("/count")
  public Map<String, Long> countWords() {
    return iCountWords.countWords();
  }
}
