package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class WordCountRestController {

  private final IWriteText iWriteText;
  private final ICountWords iCountWords;
  private final IDelete iDelete;

  @PostMapping("/write")
  public void writeText(@RequestBody String text){
    iWriteText.writeText(text);
  }

  @GetMapping("/count")
  public Map<String, Long> countWords(){
    return iCountWords.countWords();
  }

  @DeleteMapping("/all")
  public void deleteAll(){
    iDelete.deleteAll();
  }

  @DeleteMapping("/word/{word}")
  public void deleteWord(@PathVariable("word") String word){
    iDelete.deleteWord(word);
  }
}
