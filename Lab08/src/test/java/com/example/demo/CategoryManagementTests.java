package com.example.demo;

import com.example.demo.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryManagementTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void adminShouldCreateCategorySuccessfully() throws Exception {
        long beforeCount = categoryRepository.count();

        mockMvc.perform(post("/categories/create")
                        .session((org.springframework.mock.web.MockHttpSession) mockMvc.perform(post("/login")
                                        .param("username", "admin")
                                        .param("password", "admin123"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/products"))
                                .andReturn()
                                .getRequest()
                                .getSession(false))
                        .param("name", "Gaming"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories"));

        assertEquals(beforeCount + 1, categoryRepository.count());
        assertTrue(categoryRepository.findAll().stream().anyMatch(c -> "Gaming".equals(c.getName())));
    }

    @Test
    void normalUserShouldNotAccessCategoryManagementPage() throws Exception {
        mockMvc.perform(get("/categories")
                        .session((org.springframework.mock.web.MockHttpSession) mockMvc.perform(post("/login")
                                        .param("username", "user1")
                                        .param("password", "user123"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/products"))
                                .andReturn()
                                .getRequest()
                                .getSession(false)))
                .andExpect(status().isForbidden());
    }
}
