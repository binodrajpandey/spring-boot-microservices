package com.bebit.discoveryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * https://www.youtube.com/watch?v=0TQliqoX6Kc&t=7s&ab_channel=ProgrammingTechie
 */
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(DiscoveryServerApplication.class, args);

  }

}
