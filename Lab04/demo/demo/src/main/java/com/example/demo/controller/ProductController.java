package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.model.Category;
import com.example.demo.service.ProductService;
import com.example.demo.service.CategoryService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // ================== INDEX ==================
    @GetMapping
    public String index(Model model) {
        model.addAttribute("listproduct", productService.getAll());
        return "product/products";
    }

    // ================== CREATE ==================
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAll());
        return "product/create";
    }

    @PostMapping("/create")
    public String create(
            @Valid Product newProduct,
            BindingResult result,
            @RequestParam(value = "category_id", required = false) Integer categoryId,
            @RequestParam(value = "imageProduct", required = false) MultipartFile imageProduct,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("product", newProduct);
            model.addAttribute("categories", categoryService.getAll());
            return "product/create";
        }

        if (imageProduct != null && !imageProduct.isEmpty()) {
            productService.updateImage(newProduct, imageProduct);
        }

        if (categoryId != null && categoryId > 0) {
            Category selectedCategory = categoryService.get(categoryId);
            newProduct.setCategory(selectedCategory);
        }

        productService.add(newProduct);

        return "redirect:/products";
    }

    // ================== EDIT ==================
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        Product find = productService.get(id);

        if (find == null) {
            return "error/404";
        }

        model.addAttribute("product", find);
        model.addAttribute("categories", categoryService.getAll());
        return "product/edit";
    }

    @PostMapping("/update")
    public String update(
            @Valid Product editProduct,
            BindingResult result,
            @RequestParam(value = "category_id", required = false) Integer categoryId,
            @RequestParam(value = "imageProduct", required = false) MultipartFile imageProduct,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("product", editProduct);
            model.addAttribute("categories", categoryService.getAll());
            return "product/edit";
        }

        if (imageProduct != null && !imageProduct.isEmpty()) {
            productService.updateImage(editProduct, imageProduct);
        }

        if (categoryId != null && categoryId > 0) {
            Category selectedCategory = categoryService.get(categoryId);
            editProduct.setCategory(selectedCategory);
        }

        productService.update(editProduct);

        return "redirect:/products";
    }

    // ================== DELETE ==================
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        Product find = productService.get(id);
        if (find != null) {
            productService.delete(id);
        }
        return "redirect:/products";
    }
}