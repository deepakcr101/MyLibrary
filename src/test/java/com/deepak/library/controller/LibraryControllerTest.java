package com.deepak.library.controller;

import com.deepak.library.dto.AddBookRequest;
import com.deepak.library.domain.Book;
import com.deepak.library.domain.Author;
import com.deepak.library.security.SecurityConfig;
import com.deepak.library.service.LibraryService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibraryController.class)
@Import(SecurityConfig.class)
class LibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LibraryService libraryService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        reset(libraryService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddBook() throws Exception {
        // Arrange
        String title = "New Book";
        String authorName = "New Author";

        // Use No-Args constructor and setters
        AddBookRequest request = new AddBookRequest();
        request.setTitle(title);
        request.setAuthorName(authorName);

        Book savedBook = new Book(title);
        savedBook.setAuthor(new Author(authorName));

        when(libraryService.addBook(title, authorName)).thenReturn(savedBook);

        // Act & Assert
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.author.name").value(authorName));
    }

    @Test
    @WithMockUser(roles = "USER") // Matches SecurityConfig: .anyRequest().authenticated()
    void testGetAllBooks() throws Exception {
        // Arrange
        Book book1 = new Book("Book 1");
        Book book2 = new Book("Book 2");
        when(libraryService.getAllBooks()).thenReturn(List.of(book1, book2));

        // Act & Assert
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Book 1"))
                .andExpect(jsonPath("$[1].title").value("Book 2"));

        verify(libraryService, times(1)).getAllBooks();
    }
}
