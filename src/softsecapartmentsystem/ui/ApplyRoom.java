package softsecapartmentsystem.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class ApplyRoom extends JFrame {

    JLabel titleLabel;
    JLabel studentIdLabel, nameLabel, genderLabel;
    JLabel studentIdValue, nameValue, genderValue;
    JLabel selectedRoomLabel, selectedRoomValue;
    JLabel specialRequestLabel;

    JTable roomTable;
    JScrollPane tableScrollPane, requestScrollPane;
    JTextArea specialRequestArea;

    JButton applyButton, clearButton, backButton;
    JPanel panel;

    String studentId;
    String studentName;
    String studentGender;

    public ApplyRoom(String studentId, String studentName, String studentGender) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentGender = studentGender;

        setTitle("Apply for Room");
        setSize(760, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 248, 255));

        titleLabel = new JLabel("Apply for Hostel Room");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(260, 20, 250, 30);

        studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setBounds(40, 70, 100, 25);
        studentIdValue = new JLabel(this.studentId);
        studentIdValue.setBounds(130, 70, 180, 25);

        nameLabel = new JLabel("Name:");
        nameLabel.setBounds(40, 100, 100, 25);
        nameValue = new JLabel(this.studentName);
        nameValue.setBounds(130, 100, 180, 25);

        genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(40, 130, 100, 25);
        genderValue = new JLabel(this.studentGender);
        genderValue.setBounds(130, 130, 180, 25);

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
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableScrollPane = new JScrollPane(roomTable);
        tableScrollPane.setBounds(40, 180, 660, 150);

        selectedRoomLabel = new JLabel("Selected Room:");
        selectedRoomLabel.setBounds(40, 350, 100, 25);

        selectedRoomValue = new JLabel("None");
        selectedRoomValue.setBounds(150, 350, 150, 25);
        selectedRoomValue.setFont(new Font("Arial", Font.BOLD, 14));

        specialRequestLabel = new JLabel("Special Request:");
        specialRequestLabel.setBounds(40, 390, 110, 25);

        specialRequestArea = new JTextArea();
        requestScrollPane = new JScrollPane(specialRequestArea);
        requestScrollPane.setBounds(150, 390, 220, 50);

        applyButton = new JButton("Apply");
        applyButton.setBounds(430, 390, 90, 30);

        clearButton = new JButton("Clear");
        clearButton.setBounds(540, 390, 90, 30);

        backButton = new JButton("Back");
        backButton.setBounds(650, 390, 80, 30);

        roomTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = roomTable.getSelectedRow();
            if (selectedRow != -1) {
                String roomNumber = roomTable.getValueAt(selectedRow, 0).toString();
                selectedRoomValue.setText(roomNumber);
            }
        });

        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = roomTable.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null,
                            "Please select a room first.");
                    return;
                }

                int available = Integer.parseInt(roomTable.getValueAt(selectedRow, 4).toString());

                if (available <= 0) {
                    JOptionPane.showMessageDialog(null,
                            "This room is already full. Please choose another room.");
                    return;
                }

                String roomNumber = roomTable.getValueAt(selectedRow, 0).toString();
                String roomType = roomTable.getValueAt(selectedRow, 1).toString();
                String specialRequest = specialRequestArea.getText();

                JOptionPane.showMessageDialog(null,
                        "Application Submitted"
                        + "\nStudent ID: " + ApplyRoom.this.studentId
                        + "\nName: " + ApplyRoom.this.studentName
                        + "\nGender: " + ApplyRoom.this.studentGender
                        + "\nRoom Number: " + roomNumber
                        + "\nRoom Type: " + roomType
                        + "\nSpecial Request: " + specialRequest);
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                roomTable.clearSelection();
                selectedRoomValue.setText("None");
                specialRequestArea.setText("");
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new StudentDashboard(
                        ApplyRoom.this.studentId,
                        ApplyRoom.this.studentName,
                        ApplyRoom.this.studentGender
                );
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
        panel.add(tableScrollPane);
        panel.add(selectedRoomLabel);
        panel.add(selectedRoomValue);
        panel.add(specialRequestLabel);
        panel.add(requestScrollPane);
        panel.add(applyButton);
        panel.add(clearButton);
        panel.add(backButton);

        add(panel);
        setVisible(true);
    }
}