package com.roberttisma.sandbox.protobuf_mapstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class ProgramServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProgramServiceApplication.class, args);
  }

}
