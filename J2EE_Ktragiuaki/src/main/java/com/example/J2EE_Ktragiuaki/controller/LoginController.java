package com.example.J2EE_Ktragiuaki.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(
        @RequestParam(required = false) String error,
        @RequestParam(required = false) String logout,
        Model model
    ) {
        if (error != null) {
            model.addAttribute("errorMessage", "Sai username hoặc password.");
        }

        if (logout != null) {
            model.addAttribute("successMessage", "Đăng xuất thành công.");
        }

        return "login";
    }
}
