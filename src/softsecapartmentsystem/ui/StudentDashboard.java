/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package softsecapartmentsystem.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Fauzan
 */
public class StudentDashboard extends JFrame{
    JLabel titleLabel, welcomeLabel;
    JButton applyRoomButton, searchRoomButton, biodataButton, logoutButton;
    JPanel panel;
    
    public StudentDashboard(){
        setTitle("Student Dashboard");
        setSize(500,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 248, 255));
        
        titleLabel = new JLabel("University Hostel Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(70, 30, 380, 30);
        
        welcomeLabel = new JLabel("Welcome, Student");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        welcomeLabel.setBounds(170, 80, 200, 25);
        
        applyRoomButton = new JButton("Apply for Room");
        applyRoomButton.setBounds(150, 130, 200, 35);
        
        searchRoomButton = new JButton("Search Available Rooms");
        searchRoomButton.setBounds(150, 180, 200, 35);
        
        biodataButton = new JButton("View Biodata");
        biodataButton.setBounds(150, 230, 200, 35);
        
        logoutButton = new JButton("Logout");
        logoutButton.setBounds(150, 280, 200, 35);
        
        panel.add(titleLabel);
        panel.add(welcomeLabel);
        panel.add(applyRoomButton);
        panel.add(searchRoomButton);
        panel.add(biodataButton);
        panel.add(logoutButton);

        add(panel);

        setVisible(true);
        
        applyRoomButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ApplyRoom();
                dispose();
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                new Login();
                dispose();
            }
        });

    }
}
