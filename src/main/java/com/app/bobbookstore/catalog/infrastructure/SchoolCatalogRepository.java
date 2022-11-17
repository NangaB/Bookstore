package com.app.bobbookstore.catalog.infrastructure;

import com.app.bobbookstore.catalog.domain.Book;
import com.app.bobbookstore.catalog.domain.CatalogRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;

@Repository
public class SchoolCatalogRepository implements CatalogRepository {

    private final Map<Long, Book> storage = new ConcurrentHashMap<>();
    private final AtomicLong ID_NEXT_VALUE = new AtomicLong(0L);

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void saveBook(Book book) {
        if(book.getId() != null){
            storage.put(book.getId(), book);
        }else {
            long nextId = nextId();
            book.setId(nextId);
            storage.put(nextId, book);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void removeById(Long id) {
        storage.remove(id);
    }

    private long nextId(){
        return ID_NEXT_VALUE.getAndIncrement();
    }

}
