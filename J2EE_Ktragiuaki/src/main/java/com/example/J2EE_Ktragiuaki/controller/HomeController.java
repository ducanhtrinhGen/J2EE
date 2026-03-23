package com.example.J2EE_Ktragiuaki.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.example.J2EE_Ktragiuaki.entity.Category;
import com.example.J2EE_Ktragiuaki.entity.Student;
import com.example.J2EE_Ktragiuaki.repository.StudentRepository;
import com.example.J2EE_Ktragiuaki.service.CategoryService;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.transaction.annotation.Transactional;

@Controller
public class HomeController {

    private final CategoryService categoryService;
    private final StudentRepository studentRepository;

    public HomeController(CategoryService categoryService, StudentRepository studentRepository) {
        this.categoryService = categoryService;
        this.studentRepository = studentRepository;
    }

    @GetMapping({"/", "/home", "/courses"})
    @Transactional(readOnly = true)
    public String home(@RequestParam(defaultValue = "0") int page, Model model, Principal principal) {
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

        Set<Long> enrolledCourseIds = Collections.emptySet();
        if (principal != null) {
            try {
                java.util.Optional<Student> studentOpt = studentRepository.findByUsername(principal.getName());
                if (studentOpt.isPresent()) {
                    enrolledCourseIds = studentOpt.get().getCourses().stream()
                        .map(Category::getId)
                        .collect(Collectors.toSet());
                }
            } catch (Exception exception) {
                enrolledCourseIds = Collections.emptySet();
            }
        }

        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("displayTotalPages", displayTotalPages);
        model.addAttribute("previousPage", previousPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("enrolledCourseIds", enrolledCourseIds);
        return "home";
    }
}
