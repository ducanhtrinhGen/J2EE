package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void saveCategory(@NonNull Category category) {
        categoryRepository.save(category);
    }

    public Category getCategoryById(@NonNull Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void deleteCategory(@NonNull Integer id) {
        categoryRepository.deleteById(id);
    }
}
