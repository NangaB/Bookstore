package com.app.bobbookstore;

import com.app.bobbookstore.catalog.application.port.CatalogUseCase;
import com.app.bobbookstore.catalog.domain.Book;
import com.app.bobbookstore.order.application.port.CreateOrderUseCase;
import com.app.bobbookstore.order.application.port.QueryOrderUseCase;
import com.app.bobbookstore.order.domain.OrderItem;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ApplicationStartup implements CommandLineRunner {
    private final CatalogUseCase catalog;
    private final CreateOrderUseCase order;
    private final QueryOrderUseCase query;
    private final String title;

    public ApplicationStartup(CatalogUseCase catalog, CreateOrderUseCase order, QueryOrderUseCase query,
                              @Value("${bookstore.catalog.title}") String title) {
        this.catalog = catalog;
        this.order = order;
        this.query = query;
        this.title = title;
    }

    @Override
    public void run(String... args) throws Exception {
        initData();
        findByTitle();
        findAndUpdate();
        findByTitle();
        createOrder();
    }

    private void createOrder() {
        //find books - we assume that they are in repository
        Book harry = catalog.findOneByTitle("Harry Potter").orElseThrow(() -> new IllegalStateException("Cannot find a book"));
        Book pan = catalog.findOneByTitle("Pan Wolodowski").orElseThrow(() -> new IllegalStateException("Cannot find a book"));
        //create recipient

        //create order
        CreateOrderUseCase.CreateOrderCommand command = CreateOrderUseCase.CreateOrderCommand.builder()
                .recipient(null)
                .item(new OrderItem(harry, 1))
                .item(new OrderItem(pan, 1))
                .build();

        //save in repository
        CreateOrderUseCase.CreateOrderResponse response = order.createOrder(command);
        System.out.println("Created ORDER with id: " + response.getOrderId());

        //list all orders

//        query.findAll()
//                .forEach(ordert -> {
//                        System.out.println("Got order with total price: " + ordert.totalPrice() + " details: " + ordert);
//                });

    }


    private void initData(){
        catalog.addBook(new CatalogUseCase.CreateBookCommand("Harry Potter", "JK", 1900, new BigDecimal(100)));
        catalog.addBook(new CatalogUseCase.CreateBookCommand("Pan Samochodzik", "Nienacki", 1987, new BigDecimal(80)));
        catalog.addBook(new CatalogUseCase.CreateBookCommand("Pan Wolodowski", "Sienkiewicz", 1987, new BigDecimal(68)));
    }

    private void findByTitle() {
        List<Book> books = catalog.findByTitle(title);
        books.forEach(System.out::println);
    }

    private void findAndUpdate() {
        System.out.println("Updating book....");
        catalog.findOneByTitleAndAuthor("Pan Samochodzik", "Nienacki")
                .ifPresent(book -> {
                    CatalogUseCase.UpdateBookCommand command = CatalogUseCase.UpdateBookCommand.builder()
                            .id(book.getId())
                            .title("Pan Samochodziki i coś tam dalej")
                            .build();
                    catalog.updateBook(command);
                    CatalogUseCase.UpdateBookResponse response = catalog.updateBook(command);
                    System.out.println("Updating book result: " + response.isSuccess());
                });
    }

    private void findAndUpdatee() {
        catalog.findOneByTitleAndAuthor("Harry", "JK")
                .ifPresent(book -> {
                    CatalogUseCase.UpdateBookCommand commmand = new CatalogUseCase.UpdateBookCommand(
                            book.getId(),
                            "Harry Potter i komnata tajemnic",
                            book.getAuthor(),
                            book.getYear(),
                            book.getPrice()
                    );
                    catalog.updateBook(commmand);
                });
    }
}
