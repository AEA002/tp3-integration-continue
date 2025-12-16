package com.universite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // Trouver des livres par auteur
    List<Book> findByAuthor(String author);
    
    // Trouver des livres par genre
    List<Book> findByGenre(String genre);
    
    // Trouver des livres par titre (contient)
    List<Book> findByTitleContainingIgnoreCase(String title);
}