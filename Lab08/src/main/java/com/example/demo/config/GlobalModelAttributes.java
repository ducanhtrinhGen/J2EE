package com.example.demo.config;

import com.example.demo.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @Autowired
    private CartService cartService;

    @ModelAttribute("cartItemCount")
    public int cartItemCount(HttpSession session) {
        return cartService.getTotalQuantity(session);
    }
}
