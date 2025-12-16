package com.universite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;
    
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }
    
    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }
    
    public Member createMember(Member member) {
        return memberRepository.save(member);
    }
    
    public Member updateMember(Long id, Member memberDetails) {
        return memberRepository.findById(id).map(member -> {
            if (memberDetails.getName() != null) {
                member.setName(memberDetails.getName());
            }
            if (memberDetails.getAddress() != null) {
                member.setAddress(memberDetails.getAddress());
            }
            if (memberDetails.getPhoneNumber() != null) {
                member.setPhoneNumber(memberDetails.getPhoneNumber());
            }
            return memberRepository.save(member);
        }).orElseThrow(() -> new RuntimeException("Membre non trouvé"));
    }
    
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
    
    public List<Member> searchByName(String name) {
        return memberRepository.findByNameContainingIgnoreCase(name);
    }
    
    public Member findByPhone(String phone) {
        return memberRepository.findByPhoneNumber(phone);
    }
}