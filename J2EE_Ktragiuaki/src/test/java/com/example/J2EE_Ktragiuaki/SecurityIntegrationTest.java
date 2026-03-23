package com.example.J2EE_Ktragiuaki;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void coursesPageIsAccessibleWithoutLogin() throws Exception {
        mockMvc.perform(get("/courses"))
            .andExpect(status().isOk());
    }

    @Test
    void adminPageRedirectsAnonymousUserToLogin() throws Exception {
        mockMvc.perform(get("/admin/courses"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login"));
    }

    @Test
    void adminPageAllowsAdminRole() throws Exception {
        mockMvc.perform(get("/admin/courses").with(user("admin").roles("ADMIN")))
            .andExpect(status().isOk());
    }

    @Test
    void enrollPageAllowsStudentRole() throws Exception {
        mockMvc.perform(get("/enroll").with(user("student").roles("STUDENT")))
            .andExpect(status().isOk());
    }

    @Test
    void enrollPageRejectsAdminRole() throws Exception {
        mockMvc.perform(get("/enroll").with(user("admin").roles("ADMIN")))
            .andExpect(status().isForbidden());
    }
}
