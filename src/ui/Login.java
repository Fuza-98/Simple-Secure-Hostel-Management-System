/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

//DB imports

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import db.DBConnection;

//Hashing
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Acer
 */
public class Login extends JFrame{
    JLabel titleLabel, loginLabel, userLabel, passLabel, statusLabel;
    JTextField userField;
    JPasswordField passField;
    JButton loginButton, clearButton, registerButton;
    JPanel panel;
    
    public Login(){
        setTitle("HOSTEL MANAGEMENT SYSTEM");
        setSize(500,380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
     
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 248, 255));
        
        titleLabel = new JLabel("University Hostel Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(70, 30, 380, 30);
        
        loginLabel = new JLabel("User Login");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 16));
        loginLabel.setBounds(190, 75, 120, 25);
        
        userLabel = new JLabel("Username:");
        userLabel.setBounds(100, 120, 100, 25);
        
        userField = new JTextField();
        userField.setBounds(180, 120, 180, 25);
        
        passLabel = new JLabel("Password:");
        passLabel.setBounds(100, 160, 100, 25);

        passField = new JPasswordField();
        passField.setBounds(180, 160, 180, 25);

        loginButton = new JButton("Login");
        loginButton.setBounds(140, 210, 90, 30);

        clearButton = new JButton("Clear");
        clearButton.setBounds(250, 210, 90, 30);

        registerButton = new JButton("Register");
        registerButton.setBounds(190, 250, 100, 30);

        statusLabel = new JLabel("Please enter your login details");
        statusLabel.setBounds(160, 290, 260, 25);
        
        //Event listeners
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLogin();   
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userField.setText("");
                passField.setText("");
                statusLabel.setText("Fields cleared");
                statusLabel.setForeground(Color.BLUE);
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Register();
                dispose();
            }
        });
        
        panel.add(titleLabel);
        panel.add(loginLabel);
        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(loginButton);
        panel.add(clearButton);
        panel.add(registerButton);
        panel.add(statusLabel);
        add(panel);
        
        setVisible(true);   
    }
    
    private void handleLogin() {
    String username = userField.getText().trim();
    String password = new String(passField.getPassword());

    if (username.isEmpty() || password.isEmpty()) {
        statusLabel.setText("Username and password cannot be empty");
        statusLabel.setForeground(Color.RED);
        return;
    }

     String sql = "SELECT u.studentID, s.full_name, s.gender, u.password, u.role "
                    + "FROM users u "
                    + "LEFT JOIN students s ON u.studentID = s.studentID "
                    + "WHERE u.username = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, username);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String storedHash = rs.getString("password");

                if (BCrypt.checkpw(password, storedHash)) {
                    String role = rs.getString("role");

                    if ("student".equals(role)) {
                        String studentId = rs.getString("studentID");
                        String studentName = rs.getString("full_name");
                        String studentGender = rs.getString("gender");

                        new StudentDashboard(studentId, studentName, studentGender);
                        dispose();
                    } else if ("admin".equals(role)) {
                        new AdminDashboard(role);
                        dispose();
                    }
                } else {
                    statusLabel.setText("Invalid username or password");
                    statusLabel.setForeground(Color.RED);
                }
            } else {
                statusLabel.setText("Invalid username or password");
                statusLabel.setForeground(Color.RED);
            }
        }

    } catch (Exception e) {
        statusLabel.setText("Database error occurred");
        statusLabel.setForeground(Color.RED);
        e.printStackTrace();
    }
}
          
}
