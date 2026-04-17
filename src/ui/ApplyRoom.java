package ui;

import util.SessionTimeout;

// ui
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

// DB connection
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import db.DBConnection;

public class ApplyRoom extends JFrame {

    JLabel titleLabel;
    JLabel studentIdLabel, nameLabel, genderLabel;
    JLabel studentIdValue, nameValue, genderValue;
    JLabel selectedRoomLabel, selectedRoomValue;
    JLabel specialRequestLabel;
    JLabel searchLabel;

    JTable roomTable;
    JScrollPane tableScrollPane, requestScrollPane;
    JTextArea specialRequestArea;
    JTextField searchField;

    JButton applyButton, clearButton, backButton, searchButton;
    JPanel panel;

    String studentId;
    String studentName;
    String studentGender;

    public ApplyRoom(String studentId, String studentName, String studentGender) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentGender = studentGender;

        setTitle("Apply for Room");
        setSize(800, 560);
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

        searchLabel = new JLabel("Search Room:");
        searchLabel.setBounds(420, 100, 100, 25);

        searchField = new JTextField();
        searchField.setBounds(520, 100, 140, 25);

        searchButton = new JButton("Search");
        searchButton.setBounds(670, 100, 80, 25);

        String[] columnNames = {"Room ID", "Room Number", "Room Type", "Capacity", "Occupied", "Available"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        roomTable = new JTable(model);
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Hide Room ID column visually
        roomTable.getColumnModel().getColumn(0).setMinWidth(0);
        roomTable.getColumnModel().getColumn(0).setMaxWidth(0);
        roomTable.getColumnModel().getColumn(0).setWidth(0);

        tableScrollPane = new JScrollPane(roomTable);
        tableScrollPane.setBounds(40, 180, 710, 150);

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
                String roomNum = roomTable.getValueAt(selectedRow, 1).toString();
                selectedRoomValue.setText(roomNum);
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchRooms();
            }
        });

        searchField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchRooms();
            }
        });

        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleApplication();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchField.setText("");
                loadRooms();
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
        panel.add(searchLabel);
        panel.add(searchField);
        panel.add(searchButton);
        panel.add(tableScrollPane);
        panel.add(selectedRoomLabel);
        panel.add(selectedRoomValue);
        panel.add(specialRequestLabel);
        panel.add(requestScrollPane);
        panel.add(applyButton);
        panel.add(clearButton);
        panel.add(backButton);

        add(panel);

        loadRooms();

        setVisible(true);

        SessionTimeout.start(this);
    }

    private void loadRooms() {
        String sql = "SELECT roomID, roomNum, roomType, capacity, occupied, (capacity - occupied) AS available FROM rooms";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            DefaultTableModel model = (DefaultTableModel) roomTable.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("roomID"),
                    rs.getString("roomNum"),
                    rs.getString("roomType"),
                    rs.getInt("capacity"),
                    rs.getInt("occupied"),
                    rs.getInt("available")
                };
                model.addRow(row);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load rooms.");
            e.printStackTrace();
        }
    }

    private void searchRooms() {
        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            loadRooms();
            return;
        }

        String sql = "SELECT roomID, roomNum, roomType, capacity, occupied, (capacity - occupied) AS available "
                   + "FROM rooms "
                   + "WHERE roomNum LIKE ? OR roomType LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);

            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) roomTable.getModel();
                model.setRowCount(0);

                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("roomID"),
                        rs.getString("roomNum"),
                        rs.getString("roomType"),
                        rs.getInt("capacity"),
                        rs.getInt("occupied"),
                        rs.getInt("available")
                    };
                    model.addRow(row);
                }

                roomTable.clearSelection();
                selectedRoomValue.setText("None");

                if (model.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "No matching rooms found.");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to search rooms.");
            e.printStackTrace();
        }
    }

    private void handleApplication() {
        int selectedRow = roomTable.getSelectedRow();
        String specialRequest = specialRequestArea.getText().trim();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room first.");
            return;
        }

        if (specialRequest.length() > 255) {
            JOptionPane.showMessageDialog(this, "Special request is too long.");
            return;
        }

        int roomId = Integer.parseInt(roomTable.getValueAt(selectedRow, 0).toString());
        int available = Integer.parseInt(roomTable.getValueAt(selectedRow, 5).toString());

        if (available <= 0) {
            JOptionPane.showMessageDialog(this, "This room is already full.");
            return;
        }

        String checkSql = "SELECT applicationID FROM applications WHERE studentID = ? AND status IN ('Pending', 'Approved')";
        String insertSql = "INSERT INTO applications (studentID, roomID, specialRequest, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {

            // Check if student already has an active application
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setString(1, studentId);

                try (ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "You already have an active application.");
                        return;
                    }
                }
            }

            // Insert new application
            try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                insertPs.setString(1, studentId);
                insertPs.setInt(2, roomId);
                insertPs.setString(3, specialRequest.isEmpty() ? null : specialRequest);
                insertPs.setString(4, "Pending");

                int rowsInserted = insertPs.executeUpdate();

                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Application submitted successfully.");
                    roomTable.clearSelection();
                    selectedRoomValue.setText("None");
                    specialRequestArea.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Application submission failed.");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database error occurred while submitting application.");
            e.printStackTrace();
        }
    }
}