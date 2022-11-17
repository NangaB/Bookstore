package com.app.bobbookstore.order.application;

import com.app.bobbookstore.order.application.port.CreateOrderUseCase;
import com.app.bobbookstore.order.domain.Order;
import com.app.bobbookstore.order.domain.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateOrderService implements CreateOrderUseCase {

    private final OrderRepository repository;

    public CreateOrderService(OrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public CreateOrderResponse createOrder(CreateOrderCommand command) {
        Order order = Order.builder()
                .items(command.getItems())
                .recipient(command.getRecipient())
                .build();

        Order savedOrder = repository.save(order);
        return CreateOrderResponse.success(savedOrder.getId());
    }

}
