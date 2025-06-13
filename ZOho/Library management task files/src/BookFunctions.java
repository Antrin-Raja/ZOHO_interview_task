

import java.sql.*;
import java.util.Scanner;

public class BookFunctions {
    public static void viewBooks() {
        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Books");

            System.out.println("\n Books in Library ");
            while (rs.next()) {
                System.out.printf("ID: %d | Title: %s | Author: %s | Copies Available: %d\n",
                        rs.getInt("BookID"), rs.getString("Title"), rs.getString("Author"), rs.getInt("CopiesAvailable"));
            }
        } catch (SQLException e) {
            System.out.println("Error viewing books: " + e.getMessage());
        }
    }

    public static void addBook(Scanner scanner) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter Title: ");
            scanner.nextLine();
            String title = scanner.nextLine();
            System.out.print("Enter Author: ");
            String author = scanner.nextLine();
            System.out.print("Enter Genre: ");
            String genre = scanner.nextLine();
            System.out.print("Enter Total Copies: ");
            int total = scanner.nextInt();

            String query = "INSERT INTO Books (Title, Author, Genre, TotalCopies, CopiesAvailable) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setString(3, genre);
            ps.setInt(4, total);
            ps.setInt(5, total);

            ps.executeUpdate();
            System.out.println("Book added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding book: " + e.getMessage());
        }
    }

    public static void removeBook(Scanner scanner) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter Book ID to remove: ");
            int id = scanner.nextInt();

            String query = "DELETE FROM Books WHERE BookID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("Book removed.");
            else System.out.println("Book not found.");
        } catch (SQLException e) {
            System.out.println("Error removing book: " + e.getMessage());
        }
    }

    public static void updateBookStock(Scanner scanner) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter Book ID: ");
            int id = scanner.nextInt();
            System.out.print("Enter number of copies to add: ");
            int add = scanner.nextInt();

            String query = "UPDATE Books SET CopiesAvailable = CopiesAvailable + ?, TotalCopies = TotalCopies + ? WHERE BookID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, add);
            ps.setInt(2, add);
            ps.setInt(3, id);

            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("Stock updated.");
            else System.out.println("Book not found.");
        } catch (SQLException e) {
            System.out.println("Error updating stock: " + e.getMessage());
        }
    }
}
