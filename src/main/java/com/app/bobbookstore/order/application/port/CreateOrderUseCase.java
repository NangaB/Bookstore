package com.app.bobbookstore.order.application.port;

import com.app.bobbookstore.order.domain.OrderItem;
import com.app.bobbookstore.order.domain.Recipient;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface CreateOrderUseCase {

    CreateOrderResponse createOrder(CreateOrderCommand command);

    @Value
    @Builder
    class CreateOrderCommand {
        @Singular
        List<OrderItem> items;
        Recipient recipient;

    }

    @Value
    class CreateOrderResponse {
        boolean success;
        Long orderId;
        List<String> errors;

        public static CreateOrderResponse success(Long orderId) {
            return new CreateOrderResponse(true, orderId, Collections.emptyList());
        }

        public static CreateOrderResponse failure(String... errors) {
            return new CreateOrderResponse(false, null, Arrays.asList(errors));
        }
    }
}
