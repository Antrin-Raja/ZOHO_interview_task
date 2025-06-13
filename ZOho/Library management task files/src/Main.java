

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n Library Management System ");
            System.out.println("1. Login as Librarian");
            System.out.println("2. Login as Member");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  

            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    String libUser = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String libPass = scanner.nextLine();

                    if (libUser.equals("admin") && libPass.equals("admin")) {
                        librarianMenu(scanner);
                    } else {
                        System.out.println("Invalid librarian credentials.");
                    }
                    break;

                case 2:
                    System.out.print("Enter Member ID: ");
                    int memberId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String memberPass = scanner.nextLine();

                    if (authenticateMember(memberId, memberPass)) {
                        memberMenu(scanner, memberId);
                    } else {
                        System.out.println("Invalid member credentials.");
                    }
                    break;

                case 3:
                    exit = true;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        System.out.println("Already exists");
        scanner.close();
    }

   
    public static boolean authenticateMember(int memberId, String password) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM LibraryMembers WHERE MemberID = ? AND Password = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, memberId);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking member credentials: " + e.getMessage());
            return false;
        }
    }

    
    public static void librarianMenu(Scanner scanner) {
        boolean back = false;

        while (!back) {
            System.out.println("\nLibrarian Menu ");
            System.out.println("1. View Members");
            System.out.println("2. Add Member");
            System.out.println("3. Remove Member");
            System.out.println("4. View Books");
            System.out.println("5. Add Book");
            System.out.println("6. Remove Book");
            System.out.println("7. Update Book Stock");
            System.out.println("8. View All Transactions");
            System.out.println("9. Search Transaction by ID");
            System.out.println("10. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1: MemberFunctions.viewMembers(); break;
                case 2: MemberFunctions.addMember(scanner); break;
                case 3: MemberFunctions.removeMember(scanner); break;
                case 4: BookFunctions.viewBooks(); break;
                case 5: BookFunctions.addBook(scanner); break;
                case 6: BookFunctions.removeBook(scanner); break;
                case 7: BookFunctions.updateBookStock(scanner); break;
                case 8: TransactionFunctions.viewAllTransactions(); break;
                case 9: TransactionFunctions.searchTransactionById(scanner); break;
                case 10: back = true; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

   
    public static void memberMenu(Scanner scanner, int memberId) {
        boolean back = false;

        while (!back) {
            System.out.println("\n Member Menu ");
            System.out.println("1. Borrow Book");
            System.out.println("2. Return Book");
            System.out.println("3. View My Transactions");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1: TransactionFunctions.borrowBook(scanner, memberId); break;
                case 2: TransactionFunctions.returnBook(scanner, memberId); break;
                case 3: TransactionFunctions.viewMemberTransactions(memberId); break;
                case 4: back = true; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }
}
