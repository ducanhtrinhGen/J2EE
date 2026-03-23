package com.example.J2EE_Ktragiuaki.controller;

import com.example.J2EE_Ktragiuaki.dto.StudentRegistrationForm;
import com.example.J2EE_Ktragiuaki.service.StudentRegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class StudentRegistrationController {

    private final StudentRegistrationService registrationService;

    public StudentRegistrationController(StudentRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping
    public String registerForm(Model model) {
        if (!model.containsAttribute("studentForm")) {
            model.addAttribute("studentForm", new StudentRegistrationForm());
        }
        return "register";
    }

    @PostMapping
    public String register(@ModelAttribute("studentForm") StudentRegistrationForm form, Model model) {
        if (registrationService.usernameExists(form.getUsername())) {
            model.addAttribute("errorMessage", "Username đã tồn tại.");
            return "register";
        }

        if (registrationService.emailExists(form.getEmail())) {
            model.addAttribute("errorMessage", "Email đã tồn tại.");
            return "register";
        }

        registrationService.registerStudent(form);
        model.addAttribute("successMessage", "Đăng ký thành công. Tài khoản được gán quyền STUDENT.");
        model.addAttribute("studentForm", new StudentRegistrationForm());
        return "register";
    }
}
