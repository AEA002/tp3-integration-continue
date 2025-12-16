package com.universite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // Trouver les membres par nom
    List<Member> findByNameContainingIgnoreCase(String name);
    
    // Trouver les membres par ville (dans l'adresse)
    List<Member> findByAddressContainingIgnoreCase(String city);
    
    // Trouver un membre par numéro de téléphone
    Member findByPhoneNumber(String phoneNumber);
}