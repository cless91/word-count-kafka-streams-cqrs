package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RestControllerCommand {

  private final IWriteText iWriteText;
  private final IDeleteWords iDeleteWords;

  @PostMapping("/write")
  public void writeText(@RequestBody String text) {
    iWriteText.writeText(text);
  }

  @DeleteMapping("/word/{word}")
  public void deleteWord(@PathVariable("word") String word) {
    iDeleteWords.deleteWord(word);
  }

  @DeleteMapping("/all")
  public void deleteAll() {
    iDeleteWords.deleteAll();
  }

}
