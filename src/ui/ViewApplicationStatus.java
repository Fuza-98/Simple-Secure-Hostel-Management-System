/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;
//ui
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//DB connection
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import db.DBConnection;

import util.*;

public class ViewApplicationStatus extends JFrame {

    JLabel titleLabel, studentIdLabel, nameLabel, genderLabel;
    JLabel studentIdValue, nameValue, genderValue;

    JLabel applicationIdLabel, roomNumberLabel, roomTypeLabel, specialRequestLabel, statusLabel, dateLabel;
    JLabel applicationIdValue, roomNumberValue, roomTypeValue, specialRequestValue, statusValue, dateValue;

    JButton backButton;
    JPanel panel;

    String studentId;
    String studentName;
    String studentGender;

    public ViewApplicationStatus() {
        
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
        

        setTitle("View Application Status");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 248, 255));

        titleLabel = new JLabel("Current Room Application Status");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(120, 20, 350, 30);

        studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setBounds(60, 80, 100, 25);
        studentIdValue = new JLabel(studentId);
        studentIdValue.setBounds(180, 80, 250, 25);

        nameLabel = new JLabel("Name:");
        nameLabel.setBounds(60, 110, 100, 25);
        nameValue = new JLabel(studentName);
        nameValue.setBounds(180, 110, 250, 25);

        genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(60, 140, 100, 25);
        genderValue = new JLabel(studentGender);
        genderValue.setBounds(180, 140, 250, 25);

        applicationIdLabel = new JLabel("Application ID:");
        applicationIdLabel.setBounds(60, 200, 120, 25);
        applicationIdValue = new JLabel("-");
        applicationIdValue.setBounds(180, 200, 250, 25);

        roomNumberLabel = new JLabel("Room Number:");
        roomNumberLabel.setBounds(60, 230, 120, 25);
        roomNumberValue = new JLabel("-");
        roomNumberValue.setBounds(180, 230, 250, 25);

        roomTypeLabel = new JLabel("Room Type:");
        roomTypeLabel.setBounds(60, 260, 120, 25);
        roomTypeValue = new JLabel("-");
        roomTypeValue.setBounds(180, 260, 250, 25);

        specialRequestLabel = new JLabel("Special Request:");
        specialRequestLabel.setBounds(60, 290, 120, 25);
        specialRequestValue = new JLabel("-");
        specialRequestValue.setBounds(180, 290, 350, 25);

        statusLabel = new JLabel("Status:");
        statusLabel.setBounds(60, 320, 120, 25);
        statusValue = new JLabel("-");
        statusValue.setBounds(180, 320, 250, 25);

        dateLabel = new JLabel("Date Applied:");
        dateLabel.setBounds(60, 350, 120, 25);
        dateValue = new JLabel("-");
        dateValue.setBounds(180, 350, 250, 25);

        backButton = new JButton("Back");
        backButton.setBounds(240, 400, 100, 30);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new StudentDashboard();
                dispose();
            }
        });

        panel.add(titleLabel);
        panel.add(studentIdLabel);
        panel.add(studentIdValue);
        panel.add(nameLabel);
        panel.add(nameValue);
        panel.add(genderLabel);
        panel.add(genderValue);
        panel.add(applicationIdLabel);
        panel.add(applicationIdValue);
        panel.add(roomNumberLabel);
        panel.add(roomNumberValue);
        panel.add(roomTypeLabel);
        panel.add(roomTypeValue);
        panel.add(specialRequestLabel);
        panel.add(specialRequestValue);
        panel.add(statusLabel);
        panel.add(statusValue);
        panel.add(dateLabel);
        panel.add(dateValue);
        panel.add(backButton);

        add(panel);

        loadApplicationStatus();

        setVisible(true);
        
        SessionTimeout.start(this);
    }

    private void loadApplicationStatus() {
        String sql = "SELECT a.applicationID, r.roomNum, r.roomType, a.specialRequest, a.status, a.date "
                   + "FROM applications a "
                   + "JOIN rooms r ON a.roomID = r.roomID "
                   + "WHERE a.studentID = ? "
                   + "ORDER BY a.date DESC "
                   + "LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, Session.getStudentId());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    applicationIdValue.setText(rs.getString("applicationID"));
                    roomNumberValue.setText(rs.getString("roomNum"));
                    roomTypeValue.setText(rs.getString("roomType"));

                    String request = rs.getString("specialRequest");
                    specialRequestValue.setText(request != null ? request : "-");

                    String status = rs.getString("status");
                    statusValue.setText(status);

                    if ("Approved".equalsIgnoreCase(status)) {
                        statusValue.setForeground(new Color(0, 128, 0)); // Green
                    } else if ("Rejected".equalsIgnoreCase(status)) {
                        statusValue.setForeground(Color.RED); // Red
                    } else if ("Pending".equalsIgnoreCase(status)) {
                        statusValue.setForeground(new Color(255, 140, 0)); // Orange
                    } else {
                        statusValue.setForeground(Color.BLACK);
                    }

                    dateValue.setText(rs.getString("date"));
                } else {
                    applicationIdValue.setText("No application found");
                    roomNumberValue.setText("-");
                    roomTypeValue.setText("-");
                    specialRequestValue.setText("-");
                    statusValue.setText("-");
                    statusValue.setForeground(Color.BLACK);
                    dateValue.setText("-");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load application status.");
            e.printStackTrace();
        }
    }
}
