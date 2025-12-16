package com.universite;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ApplicationTest {
    @Test
    public void contextLoads() {
        // Test que l'application Spring démarre correctement
        assertTrue(true);
    }
    
    @Test
    public void testBookCreation() {
        Book book = new Book("Test", "Author", "Genre");
        assertNotNull(book);
        assertEquals("Test", book.getTitle());
    }
    
    @Test
    public void testMemberCreation() {
        Member member = new Member("Name", "Address", "Phone");
        assertNotNull(member);
        assertEquals("Name", member.getName());
    }
    
    @Test
    public void testLoanCreation() {
        Loan loan = new Loan();
        loan.setLoanDate(java.time.LocalDate.now());
        assertNotNull(loan.getLoanDate());
    }
}