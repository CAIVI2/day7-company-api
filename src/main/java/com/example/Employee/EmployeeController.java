package com.example.Employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("employees")
public class EmployeeController {
    private List<Employee> employees = new ArrayList<>();
    private int id = 0;

    public void resetEmployees() {
        employees.clear();
        id = 0;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee create(@RequestBody Employee employee) {
        int id = ++this.id;
        Employee newEmployee = new Employee(id, employee.name(), employee.age(), employee.gender(), employee.salary());
        employees.add(newEmployee);
        return newEmployee;
    }

    @GetMapping("/{id}")
    public Employee get(@PathVariable Integer id) {
        return employees.stream()
                .filter(employee -> employee.id() == id)
                .findFirst()
                .orElse(null);
    }

    @GetMapping
    public List<Employee> index(
            @RequestParam(required = false, value = "gender") String gender
            ) {
        return employees.stream()
                .filter(employee -> gender.equals(employee.gender()))
                .collect(Collectors.toList());
    }
}
