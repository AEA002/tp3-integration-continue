package com.universite;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    public void testGetAllBooks() {
        Book book1 = new Book("Titre 1", "Auteur 1", "Genre 1");
        Book book2 = new Book("Titre 2", "Auteur 2", "Genre 2");
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<Book> books = bookService.getAllBooks();
        assertEquals(2, books.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void testGetBookById() {
        Book book = new Book("Titre", "Auteur", "Genre");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.getBookById(1L);
        assertTrue(result.isPresent());
        assertEquals("Titre", result.get().getTitle());
    }

    @Test
    public void testSaveBook() {
        Book book = new Book("Titre", "Auteur", "Genre");
        when(bookRepository.save(book)).thenReturn(book);

        Book saved = bookService.saveBook(book);
        assertNotNull(saved);
        assertEquals("Titre", saved.getTitle());
    }
}