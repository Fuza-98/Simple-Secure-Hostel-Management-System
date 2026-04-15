

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.mindrot.jbcrypt.BCrypt;
import db.DBConnection;

public class CreateAdmin {
    public static void main(String[] args) {
        String studentId = "ADMIN001";
        String username = "admin";
        String plainPassword = "admin123";
        String role = "admin";

        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

        String sql = "INSERT INTO users (studentID, username, password, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);
            ps.setString(2, username);
            ps.setString(3, hashedPassword);
            ps.setString(4, role);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Admin account created successfully.");
                System.out.println("Hashed password: " + hashedPassword);
            } else {
                System.out.println("Failed to create admin account.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}