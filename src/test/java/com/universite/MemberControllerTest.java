package com.universite;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.context.ActiveProfiles;

@WebMvcTest(MemberController.class)
@ActiveProfiles("test") 
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private MemberService memberService;
    
    @Test
    public void testGetAllMembers() throws Exception {
        Member member = new Member("Test", "Address", "0123456789");
        when(memberService.getAllMembers()).thenReturn(Arrays.asList(member));
        
        mockMvc.perform(get("/api/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test"));
    }
    
    @Test
    public void testGetMemberById() throws Exception {
        Member member = new Member("Alice", "Paris", "0123456789");
        when(memberService.getMemberById(1L)).thenReturn(Optional.of(member));
        
        mockMvc.perform(get("/api/members/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }
    
    @Test
    public void testCreateMember() throws Exception {
        Member member = new Member("Bob", "Lyon", "0987654321");
        when(memberService.createMember(any(Member.class))).thenReturn(member);
        
        String memberJson = "{\"name\":\"Bob\",\"address\":\"Lyon\",\"phoneNumber\":\"0987654321\"}";
        
        mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(memberJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bob"));
    }
    
    @Test
    public void testSearchByName() throws Exception {
        Member member = new Member("Charlie", "Marseille", "0112233445");
        when(memberService.searchByName("char")).thenReturn(Arrays.asList(member));
        
        mockMvc.perform(get("/api/members/search?name=char"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Charlie"));
    }
}