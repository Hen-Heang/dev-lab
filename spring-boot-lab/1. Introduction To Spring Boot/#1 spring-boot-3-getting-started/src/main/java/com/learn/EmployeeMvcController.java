package com.learn;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Optional;

/**
 * An MVC CONTROLLER that renders HTML pages (not a REST/JSON API).
 *
 * @Controller (vs @RestController): each method returns a VIEW NAME (a String
 * like "list-employees"). Spring + Thymeleaf then renders the matching
 * template at src/main/resources/templates/list-employees.html.
 * (@RestController would instead write the return value straight to the
 * HTTP response body as JSON.)
 *
 * The `Model` object is the bridge from controller to view: whatever you put
 * in it (model.addAttribute(...)) becomes available inside the HTML template.
 */
@Controller
@RequiredArgsConstructor                 // constructor injection of EmployeeService
@RequestMapping(value = {"/"})           // base path - this controller handles "/"
public class EmployeeMvcController {

    private final EmployeeService employeeService;   // injected by Spring

    /** GET "/" -> show the list of all employees. */
    @RequestMapping
    public String getAllEmployee(Model model) {
        List<Employee> employees = employeeService.getAll();
        model.addAttribute("employees", employees);  // hand the data to the view
        return "list-employees";                     // -> templates/list-employees.html
    }

    /**
     * GET "/edit" (new) or "/edit/{id}" (existing) -> show the add/edit form.
     * @PathVariable binds the {id} part of the URL to the method parameter.
     * Optional<Long> means the id may or may not be in the URL.
     */
    @RequestMapping(path = {"/edit", "/edit/{id}"})
    public String edit(Model model, @PathVariable("id") Optional<Long> id) throws EmployeeRecordNotFoundException {
        if(id.isPresent()) {
            // Editing an existing employee -> load it into the form.
            Employee employee = employeeService.findById(id.get());
            model.addAttribute("employee", employee);
        }else {
            // Adding a new one -> give the form a blank Employee.
            model.addAttribute("employee", new Employee());
        }
        return "add-edit-employee";
    }

    /** GET "/delete/{id}" -> delete, then redirect back to the list. */
    @RequestMapping(path = "/delete/{id}")
    public String delete(Model model, @PathVariable("id") Long id) throws EmployeeRecordNotFoundException {
        employeeService.delete(id);
        return "redirect:/";   // "redirect:" tells the browser to GET "/" again (Post/Redirect/Get pattern)
    }

    /**
     * POST "/createEmployee" -> the form submits here.
     * Spring automatically binds the form fields onto a new Employee object
     * (this is "data binding" by matching field names).
     */
    @RequestMapping(path = "/createEmployee", method = RequestMethod.POST)
    public String createOrUpdate(Employee employee) {
        employeeService.createOrUpdate(employee);
        return "redirect:/";
    }
}
