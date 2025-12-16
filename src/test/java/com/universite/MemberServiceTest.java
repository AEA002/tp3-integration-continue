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
public class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;
    
    @InjectMocks
    private MemberService memberService;
    
    @Test
    public void testGetAllMembers() {
        Member member1 = new Member("Alice", "Paris", "0123456789");
        Member member2 = new Member("Bob", "Lyon", "0987654321");
        when(memberRepository.findAll()).thenReturn(Arrays.asList(member1, member2));
        
        List<Member> members = memberService.getAllMembers();
        assertEquals(2, members.size());
        assertEquals("Alice", members.get(0).getName());
    }
    
    @Test
    public void testCreateMember() {
        Member member = new Member("Charlie", "Marseille", "0112233445");
        when(memberRepository.save(member)).thenReturn(member);
        
        Member saved = memberService.createMember(member);
        assertEquals("Charlie", saved.getName());
        assertEquals("Marseille", saved.getAddress());
    }
    
    @Test
    public void testSearchByName() {
        Member member = new Member("David", "Toulouse", "0556677889");
        when(memberRepository.findByNameContainingIgnoreCase("dav")).thenReturn(Arrays.asList(member));
        
        List<Member> results = memberService.searchByName("dav");
        assertEquals(1, results.size());
        assertTrue(results.get(0).getName().contains("David"));
    }
    
    @Test
    public void testUpdateMember() {
        Member existing = new Member("Eric", "Nantes", "0443322110");
        Member updates = new Member("Eric Updated", "Nantes Updated", "0998877665");
        
        when(memberRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(memberRepository.save(any(Member.class))).thenReturn(existing);
        
        Member updated = memberService.updateMember(1L, updates);
        assertEquals("Eric Updated", updated.getName());
        assertEquals("Nantes Updated", updated.getAddress());
    }
}