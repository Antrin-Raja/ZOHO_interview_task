// TransactionFunctions.java


import java.sql.*;
import java.util.Scanner;

public class TransactionFunctions {

    public static void viewAllTransactions() {
        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Transactions");

            System.out.println("\n All Transactions");
            while (rs.next()) {
                System.out.printf("Transaction ID: %d | MemberID: %d | BookID: %d | Quantity: %d | Type: %s | Date: %s\n",
                        rs.getInt("TransactionID"), rs.getInt("MemberID"), rs.getInt("BookID"),
                        rs.getInt("Quantity"), rs.getString("Type"), rs.getTimestamp("Date").toString());
            }
        } catch (SQLException e) {
            System.out.println("Couldn't find Transactions: " + e.getMessage());
        }
    }

    public static void searchTransactionById(Scanner scanner) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter Transaction ID: ");
            int id = scanner.nextInt();

            String query = "SELECT * FROM Transactions WHERE TransactionID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.printf("Transaction ID: %d | MemberID: %d | BookID: %d | Quantity: %d | Type: %s | Date: %s\n",
                        rs.getInt("TransactionID"), rs.getInt("MemberID"), rs.getInt("BookID"),
                        rs.getInt("Quantity"), rs.getString("Type"), rs.getTimestamp("Date").toString());
            } else {
                System.out.println("Transaction not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error searching transaction: " + e.getMessage());
        }
    }

    public static void borrowBook(Scanner scanner, int memberId) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter Book ID to borrow: ");
            int bookId = scanner.nextInt();

            String checkQuery = "SELECT CopiesAvailable FROM Books WHERE BookID = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt("CopiesAvailable") > 0) {
                conn.setAutoCommit(false);

                String insert = "INSERT INTO Transactions (MemberID, BookID, Quantity, Type) VALUES (?, ?, 1, 'Borrow')";
                PreparedStatement ps = conn.prepareStatement(insert);
                ps.setInt(1, memberId);
                ps.setInt(2, bookId);
                ps.executeUpdate();

                String update = "UPDATE Books SET CopiesAvailable = CopiesAvailable - 1, TimesBorrowed = TimesBorrowed + 1 WHERE BookID = ?";
                PreparedStatement ps2 = conn.prepareStatement(update);
                ps2.setInt(1, bookId);
                ps2.executeUpdate();

                conn.commit();
                System.out.println("Book borrowed successfully.");
            } else {
                System.out.println("Book not available.");
            }
        } catch (SQLException e) {
            System.out.println("Error borrowing book: " + e.getMessage());
        }
    }

    public static void returnBook(Scanner scanner, int memberId) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter Book ID: ");
            int bookId = scanner.nextInt();

            String insert = "INSERT INTO Transactions (MemberID, BookID, Quantity, Type) VALUES (?, ?, 1, 'Return')";
            PreparedStatement ps = conn.prepareStatement(insert);
            ps.setInt(1, memberId);
            ps.setInt(2, bookId);
            ps.executeUpdate();

            String update = "UPDATE Books SET CopiesAvailable = CopiesAvailable + 1 WHERE BookID = ?";
            PreparedStatement ps2 = conn.prepareStatement(update);
            ps2.setInt(1, bookId);
            ps2.executeUpdate();

            System.out.println("Book returned successfully.");
        } catch (SQLException e) {
            System.out.println("Error returning book: " + e.getMessage());
        }
    }

    public static void viewMemberTransactions(int memberId) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM Transactions WHERE MemberID = ? ORDER BY Date DESC";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, memberId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\nYour Transactions ");
            while (rs.next()) {
                System.out.printf("Transaction ID: %d | BookID: %d | Quantity: %d | Type: %s | Date: %s\n",
                        rs.getInt("TransactionID"), rs.getInt("BookID"), rs.getInt("Quantity"),
                        rs.getString("Type"), rs.getTimestamp("Date").toString());
            }
        } catch (SQLException e) {
            System.out.println("Error fetching your transactions: " + e.getMessage());
        }
    }
}
