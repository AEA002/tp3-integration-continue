package com.universite;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.Arrays;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.context.ActiveProfiles;

@WebMvcTest(LoanController.class)
@ActiveProfiles("test")
public class LoanControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private LoanService loanService;
    
    @Test
    public void testGetAllLoans() throws Exception {
        Loan loan = new Loan(LocalDate.now(), null);
        when(loanService.getAllLoans()).thenReturn(Arrays.asList(loan));
        
        mockMvc.perform(get("/api/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].loanDate").exists());
    }
    
    @Test
    public void testCreateLoan() throws Exception {
        Loan loan = new Loan(LocalDate.now(), null);
        when(loanService.createLoan(any(Loan.class))).thenReturn(loan);
        
        String loanJson = "{\"loanDate\":\"2024-01-15\",\"returnDate\":null}";
        
        mockMvc.perform(post("/api/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loanJson))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testReturnBook() throws Exception {
        Loan returnedLoan = new Loan(LocalDate.now().minusDays(7), LocalDate.now());
        when(loanService.returnBook(1L)).thenReturn(returnedLoan);
        
        mockMvc.perform(post("/api/loans/1/return"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.returnDate").exists());
    }
}