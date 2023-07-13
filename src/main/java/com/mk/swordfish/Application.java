package com.mk.swordfish;

import com.mk.swordfish.core.annotations.Behavior;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
    includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Behavior.class))
@SuppressWarnings("PMD.UseUtilityClass")
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
