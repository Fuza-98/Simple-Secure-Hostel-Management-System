/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package softsecapartmentsystem.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StudentInfo extends JFrame {

    JLabel titleLabel;
    JLabel studentIdLabel, studentIdValue;
    JLabel nameLabel, nameValue;
    JLabel genderLabel, genderValue;
    JLabel phoneLabel, phoneValue;
    JLabel emailLabel, emailValue;
    JLabel addressLabel, addressValue;

    JButton backButton;
    JPanel panel;

    String studentId;
    String studentName;
    String studentGender;

    public StudentInfo(String studentId, String studentName, String studentGender) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentGender = studentGender;

        setTitle("Personal Information");
        setSize(550, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 248, 255));

        titleLabel = new JLabel("Student Personal Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(120, 20, 320, 30);

        studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setBounds(70, 90, 120, 25);
        studentIdValue = new JLabel(this.studentId);
        studentIdValue.setBounds(220, 90, 200, 25);

        nameLabel = new JLabel("Name:");
        nameLabel.setBounds(70, 125, 120, 25);
        nameValue = new JLabel(this.studentName);
        nameValue.setBounds(220, 125, 200, 25);

        genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(70, 160, 120, 25);
        genderValue = new JLabel(this.studentGender);
        genderValue.setBounds(220, 160, 200, 25);

        phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setBounds(70, 195, 120, 25);
        phoneValue = new JLabel("012-3456789");
        phoneValue.setBounds(220, 195, 200, 25);

        emailLabel = new JLabel("Email:");
        emailLabel.setBounds(70, 230, 120, 25);
        emailValue = new JLabel("student@email.com");
        emailValue.setBounds(220, 230, 200, 25);

        addressLabel = new JLabel("Address:");
        addressLabel.setBounds(70, 265, 120, 25);
        addressValue = new JLabel("Kajang, Selangor");
        addressValue.setBounds(220, 265, 200, 25);

        backButton = new JButton("Back");
        backButton.setBounds(210, 320, 100, 30);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new StudentDashboard(StudentInfo.this.studentId, StudentInfo.this.studentName, StudentInfo.this.studentGender);
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
        panel.add(phoneLabel);
        panel.add(phoneValue);
        panel.add(emailLabel);
        panel.add(emailValue);
        panel.add(addressLabel);
        panel.add(addressValue);
        panel.add(backButton);

        add(panel);
        setVisible(true);
    }
}
