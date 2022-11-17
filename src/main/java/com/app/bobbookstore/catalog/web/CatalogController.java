package com.app.bobbookstore.catalog.web;

import com.app.bobbookstore.catalog.application.port.CatalogUseCase;
import com.app.bobbookstore.catalog.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/catalog")
@AllArgsConstructor
public class CatalogController {

    private final CatalogUseCase catalog;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getAll(@RequestParam Optional<String> title,
                             @RequestParam Optional<String> author){
        if(title.isPresent() && author.isPresent()){
            return catalog.findByTitleAndAuthor(title.get(), author.get());
        }else if(title.isPresent()){
            return catalog.findByTitle(title.get());
        }else if(author.isPresent()){
            return catalog.findByAuthor(author.get());
        }else{
            return catalog.findAll();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable Long id){
        return catalog.findById(id)
                .map(body -> ResponseEntity.ok(body))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Book addBook(@Valid @RequestBody CatalogController.RestBookCommand command){
        Book book = catalog.addBook(command.toCommand());
        return book;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id){
        catalog.removeById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateBook(@PathVariable Long id, @Valid @RequestBody RestBookCommand command){
        catalog.updateBook(command.toUpdateCommand(id));
    }

    @PutMapping(value = "/{id}/cover", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addBookCover(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("Got file: " + file.getOriginalFilename());
        catalog.updateBookCover(new CatalogUseCase.UpdateBookCoverCommand(id, file.getBytes(), file.getContentType(), file.getOriginalFilename()));
    }

    @DeleteMapping("{id}/cover")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookCover(@PathVariable Long id){
        catalog.removeBookCover(id);
    }

    @Data
    private static class RestBookCommand {
        @NotBlank(message = "provide a title")
        private String title;

        @NotBlank(message = "provide an author")
        private String author;

        @NotNull(message = "provide year")
        private Integer year;

        @DecimalMin(value = "0.00", message = "provide value greater than 0.00")
        private BigDecimal price;

        CatalogUseCase.CreateBookCommand toCommand(){
            return new CatalogUseCase.CreateBookCommand(title,author,year,price);
        }

        CatalogUseCase.UpdateBookCommand toUpdateCommand(Long id){
            return new CatalogUseCase.UpdateBookCommand(id, title, author, year, price);
        }
    }
}
