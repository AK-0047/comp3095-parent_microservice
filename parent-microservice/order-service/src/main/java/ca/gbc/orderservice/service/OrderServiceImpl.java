package ca.gbc.orderservice.service;

import ca.gbc.orderservice.client.InventoryClient;
import ca.gbc.orderservice.dto.OrderRequest;
import ca.gbc.orderservice.event.OrderPlacedEvent;
import ca.gbc.orderservice.model.Order;
import ca.gbc.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional  //RollBack
public class OrderServiceImpl implements OrderService{


    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;


    @Override
    public void placeOrder(OrderRequest orderRequest) {

        var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if(isProductInStock) {

            Order order = Order.builder()
                    .orderNumber(UUID.randomUUID().toString())
                    .price(orderRequest.price())
                    .skuCode(orderRequest.skuCode())
                    .quantity(orderRequest.quantity())
                    .build();

            orderRepository.save(order);

            //Send message to Kafka on order-placed topic
            OrderPlacedEvent orderPlacedEvent =
                    new OrderPlacedEvent(order.getOrderNumber(), orderRequest.userDetails().email());
            log.info("Start - Sending OrderPlacedEvent {} to kafka topic order-placed", orderPlacedEvent);
            kafkaTemplate.send("order-placed", orderPlacedEvent);
            log.info("Complete - Sent OrderPlacedEvent {} to kafka topic order-placed", orderPlacedEvent);
        }else{
            throw new RuntimeException("Order Not in stock");
        }
    }
}
