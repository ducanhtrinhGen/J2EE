package com.example.J2EE_Ktragiuaki.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.J2EE_Ktragiuaki.entity.Category;
import com.example.J2EE_Ktragiuaki.repository.CategoryRepository;

@Service
public class CategoryService {

    public static final int PAGE_SIZE = 5;

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Page<Category> getCategories(int page) {
        int safePage = Math.max(page, 0);
        PageRequest pageable = PageRequest.of(safePage, PAGE_SIZE, Sort.by("id").ascending());
        return categoryRepository.findAll(pageable);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll(Sort.by("id").ascending());
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Khong tim thay hoc phan voi id = " + id));
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
