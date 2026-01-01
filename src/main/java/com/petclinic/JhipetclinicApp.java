package com.petclinic;

import com.petclinic.shared.generation.domain.ExcludeFromGeneratedCodeCoverage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.Environment;

@SpringBootApplication
@ExcludeFromGeneratedCodeCoverage(reason = "Not testing logs")
public class JhipetclinicApp {

  private static final Logger log = LoggerFactory.getLogger(JhipetclinicApp.class);

  public static void main(String[] args) {
    //Environment env = SpringApplication.run(JhipetclinicApp.class, args).getEnvironment();
    Environment env = new SpringApplicationBuilder(JhipetclinicApp.class)
      .headless(false) // Important pour Swing
      .run(args)
      .getEnvironment();
    if (log.isInfoEnabled()) {
      log.info(ApplicationStartupTraces.of(env));
    }
  }
}
