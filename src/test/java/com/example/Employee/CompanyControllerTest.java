package com.example.Employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyController companyController;

    @BeforeEach
    public void setup() {
        companyController.resetCompanies();
    }

    @Test
    void should_return_created_company_when_post() throws Exception {
        String requestBody = """
                {
                    "name": "spring"
                }
                """;

        MockHttpServletRequestBuilder request = post("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("spring"));
    }

    @Test
    void should_return_companies_when_list_all() throws Exception {
        companyController.create(new Company(null, "spring"));
        companyController.create(new Company(null, "boot"));

        MockHttpServletRequestBuilder request = get("/companies")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void should_return_company_when_get_company_with_id_exists() throws Exception {
        Company company = new Company(null, "spring");
        Company expect = companyController.create(company);

        MockHttpServletRequestBuilder request = get("/companies/" + expect.id())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expect.id()))
                .andExpect(jsonPath("$.name").value(expect.name()));
    }

    @Test
    void should_return_page_companies_when_list_page() throws Exception {
        companyController.create(new Company(null, "c1"));
        companyController.create(new Company(null, "c2"));
        companyController.create(new Company(null, "c3"));
        companyController.create(new Company(null, "c4"));
        companyController.create(new Company(null, "c5"));
        companyController.create(new Company(null, "c6"));

        MockHttpServletRequestBuilder request = get("/companies?page=1&size=5")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void should_return_updated_company_when_update_company() throws Exception {
        Company company = companyController.create(new Company(null, "spring"));
        String requestBody = """
                {
                    "name": "spring-updated"
                }
                """;

        MockHttpServletRequestBuilder request = put("/companies/" + company.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(company.id()))
                .andExpect(jsonPath("$.name").value("spring-updated"));
    }

    @Test
    void should_return_204_when_delete_company() throws Exception {
        Company company = companyController.create(new Company(null, "spring"));

        MockHttpServletRequestBuilder request = delete("/companies/" + company.id())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }
}
