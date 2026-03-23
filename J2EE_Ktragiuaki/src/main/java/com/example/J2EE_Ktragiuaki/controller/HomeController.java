package com.example.J2EE_Ktragiuaki.controller;

import java.util.List;
import java.util.stream.IntStream;

import com.example.J2EE_Ktragiuaki.entity.Category;
import com.example.J2EE_Ktragiuaki.service.CategoryService;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final CategoryService categoryService;

    public HomeController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping({"/", "/home"})
    public String home(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Category> categoryPage = categoryService.getCategories(page);
        int currentPage = Math.max(page, 0);

        if (categoryPage.getTotalPages() > 0 && currentPage >= categoryPage.getTotalPages()) {
            currentPage = categoryPage.getTotalPages() - 1;
            categoryPage = categoryService.getCategories(currentPage);
        }

        List<Integer> pageNumbers = IntStream.range(0, categoryPage.getTotalPages())
            .boxed()
            .toList();
        int displayTotalPages = Math.max(categoryPage.getTotalPages(), 1);
        int previousPage = Math.max(currentPage - 1, 0);
        int nextPage = categoryPage.hasNext() ? currentPage + 1 : currentPage;

        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("displayTotalPages", displayTotalPages);
        model.addAttribute("previousPage", previousPage);
        model.addAttribute("nextPage", nextPage);
        return "home";
    }
}
