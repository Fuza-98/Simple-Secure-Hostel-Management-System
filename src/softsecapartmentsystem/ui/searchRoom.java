/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package softsecapartmentsystem.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
/**
 *
 * @author Acer
 */
public class searchRoom extends JFrame{
    JLabel titleLabel, welcomeLabel;
    JTable roomTable;
    JScrollPane scrollPane;
    JButton backButton;
    JPanel panel;

    String studentId;
    String studentName;
    String studentGender;

    public searchRoom(String studentId, String studentName, String studentGender) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentGender = studentGender;

        setTitle("Search Available Rooms");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 248, 255));

        titleLabel = new JLabel("Available Rooms and Capacity");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(190, 20, 320, 30);

        welcomeLabel = new JLabel("Welcome, " + this.studentName);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        welcomeLabel.setBounds(30, 60, 250, 25);

        String[] columnNames = {"Room Number", "Room Type", "Capacity", "Occupied", "Available"};
        Object[][] roomData = {
            {"A101", "Single", 1, 0, 1},
            {"A102", "Double", 2, 1, 1},
            {"A103", "Double", 2, 2, 0},
            {"A104", "Triple", 3, 1, 2},
            {"A105", "Triple", 3, 3, 0}
        };

        DefaultTableModel model = new DefaultTableModel(roomData, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        roomTable = new JTable(model);
        scrollPane = new JScrollPane(roomTable);
        scrollPane.setBounds(30, 100, 620, 180);

        backButton = new JButton("Back");
        backButton.setBounds(280, 300, 100, 30);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new StudentDashboard(searchRoom.this.studentId, searchRoom.this.studentName, searchRoom.this.studentGender);
                dispose();
            }
        });

        panel.add(titleLabel);
        panel.add(welcomeLabel);
        panel.add(scrollPane);
        panel.add(backButton);

        add(panel);
        setVisible(true);
    }
}
