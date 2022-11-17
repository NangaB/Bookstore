package com.app.bobbookstore.catalog.infrastructure;

import com.app.bobbookstore.catalog.domain.Book;
import com.app.bobbookstore.catalog.domain.CatalogRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;

@Repository
public class MemoryCatalogRepository implements CatalogRepository {

    private final Map<Long, Book> storage = new ConcurrentHashMap<>();

    public MemoryCatalogRepository() {
        storage.put(1L, new Book(1L, "Pan Tadeusz", "Adam Mickiewicz", 1890, new BigDecimal(100),null));
        storage.put(2L, new Book(2L, "Dziady", "Adam Mickiewicz", 1899, new BigDecimal(200), null));
        storage.put(3L, new Book(3L, "Ogniem i mieczem", "Henryk Sienkiewicz", 1899, new BigDecimal(150), null));
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void saveBook(Book book) {

    }

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void removeById(Long id) {
        storage.remove(id);
    }

}
