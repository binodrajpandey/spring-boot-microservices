package com.bebit.orderservice.services;

import com.bebit.orderservice.dtos.InventoryResponse;
import com.bebit.orderservice.dtos.OrderLineItemsDto;
import com.bebit.orderservice.dtos.OrderRequest;
import com.bebit.orderservice.models.Order;
import com.bebit.orderservice.models.OrderLineItems;
import com.bebit.orderservice.repositories.OrderRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

  private final OrderRepository orderRepository;
  private final WebClient.Builder webClientBuilder;

  public String placeOrder(OrderRequest orderRequest) {
    log.info("Start to place the order...");

    final var order = new Order();
    order.setOrderNumber(UUID.randomUUID().toString());

    final List<OrderLineItems> orderLineItems =
        orderRequest.getOrderLineItems().stream().map(this::mapToOrderLineItems).collect(Collectors.toList());
    order.setOrderLineItems(orderLineItems);

    final List<String> skuCodes =
        order.getOrderLineItems().stream().map(OrderLineItems::getSkuCode).collect(Collectors.toList());
    InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
        .uri("http://inventory-service/api/inventory",
            uriBuilder -> uriBuilder.queryParam("skuCodes", skuCodes).build())
        .retrieve()
        .bodyToMono(InventoryResponse[].class)
        .block();

    final boolean isInStock = Stream.of(inventoryResponses).allMatch(InventoryResponse::isInStock);

    if (isInStock) {
      orderRepository.save(order);
      return "Order Placed Successfully";
    } else {
      throw new IllegalArgumentException("Stock is not available, please place the order later.");
    }


  }

  private OrderLineItems mapToOrderLineItems(OrderLineItemsDto orderLineItemsDto) {
    final var orderLineItems = new OrderLineItems();
    orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
    orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
    orderLineItems.setPrice(orderLineItemsDto.getPrice());
    return orderLineItems;
  }

}
