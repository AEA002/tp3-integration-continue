package com.universite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    // Trouver les prêts par date d'emprunt
    List<Loan> findByLoanDate(LocalDate loanDate);
    
    // Trouver les prêts non retournés (returnDate est null)
    List<Loan> findByReturnDateIsNull();
    
    // Trouver les prêts retournés après une certaine date
    List<Loan> findByReturnDateAfter(LocalDate date);
}