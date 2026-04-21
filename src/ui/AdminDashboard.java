package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import util.*;
import java.util.logging.*;

public class AdminDashboard extends JFrame {
    
    private String adminId;
    
    JLabel titleLabel, welcomeLabel;
    JButton manageRoomsButton, viewApplicationsButton, logoutButton;
    JPanel panel;

    private static final Logger logger = Logger.getLogger(AdminDashboard.class.getName());
    public AdminDashboard() {  
        
        String adminId = Session.getStudentId();
        
        try {
            // Create a FileHandler to write logs to a file
            FileHandler fileHandler = new FileHandler("appLogs.log", true);  // 'true' to append to the file
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Check if the user is an admin
        if (!Session.isAdmin()) {
            JOptionPane.showMessageDialog(null, "Access Denied. Admins only.");
            new Login();
            dispose();
            return;
        }
        
        setTitle("Admin Dashboard - Hostel System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(230, 230, 250)); // Lavender background

        titleLabel = new JLabel("Admin Control Panel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBounds(140, 30, 300, 30);

        welcomeLabel = new JLabel("Logged in as: Administrator");
        welcomeLabel.setBounds(160, 70, 200, 25);

        manageRoomsButton = new JButton("Manage Rooms (CRUD)");
        manageRoomsButton.setBounds(140, 120, 220, 40);

        viewApplicationsButton = new JButton("View Student Applications");
        viewApplicationsButton.setBounds(140, 180, 220, 40);

        logoutButton = new JButton("Logout");
        logoutButton.setBounds(140, 240, 220, 40);
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);

        // Action Listeners
        manageRoomsButton.addActionListener(e -> {
            new ManageRooms();
            dispose();
        });
        
        viewApplicationsButton.addActionListener(e -> {
            new ViewApplications();
            dispose();
        });

        logoutButton.addActionListener(e -> {
            Session.clearSession();
            SessionTimeout.stop();
            logger.info("Logged out for admin: " + adminId);
            new Login();
            dispose();
        });

        panel.add(titleLabel);
        panel.add(welcomeLabel);
        panel.add(manageRoomsButton);
        panel.add(viewApplicationsButton);
        panel.add(logoutButton);

        add(panel);
        setVisible(true);
        
        SessionTimeout.start(this);
    }
}