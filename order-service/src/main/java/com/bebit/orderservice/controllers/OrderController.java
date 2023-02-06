package com.bebit.orderservice.controllers;

import com.bebit.orderservice.dtos.OrderRequest;
import com.bebit.orderservice.services.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

  final OrderService orderService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
//  @TimeLimiter(name = "inventory") //TODO: timelimiter not working with docker, need to check why?
  @Retry(name = "inventory")
  public String placeOrder(@RequestBody OrderRequest orderRequest) {

    return orderService.placeOrder(orderRequest);
//    return CompletableFuture.supplyAsync(() -> orderService.placeOrder(orderRequest));
  }

  /**
   * Return type should be same as that of the api method. This can be implemented in the service as well.
   * OrderService might throw RuntimeException so we use it here.
   */
  public String fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
    return "Oops! Something went wrong. Please place the order after some time.";

  }

}
