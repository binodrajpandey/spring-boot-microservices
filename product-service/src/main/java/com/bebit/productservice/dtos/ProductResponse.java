package com.bebit.productservice.dtos;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProductResponse {
  private String id;
  private String name;
  private String description;
  private BigDecimal price;

}
