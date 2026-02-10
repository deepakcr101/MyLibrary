package com.deepak.library.controller;

import com.deepak.library.dto.AddBookRequest;
import com.deepak.library.domain.Book;
import com.deepak.library.service.LibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(libraryService.getAllBooks());
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody AddBookRequest request) {
        Book newBook = libraryService.addBook(request.getTitle(), request.getAuthorName());
        return new ResponseEntity<>(newBook, HttpStatus.CREATED);
    }
}
