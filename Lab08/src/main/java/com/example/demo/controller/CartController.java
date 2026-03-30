package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.CartService;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        model.addAttribute("cartItems", cartService.getCartItems(session));
        model.addAttribute("cartTotalQuantity", cartService.getTotalQuantity(session));
        model.addAttribute("cartTotalAmount", cartService.getTotalAmount(session));
        return "product/cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam int productId,
            @RequestParam(defaultValue = "1") int quantity,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(defaultValue = "default") String sort,
            @RequestParam(defaultValue = "0") int page,
            HttpSession session) {

        Product product = productService.getProductById(productId);
        cartService.addToCart(product, quantity, session);

        return buildProductsRedirect(keyword, categoryId, sort, page);
    }

    @PostMapping("/cart/update")
    public String updateCart(@RequestParam int productId,
            @RequestParam(defaultValue = "1") int quantity,
            HttpSession session) {
        cartService.updateQuantity(productId, quantity, session);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove/{productId}")
    public String removeFromCart(@PathVariable int productId, HttpSession session) {
        cartService.removeFromCart(productId, session);
        return "redirect:/cart";
    }

    @PostMapping("/cart/checkout")
    public String checkout(HttpSession session, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginName = authentication != null ? authentication.getName() : null;

        try {
            int orderId = orderService.checkout(loginName, session).getId();
            return "redirect:/orders/" + orderId;
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("checkoutError", ex.getMessage());
            return "redirect:/cart";
        }
    }

    private String buildProductsRedirect(String keyword, Integer categoryId, String sort, int page) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/products")
                .queryParam("page", Math.max(page, 0));

        if (keyword != null && !keyword.isBlank()) {
            builder.queryParam("keyword", keyword);
        }

        if (categoryId != null) {
            builder.queryParam("categoryId", categoryId);
        }

        if (sort != null && !sort.isBlank() && !"default".equals(sort)) {
            builder.queryParam("sort", sort);
        }

        return "redirect:" + builder.toUriString();
    }
}
