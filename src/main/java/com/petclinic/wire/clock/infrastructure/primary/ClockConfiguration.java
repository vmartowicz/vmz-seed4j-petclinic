package com.petclinic.wire.clock.infrastructure.primary;

import java.time.Clock;
import java.time.ZoneId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ClockConfiguration {

  private static final ZoneId CLINIC_ZONE = ZoneId.of("Europe/Paris");

  @Bean
  Clock clock() {
    return Clock.system(CLINIC_ZONE);
  }
}
