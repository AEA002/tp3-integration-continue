package com.universite;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {
    @Mock
    private LoanRepository loanRepository;
    
    @InjectMocks
    private LoanService loanService;
    
    @Test
    public void testGetAllLoans() {
        Loan loan1 = new Loan(LocalDate.now(), null);
        Loan loan2 = new Loan(LocalDate.now().minusDays(5), LocalDate.now());
        when(loanRepository.findAll()).thenReturn(Arrays.asList(loan1, loan2));
        
        List<Loan> loans = loanService.getAllLoans();
        assertEquals(2, loans.size());
    }
    
    @Test
    public void testCreateLoan() {
        Loan loan = new Loan(null, null);
        Loan savedLoan = new Loan(LocalDate.now(), null);
        when(loanRepository.save(any(Loan.class))).thenReturn(savedLoan);
        
        Loan result = loanService.createLoan(loan);
        assertNotNull(result.getLoanDate());
        assertNull(result.getReturnDate());
    }
    
    @Test
    public void testReturnBook() {
        Loan loan = new Loan(LocalDate.now().minusDays(7), null);
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);
        
        Loan returned = loanService.returnBook(1L);
        assertNotNull(returned.getReturnDate());
        assertEquals(LocalDate.now(), returned.getReturnDate());
    }
    
    @Test
    public void testGetActiveLoans() {
        Loan activeLoan = new Loan(LocalDate.now(), null);
        when(loanRepository.findByReturnDateIsNull()).thenReturn(Arrays.asList(activeLoan));
        
        List<Loan> activeLoans = loanService.getActiveLoans();
        assertEquals(1, activeLoans.size());
        assertNull(activeLoans.get(0).getReturnDate());
    }
}