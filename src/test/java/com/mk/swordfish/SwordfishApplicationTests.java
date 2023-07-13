package com.mk.swordfish;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
class SwordfishApplicationTests {

  @Test
  void contextLoads() {
    // This method is empty because is just evaluating spring context loading
    Assertions.assertTrue(palin("Anita lava la tina"));
    Assertions.assertTrue(palin("- Anita lava la tina"));
    Assertions.assertTrue(palin("- Anita lava la tina."));
    Assertions.assertTrue(palin("- Anita lava la tina.  - "));
    Assertions.assertTrue(palin("- Anit1a lava la tina.  - "));


  }

  boolean palin(String in){
    List<Character> list =  in.toUpperCase().chars()
        .mapToObj(value -> (char)value)
        .filter(Character::isLetter)
        .collect(Collectors.toList());

    int size = list.size()-1;
    for(int i=0;i<list.size();i++){
      if(!list.get(i).equals(list.get(size-i)))
      {
        return false;
      }
    }
    return true;
  }

}
