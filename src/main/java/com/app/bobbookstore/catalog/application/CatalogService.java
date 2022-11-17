package com.app.bobbookstore.catalog.application;

import com.app.bobbookstore.catalog.application.port.CatalogUseCase;
import com.app.bobbookstore.catalog.domain.Book;
import com.app.bobbookstore.catalog.domain.CatalogRepository;
import com.app.bobbookstore.uploads.application.port.UploadUseCase;
import com.app.bobbookstore.uploads.domain.Upload;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CatalogService implements CatalogUseCase {

    private final CatalogRepository repository;
    private final UploadUseCase upload;

    public CatalogService(@Qualifier("schoolCatalogRepository") CatalogRepository repository, UploadUseCase upload) {
        this.repository = repository;
        this.upload = upload;
    }

    @Override
    public List<Book> findAll(){
        return repository.findAll();
    }

    public Optional<Book> findById(Long id){
        return repository.findById(id);
    }

    @Override
    public List<Book> findByTitle(String title){
        return repository.findAll()
                .stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByAuthor(String author){
        return repository.findAll()
                .stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Optional<Book> findOneByTitle(String title){
        return repository.findAll()
                .stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .findFirst();
    }

    @Override
    public List<Book> findByTitleAndAuthor(String title, String author){
        return repository.findAll()
                .stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findOneByTitleAndAuthor(String title, String author){
        return repository.findAll()
                .stream()
                .filter(book -> book.getTitle().toLowerCase().startsWith(title.toLowerCase()))
                .filter(book -> book.getAuthor().toLowerCase().startsWith(author.toLowerCase()))
                .findFirst();
    }

    @Override
    public Book addBook(CreateBookCommand createBookCommand) {
        Book book = new Book(createBookCommand.getTitle(), createBookCommand.getAuthor(), createBookCommand.getYear(), createBookCommand.getPrice());
        repository.saveBook(book);
        return book;
    }

    @Override
    public void removeById(Long id){
        repository.removeById(id);
    }


    @Override
    public UpdateBookResponse updateBook(UpdateBookCommand command) {
        return repository.findById(command.getId())
                .map(book -> {
                    Book updatedBook = command.updateFields(book);
//                    book.setTitle(command.getTitle());
//                    book.setAuthor(command.getAuthor());
//                    book.setYear(command.getYear());
                    repository.saveBook(updatedBook);
                    return UpdateBookResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateBookResponse(false, Arrays.asList("Book not found with id: " + command.getId())));
    }

    @Override
    public void updateBookCover(UpdateBookCoverCommand command) {
        int length = command.getFile().length;
        System.out.println("Received cover command " + command.getFilename() + " bytes " + length);
        repository.findById(command.getId())
                .ifPresent(book -> {
                    Upload savedUpload = upload.save(new UploadUseCase.SaveUploadCommand(command.getFilename(), command.getFile(), command.getContentType()));
                    book.setCoverId(savedUpload.getId());
                    repository.saveBook(book);
                    ;});
    }

    @Override
    public void removeBookCover(Long id) {
        repository.findById(id).ifPresent(book -> {
            if(book.getCoverId() != null) {
                upload.removeById(book.getCoverId());
                book.setCoverId(null);
                repository.saveBook(book);
            }
        });

    }

}
