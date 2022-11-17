package com.app.bobbookstore.order.domain;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@ToString
@Builder
public class Order {

    Long id;
    @Builder.Default
    OrderStatus status = OrderStatus.NEW;
    List<OrderItem> items;
    Recipient recipient;
    LocalDateTime createdAt;

    public BigDecimal totalPrice(){
        return items.stream()
                .map(item -> item.getBook().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
