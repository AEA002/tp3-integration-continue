package com.universite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class BookRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private BookRepository bookRepository;
    
    @BeforeEach
    public void setUp() {
        // Nettoyer avant chaque test
        bookRepository.deleteAll();
    }
    
    @Test
    public void testSaveAndFindBook() {
        // Arrange
        Book book = new Book("Test Book", "Test Author", "Test Genre");
        
        // Act
        Book savedBook = bookRepository.save(book);
        entityManager.flush();
        entityManager.clear();
        
        // Assert
        List<Book> books = bookRepository.findAll();
        assertEquals(1, books.size());
        assertEquals("Test Book", books.get(0).getTitle());
    }
    
    @Test
    public void testFindByAuthor() {
        // Arrange
        Book book1 = new Book("Book1", "Author1", "Genre1");
        Book book2 = new Book("Book2", "Author1", "Genre2");
        Book book3 = new Book("Book3", "Author2", "Genre1");
        
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
        entityManager.flush();
        entityManager.clear();
        
        // Act
        List<Book> booksByAuthor1 = bookRepository.findByAuthor("Author1");
        List<Book> booksByAuthor2 = bookRepository.findByAuthor("Author2");
        
        // Assert
        assertEquals(2, booksByAuthor1.size());
        assertEquals(1, booksByAuthor2.size());
    }
    
    @Test
    public void testFindByTitleContaining() {
        // Arrange
        Book book1 = new Book("Harry Potter", "J.K. Rowling", "Fantasy");
        Book book2 = new Book("Potter's Field", "Other Author", "Mystery");
        
        bookRepository.save(book1);
        bookRepository.save(book2);
        entityManager.flush();
        entityManager.clear();
        
        // Act
        List<Book> booksWithPotter = bookRepository.findByTitleContainingIgnoreCase("potter");
        List<Book> booksWithHarry = bookRepository.findByTitleContainingIgnoreCase("harry");
        
        // Assert
        assertEquals(2, booksWithPotter.size()); // Les deux contiennent "potter" (ignore case)
        assertEquals(1, booksWithHarry.size());
    }
}