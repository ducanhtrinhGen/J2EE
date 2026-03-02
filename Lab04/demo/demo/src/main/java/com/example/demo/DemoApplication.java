package com.example.demo;

import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(CategoryService categoryService) {
		return args -> {
			// Thêm danh mục mặc định
			Category asus = new Category();
			asus.setName("Asus");
			categoryService.add(asus);

			Category dell = new Category();
			dell.setName("Dell");
			categoryService.add(dell);

			Category macbook = new Category();
			macbook.setName("Macbook");
			categoryService.add(macbook);
		};
	}

}
