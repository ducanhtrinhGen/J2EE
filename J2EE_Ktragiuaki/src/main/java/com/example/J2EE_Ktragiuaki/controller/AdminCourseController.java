package com.example.J2EE_Ktragiuaki.controller;

import com.example.J2EE_Ktragiuaki.entity.Category;
import com.example.J2EE_Ktragiuaki.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/courses")
public class AdminCourseController {

    private final CategoryService categoryService;

    public AdminCourseController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("courses", categoryService.getAllCategories());
        return "admin/course-list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("course", new Category());
        model.addAttribute("formTitle", "Thêm Khóa Học Mới");
        model.addAttribute("formAction", "/admin/courses/create");
        return "admin/course-form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("course") Category category) {
        categoryService.saveCategory(category);
        return "redirect:/admin/courses";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("course", categoryService.getCategoryById(id));
        model.addAttribute("formTitle", "Cập Nhật Khóa Học");
        model.addAttribute("formAction", "/admin/courses/edit/" + id);
        return "admin/course-form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("course") Category formCategory) {
        Category currentCategory = categoryService.getCategoryById(id);
        currentCategory.setName(formCategory.getName());
        currentCategory.setCredits(formCategory.getCredits());
        currentCategory.setLecturer(formCategory.getLecturer());
        currentCategory.setImageUrl(formCategory.getImageUrl());
        categoryService.saveCategory(currentCategory);
        return "redirect:/admin/courses";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/courses";
    }
}
