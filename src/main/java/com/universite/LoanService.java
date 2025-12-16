package com.universite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;
    
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }
    
    public Optional<Loan> getLoanById(Long id) {
        return loanRepository.findById(id);
    }
    
    public Loan createLoan(Loan loan) {
        if (loan.getLoanDate() == null) {
            loan.setLoanDate(LocalDate.now());
        }
        return loanRepository.save(loan);
    }
    
    public Loan updateLoan(Long id, Loan loanDetails) {
        return loanRepository.findById(id).map(loan -> {
            if (loanDetails.getLoanDate() != null) {
                loan.setLoanDate(loanDetails.getLoanDate());
            }
            if (loanDetails.getReturnDate() != null) {
                loan.setReturnDate(loanDetails.getReturnDate());
            }
            return loanRepository.save(loan);
        }).orElseThrow(() -> new RuntimeException("Prêt non trouvé"));
    }
    
    public void deleteLoan(Long id) {
        loanRepository.deleteById(id);
    }
    
    public List<Loan> getActiveLoans() {
        return loanRepository.findByReturnDateIsNull();
    }
    
    public Loan returnBook(Long loanId) {
        return loanRepository.findById(loanId).map(loan -> {
            loan.setReturnDate(LocalDate.now());
            return loanRepository.save(loan);
        }).orElseThrow(() -> new RuntimeException("Prêt non trouvé"));
    }
}