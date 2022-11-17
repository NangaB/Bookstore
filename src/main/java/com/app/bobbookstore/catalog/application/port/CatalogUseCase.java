package com.app.bobbookstore.catalog.application.port;

import com.app.bobbookstore.catalog.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface CatalogUseCase {

    Optional<Book> findOneByTitle(String title);

    List<Book> findByTitle(String title);

    List<Book> findByAuthor(String author);

    List<Book> findAll();

    Optional<Book> findById(Long id);

    Optional<Book> findOneByTitleAndAuthor(String title, String author);

    List<Book> findByTitleAndAuthor(String title, String author);

    Book addBook(CreateBookCommand createBookCommand);

    void removeById(Long id);

    void updateBookCover(UpdateBookCoverCommand command);

    UpdateBookResponse updateBook(UpdateBookCommand command);

    void removeBookCover(Long id);

    @Value
    class CreateBookCommand{
        String title;
        String author;
        Integer year;
        BigDecimal price;
    }

    @Value
    @Builder
    @AllArgsConstructor
    class UpdateBookCommand{
        Long id;
        String title;
        String author;
        Integer year;
        BigDecimal price;

        //to updating book - we don't want to trash the CagalogService
        public Book updateFields(Book book){
            if(title != null){
                book.setTitle(title);
            }
            if(author != null){
                book.setAuthor(author);
            }
            if(year != null){
                book.setYear(year);
            }
            if(price != null){
                book.setPrice(price);
            }
            return book;
        }
    }

    @Value
    class UpdateBookResponse {
        public static UpdateBookResponse SUCCESS = new UpdateBookResponse(true, Collections.emptyList());

        boolean success;
        List<String> errors;
    }

    @Value
    class UpdateBookCoverCommand{
        Long id;
        byte[] file;
        String contentType;
        String filename;

    }
}
