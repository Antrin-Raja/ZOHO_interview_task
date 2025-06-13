

import java.sql.*;
import java.util.Scanner;

public class AddMemberTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(" Add New Library Member ");
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Username: ");
        String username = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO LibraryMembers (Name, Username, Password) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, username);
            ps.setString(3, password);

            ps.executeUpdate();
            System.out.println(" Member added successfully ");
        } catch (SQLException e) {
            System.out.println("Error adding member: " + e.getMessage());
        }

        scanner.close();
    }
}


