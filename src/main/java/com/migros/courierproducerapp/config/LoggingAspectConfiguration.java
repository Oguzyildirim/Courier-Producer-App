package com.migros.courierproducerapp.config;

import com.migros.courierproducerapp.aop.logging.LoggingAspect;

import com.migros.courierproducerapp.util.AllProfiles;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

  @Bean
  @AllProfiles({ProfileConstants.SPRING_PROFILE_DEVELOPMENT, ProfileConstants.SPRING_PROFILE_PRODUCTION})
  public LoggingAspect loggingAspect(Environment env) {
    return new LoggingAspect(env);
  }
}
