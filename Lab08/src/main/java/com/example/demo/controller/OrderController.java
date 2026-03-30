package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable int id, Model model) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return "redirect:/cart";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginName = authentication != null ? authentication.getName() : null;

        if (order.getAccount() != null && loginName != null && !loginName.equals(order.getAccount().getLogin_name())) {
            return "redirect:/products";
        }

        int totalQuantity = order.getOrderDetails().stream()
                .mapToInt(detail -> detail.getQuantity())
                .sum();

        model.addAttribute("order", order);
        model.addAttribute("orderDetails", order.getOrderDetails());
        model.addAttribute("orderTotalQuantity", totalQuantity);

        return "product/order-success";
    }
}
