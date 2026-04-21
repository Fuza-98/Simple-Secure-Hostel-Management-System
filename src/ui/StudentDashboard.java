package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;

import util.*;

public class StudentDashboard extends JFrame {

    JLabel titleLabel, welcomeLabel;
    JButton applyRoomButton, viewRoomStatusButton, personalInfoButton, logoutButton;
    JPanel panel;

    String studentId, studentName, studentGender;
    
    private static final Logger logger = Logger.getLogger(StudentDashboard.class.getName());
    

    public StudentDashboard() {
        
        try {
            // Create a FileHandler to write logs to a file
            FileHandler fileHandler = new FileHandler("appLogs.log", true);  // 'true' to append to the file
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String studentId = Session.getStudentId();
        String studentName = Session.getStudentName();
        String studentGender = Session.getStudentGender();
        
        // Check if the user is a student
        if (!Session.isStudent()) {
            JOptionPane.showMessageDialog(null, "Access Denied. Students only.");
            new Login();
            dispose();
            return;
        }
        

        setTitle("Student Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 248, 255));

        titleLabel = new JLabel("University Hostel Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(70, 30, 380, 30);

        welcomeLabel = new JLabel("Welcome, " + studentName);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        welcomeLabel.setBounds(150, 80, 250, 25);

        applyRoomButton = new JButton("Apply for Room");
        applyRoomButton.setBounds(150, 130, 200, 35);

        personalInfoButton = new JButton("View Personal Info");
        personalInfoButton.setBounds(150, 180, 200, 35);
        
        viewRoomStatusButton = new JButton("View Application Status");
        viewRoomStatusButton.setBounds(150, 230, 200, 35);

        logoutButton = new JButton("Logout");
        logoutButton.setBounds(150, 280, 200, 35);
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);
        
        
        //Buttons and event listeners
        applyRoomButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ApplyRoom();
                dispose();
            }
        });
        
         personalInfoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new studentInfo();
                dispose();
            }
        }); 
         
         viewRoomStatusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ViewApplicationStatus();
                dispose();
            }
        }); 

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.info("User " + studentName + " (" + studentId + ") logged out");
                Session.clearSession();
                SessionTimeout.stop();
                new Login();
                dispose();
            }
        });

        panel.add(titleLabel);
        panel.add(welcomeLabel);
        panel.add(applyRoomButton);
        panel.add(personalInfoButton);
        panel.add(viewRoomStatusButton);
        panel.add(logoutButton);

        add(panel);
        setVisible(true);
        
        SessionTimeout.start(this);
    }
}