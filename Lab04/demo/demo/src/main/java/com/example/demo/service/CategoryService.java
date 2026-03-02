package com.example.demo.service;

import com.example.demo.model.Category;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    List<Category> listCategory = new ArrayList<>();

    public List<Category> getAll() {
        return listCategory;
    }

    public Category get(int id) {
        return listCategory.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void add(Category newCategory) {
        int maxId = listCategory.stream()
                .mapToInt(Category::getId)
                .max()
                .orElse(0);

        newCategory.setId(maxId + 1);
        listCategory.add(newCategory);
    }

    public void update(Category editCategory) {
        Category find = get(editCategory.getId());
        if (find != null) {
            find.setName(editCategory.getName());
        }
    }

    public void delete(int id) {
        Category find = get(id);
        if (find != null) {
            listCategory.remove(find);
        }
    }
}
