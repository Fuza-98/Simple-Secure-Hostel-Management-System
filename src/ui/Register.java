package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;
import java.io.*;

//DB imports

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import db.DBConnection;

//Hashing
import org.mindrot.jbcrypt.BCrypt;

public class Register extends JFrame {

    JLabel titleLabel, registerLabel;
    JLabel studentIdLabel, usernameLabel, passwordLabel, confirmPasswordLabel, statusLabel;

    JTextField studentIdField, usernameField;
    JPasswordField passwordField, confirmPasswordField;

    JButton registerButton, clearButton, backButton;
    JPanel panel;
    
    private static final Logger logger = Logger.getLogger(Register.class.getName());

    public Register() {
            try {
                FileHandler fileHandler = new FileHandler("appLogs.log", true);  // 'true' to append to the log file
                SimpleFormatter formatter = new SimpleFormatter();
                fileHandler.setFormatter(formatter);
                logger.addHandler(fileHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        
        setTitle("Student Registration");
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 248, 255));

        titleLabel = new JLabel("University Hostel Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(85, 25, 380, 30);

        registerLabel = new JLabel("Student Registration");
        registerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        registerLabel.setBounds(185, 65, 180, 25);

        studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setBounds(80, 130, 120, 25);

        studentIdField = new JTextField();
        studentIdField.setBounds(220, 130, 180, 25);

        usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(80, 175, 120, 25);

        usernameField = new JTextField();
        usernameField.setBounds(220, 175, 180, 25);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(80, 220, 120, 25);

        passwordField = new JPasswordField();
        passwordField.setBounds(220, 220, 180, 25);

        confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setBounds(80, 265, 120, 25);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(220, 265, 180, 25);

        registerButton = new JButton("Register");
        registerButton.setBounds(90, 330, 100, 30);

        clearButton = new JButton("Clear");
        clearButton.setBounds(210, 330, 100, 30);

        backButton = new JButton("Back to Login");
        backButton.setBounds(330, 330, 120, 30);

        statusLabel = new JLabel("");
        statusLabel.setBounds(60, 375, 500, 50);
        statusLabel.setForeground(Color.RED);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleRegistration();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                studentIdField.setText("");
                usernameField.setText("");
                passwordField.setText("");
                confirmPasswordField.setText("");
                statusLabel.setText("");
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Login();
                dispose();
            }
        });

        panel.add(titleLabel);
        panel.add(registerLabel);
        panel.add(studentIdLabel);
        panel.add(studentIdField);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(confirmPasswordLabel);
        panel.add(confirmPasswordField);
        panel.add(registerButton);
        panel.add(clearButton);
        panel.add(backButton);
        panel.add(statusLabel);

        add(panel);
        setVisible(true);
    }
    
    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");
    }
    
    private void handleRegistration() {
        String studentId = studentIdField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (studentId.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setText("Please fill in all fields.");
            statusLabel.setForeground(Color.RED);
            return;
        }

        if (username.length() < 3 || username.length() > 100) {
            statusLabel.setText("Name must be between 3 and 100 characters.");
            statusLabel.setForeground(Color.RED);
            return;
        }

        if (!username.matches("[A-Za-z @.'-]+")) {
            statusLabel.setText("Name contains invalid characters.");
            statusLabel.setForeground(Color.RED);
            return;
        }

        if (!isValidPassword(password)) {
            statusLabel.setText("<html>Password must be at least 8 characters with uppercase, lowercase, and a number.</html>");
            statusLabel.setForeground(Color.RED);
            return;
        }

        if (!password.equals(confirmPassword)) {
            statusLabel.setText("Passwords do not match.");
            statusLabel.setForeground(Color.RED);
            return;
        }

        String checkStudentSql = "SELECT studentID FROM students WHERE studentID = ?";
        String checkUserSql = "SELECT username FROM users WHERE username = ? OR studentID = ?";
        String insertUserSql = "INSERT INTO users (studentID, username, password, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {

            // Log that the check for student exists is happening
            logger.info("Checking if student exists with ID: " + studentId);

            // Check if student exists
            try (PreparedStatement ps1 = conn.prepareStatement(checkStudentSql)) {
                ps1.setString(1, studentId);
                ResultSet rs1 = ps1.executeQuery();

                if (!rs1.next()) {
                    statusLabel.setText("Student ID not found in student records.");
                    statusLabel.setForeground(Color.RED);
                    logger.warning("Student ID not found for registration attempt: " + studentId);
                    return;
                }
            }

            // Log that the check for existing user or student ID is happening
            logger.info("Checking if username or student ID already exists for: " + username);

            // Check if username or student already registered
            try (PreparedStatement ps2 = conn.prepareStatement(checkUserSql)) {
                ps2.setString(1, username);
                ps2.setString(2, studentId);
                ResultSet rs2 = ps2.executeQuery();

                if (rs2.next()) {
                    statusLabel.setText("Username already taken or student already registered.");
                    statusLabel.setForeground(Color.RED);
                    logger.warning("Username already taken or student already registered: " + username);
                    return;
                }
            }

            // Log password hashing process
            logger.info("Hashing password for username: " + username);
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt()); // For hashing

            // Log user insertion process
            logger.info("Inserting new user: " + username + " with student ID: " + studentId);

            // Insert new user account
            try (PreparedStatement ps3 = conn.prepareStatement(insertUserSql)) {
                ps3.setString(1, studentId);
                ps3.setString(2, username);
                ps3.setString(3, hashedPassword); // Replaced with hashed password
                ps3.setString(4, "student");

                int rowsInserted = ps3.executeUpdate();

                if (rowsInserted > 0) {
                    statusLabel.setText("Registration successful. You may now log in.");
                    statusLabel.setForeground(new Color(0, 128, 0));

                    // Log registration success
                    logger.info("Successfully registered user: " + username + " with student ID: " + studentId);

                    studentIdField.setText("");
                    usernameField.setText("");
                    passwordField.setText("");
                    confirmPasswordField.setText("");
                } else {
                    statusLabel.setText("Registration failed.");
                    statusLabel.setForeground(Color.RED);
                    logger.warning("Failed to register user: " + username);
                }
            }

        } catch (Exception e) {
            statusLabel.setText("Database error occurred.");
            statusLabel.setForeground(Color.RED);
            e.printStackTrace();
            logger.severe("Database error during registration: " + e.getMessage());
        }
    }
}