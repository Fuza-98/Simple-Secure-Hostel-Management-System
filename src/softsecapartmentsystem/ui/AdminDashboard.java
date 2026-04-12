package softsecapartmentsystem.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame {
    JLabel titleLabel, welcomeLabel;
    JButton manageRoomsButton, viewApplicationsButton, logoutButton;
    JPanel panel;

    public AdminDashboard() {
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

        // Action Listeners
        manageRoomsButton.addActionListener(e -> {
            new ManageRooms();
            dispose();
        });

        logoutButton.addActionListener(e -> {
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
    }
}