package com.example.J2EE_Ktragiuaki;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void homePageDisplaysFiveCategoriesPerPage() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("home"))
            .andExpect(model().attribute("currentPage", is(0)))
            .andExpect(model().attribute("categoryPage", hasProperty("size", is(5))))
            .andExpect(model().attributeExists("pageNumbers", "displayTotalPages", "previousPage", "nextPage"));
    }

    @Test
    void homePageAcceptsPaginationParameter() throws Exception {
        mockMvc.perform(get("/").param("page", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("home"))
            .andExpect(model().attribute("currentPage", is(1)));
    }
}
