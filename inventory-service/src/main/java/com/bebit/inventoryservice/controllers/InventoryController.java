package com.bebit.inventoryservice.controllers;

import com.bebit.inventoryservice.dtos.InventoryResponse;
import com.bebit.inventoryservice.services.InventoryService;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

  private final InventoryService inventoryService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<InventoryResponse> isInStock(@RequestParam List<String> skuCodes) throws InterruptedException {
//    TimeUnit.SECONDS.sleep(15); // Uncomment this to test timeout for circuit breaker.
    return inventoryService.isInStock(skuCodes);
  }

}
