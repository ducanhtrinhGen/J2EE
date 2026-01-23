/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package btvn;

/**
 *
 * @author DUCANH
 */
import java.util.Scanner;

public class Book {
    private int id; // Ma sach
    private String title; // Ten sach
    private String author; // Tac gia
    private double price; // Don gia

    // Constructor đầy đủ tham số
    public Book(int id, String title, String author, double price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public Book() {
    } // Constructor mac dinh

    // Getter và Setter
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public double getPrice() {
        return price;
    }

    // Ham nhap thong tin
    public void input() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhap ID sach: ");
        this.id = Integer.parseInt(sc.nextLine());
        System.out.print("Nhap ten sach: ");
        this.title = sc.nextLine();
        System.out.print("Nhap tac gia: ");
        this.author = sc.nextLine();
        System.out.print("Nhap don gia: ");
        this.price = Double.parseDouble(sc.nextLine());
    }

    // Ham xuat thong tin su dung Text Block
    public void output() {
        String msg = """
                BOOK [ID: %d | Tieu De: %s | Tac Gia: %s | Gia: %.2f]
                """.formatted(id, title, author, price);
        System.out.print(msg);
    }
}
