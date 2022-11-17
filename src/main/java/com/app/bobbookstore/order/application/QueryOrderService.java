package com.app.bobbookstore.order.application;

import com.app.bobbookstore.order.application.port.QueryOrderUseCase;
import com.app.bobbookstore.order.domain.Order;
import com.app.bobbookstore.order.domain.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryOrderService implements QueryOrderUseCase {

    private final OrderRepository repository;

    public QueryOrderService(OrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Order> findAll() {
        return repository.findAll();
    }
}
