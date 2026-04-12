/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package softsecapartmentsystem.ui;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 *
 * @author Acer
 */
public class Login extends JFrame{
    JLabel titleLabel, loginLabel, userLabel, passLabel, statusLabel;
    JTextField userField;
    JPasswordField passField;
    JButton loginButton, clearButton;
    JPanel panel;
    
    public Login(){
        setTitle("HOSTEL MANAGEMENT SYSTEM");
        setSize(500,350);
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

        statusLabel = new JLabel("Please enter your login details");
        statusLabel.setBounds(140, 260, 250, 25);
        statusLabel.setForeground(Color.DARK_GRAY);
        
        //Event listeners
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    statusLabel.setText("Username and password cannot be empty");
                    statusLabel.setForeground(Color.RED);
                } else if (username.equals("student") && password.equals("student123")) {
                    String studentId = "20241234";
                    String studentName = "Ali bin Ahmad";
                    String studentGender = "Male";

                    new StudentDashboard(studentId, studentName, studentGender);
                    dispose();
                
                } else {
                    statusLabel.setText("Login button clicked");
                    statusLabel.setForeground(new Color(0, 128, 0));
                }
                
                
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
        
        panel.add(titleLabel);
        panel.add(loginLabel);
        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(loginButton);
        panel.add(clearButton);
        panel.add(statusLabel);
        add(panel);
        
        setVisible(true);   
    }
          
}
