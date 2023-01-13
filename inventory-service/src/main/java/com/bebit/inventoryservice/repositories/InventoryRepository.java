package com.bebit.inventoryservice.repositories;

import com.bebit.inventoryservice.models.Inventory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

  List<Inventory> findBySkuCodeIn(List<String> skuCodes);
}
