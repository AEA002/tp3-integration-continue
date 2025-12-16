package com.universite;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@ActiveProfiles("test") // Active le profil "test"
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllBooks_Success() throws Exception {
        // Arrange
        Book book1 = new Book("Le Petit Prince", "Antoine de Saint-Exupéry", "Conte");
        book1.setId(1L);
        Book book2 = new Book("1984", "George Orwell", "Science-Fiction");
        book2.setId(2L);
        
        List<Book> books = Arrays.asList(book1, book2);
        
        when(bookService.getAllBooks()).thenReturn(books);

        // Act & Assert
        mockMvc.perform(get("/api/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Le Petit Prince")));
    }

    @Test
    public void testGetBookById_Success() throws Exception {
        // Arrange
        Book book = new Book("Harry Potter", "J.K. Rowling", "Fantasy");
        book.setId(1L);
        
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        // Act & Assert
        mockMvc.perform(get("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Harry Potter")));
    }

    @Test
    public void testGetBookById_NotFound() throws Exception {
        // Arrange
        when(bookService.getBookById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/books/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateBook_Success() throws Exception {
        // Arrange
        Book newBook = new Book("Nouveau Livre", "Nouvel Auteur", "Nouveau Genre");
        Book savedBook = new Book("Nouveau Livre", "Nouvel Auteur", "Nouveau Genre");
        savedBook.setId(1L);
        
        when(bookService.saveBook(any(Book.class))).thenReturn(savedBook);

        String bookJson = "{\"title\":\"Nouveau Livre\",\"author\":\"Nouvel Auteur\",\"genre\":\"Nouveau Genre\"}";

        // Act & Assert
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Nouveau Livre")));
    }

    @Test
    public void testDeleteBook_Success() throws Exception {
        // Arrange
        doNothing().when(bookService).deleteBook(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    // Réduis le nombre de tests pour simplifier
    @Test
    public void testCreateBook_InvalidJson() throws Exception {
        String invalidJson = "{invalid json}";
        
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}