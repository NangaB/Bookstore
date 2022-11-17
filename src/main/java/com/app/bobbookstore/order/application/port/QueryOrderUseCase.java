package com.app.bobbookstore.order.application.port;

import com.app.bobbookstore.order.domain.Order;

import java.util.List;

public interface QueryOrderUseCase {

    List<Order> findAll();
}
