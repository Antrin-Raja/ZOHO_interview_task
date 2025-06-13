// MemberFunctions.java


import java.sql.*;
import java.util.Scanner;

public class MemberFunctions {

    public static void viewMembers() {
        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM LibraryMembers");

            System.out.println("\n--- Library Members ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Username: %s\n",
                        rs.getInt("MemberID"), rs.getString("Name"), rs.getString("Username"));
            }
        } catch (SQLException e) {
            System.out.println("Error viewing members: " + e.getMessage());
        }
    }

    public static void addMember(Scanner scanner) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter Name: ");
            scanner.nextLine();  // flush
            String name = scanner.nextLine();

            System.out.print("Enter Username: ");
            String username = scanner.nextLine();

            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            String query = "INSERT INTO LibraryMembers (Name, Username, Password) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, username);
            ps.setString(3, password);

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int memberId = keys.getInt(1);
                System.out.println("✅ Member added successfully!");
                System.out.println("📌 Your Member ID is: " + memberId);
            } else {
                System.out.println("⚠️ Member added but ID could not be retrieved.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error adding member: " + e.getMessage());
        }
    }


    public static void removeMember(Scanner scanner) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter Member ID to remove: ");
            int id = scanner.nextInt();

            String query = "DELETE FROM LibraryMembers WHERE MemberID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("Member removed.");
            else System.out.println("Member not found.");
        } catch (SQLException e) {
            System.out.println("Error removing member: " + e.getMessage());
        }
    }
}
