package com.bebit.inventoryservice.services;

import com.bebit.inventoryservice.dtos.InventoryResponse;
import com.bebit.inventoryservice.models.Inventory;
import com.bebit.inventoryservice.repositories.InventoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

  private final InventoryRepository inventoryRepository;

  @Transactional(readOnly = true)
  public List<InventoryResponse> isInStock(List<String> skuCodes) {
    return inventoryRepository.findBySkuCodeIn(skuCodes).stream().map(this::mapToInventoryDto)
        .collect(Collectors.toList());
  }

  private InventoryResponse mapToInventoryDto(Inventory inventory) {
    return InventoryResponse.builder()
        .skuCode(inventory.getSkuCode())
        .isInStock(inventory.getQuantity() > 0)
        .build();
  }

}
