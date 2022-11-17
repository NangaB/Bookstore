package com.app.bobbookstore.order.domain;

import com.app.bobbookstore.catalog.domain.Book;
import lombok.Value;

@Value
public class OrderItem {
    Book book;
    int quantity;
}
