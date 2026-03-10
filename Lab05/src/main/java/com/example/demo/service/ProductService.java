package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void saveProduct(@NonNull Product product) {
        productRepository.save(product);
    }

    public Product getProductById(@NonNull Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void deleteProduct(@NonNull Long id) {
        productRepository.deleteById(id);
    }
}