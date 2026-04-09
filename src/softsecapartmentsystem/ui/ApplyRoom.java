/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package softsecapartmentsystem.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ApplyRoom extends JFrame {

    JLabel titleLabel;
    JLabel studentIdLabel, nameLabel, genderLabel;
    JLabel studentIdValue, nameValue, genderValue;
    JLabel roomTypeLabel, specialRequestLabel;

    JComboBox<String> roomTypeCombo;
    JTextArea specialRequestArea;
    JButton applyButton, clearButton, backButton;

    JPanel panel;
    JScrollPane scrollPane;

    public ApplyRoom() {
        setTitle("Apply for Room");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 248, 255));

        titleLabel = new JLabel("Apply for Hostel Room");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(140, 20, 250, 30);

        studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setBounds(60, 80, 100, 25);

        studentIdValue = new JLabel("20241234");
        studentIdValue.setBounds(180, 80, 200, 25);

        nameLabel = new JLabel("Name:");
        nameLabel.setBounds(60, 115, 100, 25);

        nameValue = new JLabel("Ali bin Ahmad");
        nameValue.setBounds(180, 115, 200, 25);

        genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(60, 150, 100, 25);

        genderValue = new JLabel("Male");
        genderValue.setBounds(180, 150, 200, 25);

        roomTypeLabel = new JLabel("Room Type:");
        roomTypeLabel.setBounds(60, 190, 100, 25);

        String[] roomTypes = {"Single", "Double", "Triple"};
        roomTypeCombo = new JComboBox<>(roomTypes);
        roomTypeCombo.setBounds(180, 190, 180, 25);

        specialRequestLabel = new JLabel("Special Request:");
        specialRequestLabel.setBounds(60, 230, 100, 25);

        specialRequestArea = new JTextArea();
        scrollPane = new JScrollPane(specialRequestArea);
        scrollPane.setBounds(180, 230, 200, 80);

        applyButton = new JButton("Apply");
        applyButton.setBounds(90, 340, 90, 30);

        clearButton = new JButton("Clear");
        clearButton.setBounds(200, 340, 90, 30);

        backButton = new JButton("Back");
        backButton.setBounds(310, 340, 90, 30);

        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String roomType = roomTypeCombo.getSelectedItem().toString();
                String specialRequest = specialRequestArea.getText();

                JOptionPane.showMessageDialog(null,
                        "Application Submitted\nRoom Type: " + roomType +
                        "\nSpecial Request: " + specialRequest);
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                roomTypeCombo.setSelectedIndex(0);
                specialRequestArea.setText("");
            }
        });

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
        panel.add(roomTypeLabel);
        panel.add(roomTypeCombo);
        panel.add(specialRequestLabel);
        panel.add(scrollPane);
        panel.add(applyButton);
        panel.add(clearButton);
        panel.add(backButton);

        add(panel);
        setVisible(true);
    }
}
