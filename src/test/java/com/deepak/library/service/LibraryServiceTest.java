package com.deepak.library.service;

import com.deepak.library.repository.AuthorRepository;
import com.deepak.library.repository.BookRepository;
import com.deepak.library.domain.Author;
import com.deepak.library.domain.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibraryServiceTest {

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private LibraryService libraryService;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        authorRepository = mock(AuthorRepository.class);
        // Ensure your LibraryService constructor matches these arguments
        libraryService = new LibraryService(bookRepository, authorRepository);
    }



    @Test
    void testGetAllBooks() {
        // Arrange
        Book book1 = new Book("Book 1");
        Book book2 = new Book("Book 2");

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        // Act
        List<Book> books = libraryService.getAllBooks();

        // Assert
        assertEquals(2, books.size());
        assertEquals("Book 1", books.get(0).getTitle());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testAddBook_NewAuthor() {
        // Arrange
        String bookTitle = "New Book";
        String authorName = "New Author";

        Author author = new Author(authorName);
        Book book = new Book(bookTitle);
        book.setAuthor(author);

        when(authorRepository.findByName(authorName)).thenReturn(Optional.empty());
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // Act
        Book addedBook = libraryService.addBook(bookTitle, authorName);

        // Assert
        assertNotNull(addedBook);
        assertEquals(bookTitle, addedBook.getTitle());
        assertEquals(authorName, addedBook.getAuthor().getName());
        verify(authorRepository, times(1)).findByName(authorName);
        verify(authorRepository, times(1)).save(any(Author.class));
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testAddBook_ExistingAuthor() {
        // Arrange
        String bookTitle = "Existing Book";
        String authorName = "Existing Author";

        Author existingAuthor = new Author(authorName);
        Book book = new Book(bookTitle);
        book.setAuthor(existingAuthor);

        when(authorRepository.findByName(authorName)).thenReturn(Optional.of(existingAuthor));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // Act
        Book addedBook = libraryService.addBook(bookTitle, authorName);

        // Assert
        assertNotNull(addedBook);
        assertEquals(authorName, addedBook.getAuthor().getName());
        verify(authorRepository, times(1)).findByName(authorName);
        verify(authorRepository, never()).save(any(Author.class));
        verify(bookRepository, times(1)).save(any(Book.class));
    }
}