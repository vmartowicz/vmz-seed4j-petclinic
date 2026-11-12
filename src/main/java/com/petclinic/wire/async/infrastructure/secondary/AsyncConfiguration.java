package com.petclinic.wire.async.infrastructure.secondary;

import java.util.concurrent.Executor;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAsync
@EnableScheduling
class AsyncConfiguration {

  @Bean
  public Executor taskExecutor(ThreadPoolTaskExecutorBuilder builder) {
    var executor = builder.build();
    executor.initialize();
    return executor;
  }
}
