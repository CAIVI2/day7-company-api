package com.example.Employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("companies")
public class CompanyController {
    private List<Company> companies = new ArrayList<>();
    private int id = 0;

    public void resetCompanies() {
        companies.clear();
        id = 0;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company create(@RequestBody Company company) {
        int id = ++this.id;
        Company newCompany = new Company(id, company.name());
        companies.add(newCompany);
        return newCompany;
    }

    @GetMapping
    public List<Company> index() {
        return companies;
    }

    @GetMapping("/{id}")
    public Company get(@PathVariable Integer id) {
        return companies.stream()
                .filter(company -> company.id().equals(id))
                .findFirst()
                .orElse(null);
    }
}

