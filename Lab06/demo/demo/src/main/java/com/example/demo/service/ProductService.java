package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Lấy tất cả sản phẩm
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Thêm hoặc cập nhật sản phẩm
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    // Lấy sản phẩm theo ID
    public Product getProductById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    // Xóa sản phẩm
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }
}