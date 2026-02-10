package com.deepak.library.service;

import com.deepak.library.domain.Author;
import com.deepak.library.domain.Book;
import com.deepak.library.repository.AuthorRepository;
import com.deepak.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LibraryService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public LibraryService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional
    public Book addBook(String title, String authorName) {
        // Find author or create a new one if not exists
        Author author = authorRepository.findByName(authorName)
                .orElseGet(() -> authorRepository.save(new Author(authorName)));

        Book newBook = new Book(title);
        newBook.setAuthor(author);

        return bookRepository.save(newBook);
    }
}
