package com.universite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class LibraryManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryManagementApplication.class, args);
    }
    
    @Bean
    @Profile("!test") // NE PAS exécuter pendant les tests
    public CommandLineRunner demo(BookRepository bookRepository, 
                                  MemberRepository memberRepository,
                                  LoanRepository loanRepository) {
        return args -> {
            // Ajout de données de test
            Book book1 = new Book("Le Petit Prince", "Antoine de Saint-Exupéry", "Conte");
            Book book2 = new Book("1984", "George Orwell", "Science-Fiction");
            Book book3 = new Book("Harry Potter", "J.K. Rowling", "Fantasy");
            
            bookRepository.save(book1);
            bookRepository.save(book2);
            bookRepository.save(book3);
            
            Member member1 = new Member("Alice Dupont", "123 Rue de Paris", "0123456789");
            Member member2 = new Member("Bob Martin", "456 Avenue des Champs", "0987654321");
            
            memberRepository.save(member1);
            memberRepository.save(member2);
            
            System.out.println("Données de test ajoutées avec succès !");
        };
    }
}