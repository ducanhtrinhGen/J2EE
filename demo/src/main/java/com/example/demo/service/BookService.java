package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import com.example.demo.model.Book;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private List<Book> books = new ArrayList<>();

    public List<Book> getAllBooks() {
        return books;
    }

    public Book getBookById(int id) {
        return books.stream().filter(book -> book.getId() == id).findFirst().orElse(null);
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void updateBook(int id, Book book) {
        books.stream().filter(existingBook -> existingBook.getId() == id).findFirst().ifPresent(existingBook -> {
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
        });

    }

    public void deleteBook(int id) {
        books.removeIf(book -> book.getId() == id);
    }
}