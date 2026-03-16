package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import com.example.demo.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private org.springframework.core.io.ResourceLoader resourceLoader;

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    @GetMapping
    public String listProducts(Model model) {

        List<Product> productList = productService.getAllProducts();
        model.addAttribute("products", productList);
        model.addAttribute("isAdmin", isAdmin());

        return "product/products";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {

        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());

        return "product/create";
    }

    @PostMapping("/create")
    public String createProduct(@ModelAttribute Product product,
            @RequestParam("imageProduct") MultipartFile file,
            @RequestParam(value = "category", required = false) Integer categoryId) throws IOException {

        if (!file.isEmpty()) {
            // Validate file
            if (!isValidImageFile(file)) {
                return "redirect:/products/create?error=Invalid file type";
            }

            String fileName = saveUploadedFile(file);
            if (fileName != null) {
                product.setImage(fileName);
            }
        }

        if (categoryId != null) {
            product.setCategory(categoryService.getCategoryById(categoryId));
        }

        productService.saveProduct(product);

        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {

        Product product = productService.getProductById(id);

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());

        return "product/edit";
    }

    @PostMapping("/update")
    public String updateProduct(@ModelAttribute Product product,
            @RequestParam("imageProduct") MultipartFile file,
            @RequestParam(value = "category", required = false) Integer categoryId) throws IOException {

        if (!file.isEmpty()) {
            // Validate file
            if (!isValidImageFile(file)) {
                return "redirect:/products/edit/" + product.getId() + "?error=Invalid file type";
            }

            String fileName = saveUploadedFile(file);
            if (fileName != null) {
                product.setImage(fileName);
            }
        }

        if (categoryId != null) {
            product.setCategory(categoryService.getCategoryById(categoryId));
        }

        productService.saveProduct(product);

        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Integer id) {

        productService.deleteProduct(id);

        return "redirect:/products";
    }

    @GetMapping("/image/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) throws IOException {
        String userHome = System.getProperty("user.home");
        String imagePath = userHome + File.separator + "Demo_Validation_Images" + File.separator + filename;
        Path path = Paths.get(imagePath);

        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        byte[] imageBytes = Files.readAllBytes(path);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }

    private boolean isValidImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    private String saveUploadedFile(MultipartFile file) throws IOException {
        try {
            // Use absolute path - User's home directory
            String userHome = System.getProperty("user.home");
            String uploadDir = userHome + File.separator + "Demo_Validation_Images" + File.separator;
            File uploadFolder = new File(uploadDir);

            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            File saveFile = new File(uploadDir + uniqueFilename);
            file.transferTo(saveFile);

            return uniqueFilename;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}