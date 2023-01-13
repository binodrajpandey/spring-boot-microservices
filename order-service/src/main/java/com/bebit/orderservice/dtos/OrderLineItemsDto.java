package com.bebit.orderservice.dtos;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderLineItemsDto {

  private Long id;
  private String skuCode;
  private BigDecimal price;
  private Integer quantity;

}
