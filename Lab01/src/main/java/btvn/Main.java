/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package btvn;

/**
 *
 * @author DUCANH
 */
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Book> listBook = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        int chon;

        do {
            System.out.println("\n--- CHUONG TRINH QUAN LY SACH ---");
            System.out.println("1. Them 1 cuon sach");
            System.out.println("2. Xoa 1 cuon sach");
            System.out.println("3. Thay doi cuon sach");
            System.out.println("4. Xuat thong tin tat ca");
            System.out.println("5. Tim sach co tua de chua 'Lap trinh'");
            System.out.println("6. Lay toi da K cuon sach co gia <= P");
            System.out.println("7. Tim sach theo danh sach tac gia");
            System.out.println("0. Thoat");
            System.out.print("Chon chuc nang: ");
            chon = Integer.parseInt(sc.nextLine());

            switch (chon) {
                case 1 -> { // Them sach
                    Book b = new Book();
                    b.input();
                    listBook.add(b);
                }
                case 2 -> { // Xoa sach theo ID
                    System.out.print("Nhap ma sach can xoa: ");
                    int idXoa = Integer.parseInt(sc.nextLine());
                    listBook.removeIf(p -> p.getId() == idXoa);
                    System.out.println("Da cap nhat danh sach.");
                }
                case 4 -> { // Xuat tat ca dung Lambda & Method Reference
                    System.out.println("Danh sach sach hien tai:");
                    listBook.forEach(Book::output);
                }
                case 5 -> { // Tim sach chua chu "Lap trinh"
                    System.out.println("Ket qua tim kiem:");
                    listBook.stream()
                            .filter(b -> b.getTitle().toLowerCase().contains("lap trinh"))
                            .forEach(Book::output);
                }
                case 6 -> { // Lay K cuon gia <= P
                    System.out.print("Nhap so luong K: ");
                    int K = Integer.parseInt(sc.nextLine());
                    System.out.print("Nhap gia P mong muon: ");
                    double P = Double.parseDouble(sc.nextLine());

                    listBook.stream()
                            .filter(b -> b.getPrice() <= P)
                            .limit(K)
                            .forEach(Book::output);
                }
                case 7 -> { // Tim theo danh sach tac gia
                    System.out.print("Nhap danh sach tac gia (cach nhau boi dau phay): ");
                    String input = sc.nextLine();
                    Set<String> setTacGia = Arrays.stream(input.split(","))
                            .map(String::trim)
                            .collect(Collectors.toSet());

                    listBook.stream()
                            .filter(b -> setTacGia.contains(b.getAuthor()))
                            .forEach(Book::output);
                }
                case 0 -> System.out.println("Tam biet!");
                default -> System.out.println("Lua chon khong hop le!");
            }
        } while (chon != 0);
    }
}
