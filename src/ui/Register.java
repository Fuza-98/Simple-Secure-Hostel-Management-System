package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Register extends JFrame {

    JLabel titleLabel, registerLabel;
    JLabel studentIdLabel, usernameLabel, passwordLabel, confirmPasswordLabel, statusLabel;

    JTextField studentIdField, usernameField;
    JPasswordField passwordField, confirmPasswordField;

    JButton registerButton, clearButton, backButton;
    JPanel panel;

    public Register() {
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
        statusLabel.setBounds(80, 375, 380, 25);
        statusLabel.setForeground(Color.RED);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String studentId = studentIdField.getText().trim();
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (studentId.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    statusLabel.setText("Please fill in all fields.");
                    statusLabel.setForeground(Color.RED);
                } else if (!password.equals(confirmPassword)) {
                    statusLabel.setText("Passwords do not match.");
                    statusLabel.setForeground(Color.RED);
                } else {
                    JOptionPane.showMessageDialog(null,
                        "Registered successfully"
                        );
                }
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
}