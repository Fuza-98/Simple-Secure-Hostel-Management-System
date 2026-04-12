package softsecapartmentsystem.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StudentDashboard extends JFrame {

    JLabel titleLabel, welcomeLabel;
    JButton applyRoomButton, searchRoomButton, personalInfoButton, logoutButton;
    JPanel panel;

    String studentId;
    String studentName;
    String studentGender;

    public StudentDashboard(String studentId, String studentName, String studentGender) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentGender = studentGender;

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

        welcomeLabel = new JLabel("Welcome, " + this.studentName);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        welcomeLabel.setBounds(150, 80, 250, 25);

        applyRoomButton = new JButton("Apply for Room");
        applyRoomButton.setBounds(150, 130, 200, 35);

        searchRoomButton = new JButton("Search Available Rooms");
        searchRoomButton.setBounds(150, 180, 200, 35);

        personalInfoButton = new JButton("View Personal Info");
        personalInfoButton.setBounds(150, 230, 200, 35);

        logoutButton = new JButton("Logout");
        logoutButton.setBounds(150, 280, 200, 35);
        
        
        //Buttons and event listeners
        applyRoomButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ApplyRoom(StudentDashboard.this.studentId, StudentDashboard.this.studentName, StudentDashboard.this.studentGender);
                dispose();
            }
        });
        
        searchRoomButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new searchRoom(StudentDashboard.this.studentId, StudentDashboard.this.studentName, StudentDashboard.this.studentGender);
                dispose();
            }
        });
        
       /*  personalInfoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new studentInfo(StudentDashboard.this.studentId, StudentDashboard.this.studentName, StudentDashboard.this.studentGender);
                dispose();
            }
        }); */

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Login();
                dispose();
            }
        });

        panel.add(titleLabel);
        panel.add(welcomeLabel);
        panel.add(applyRoomButton);
        panel.add(searchRoomButton);
        panel.add(personalInfoButton);
        panel.add(logoutButton);

        add(panel);
        setVisible(true);
    }
}