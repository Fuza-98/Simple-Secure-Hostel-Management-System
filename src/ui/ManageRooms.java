/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import db.RoomDAO;
import RoomsDetails.Room;

/**
 *
 * @author Acer
 */
public class ManageRooms extends JFrame {
    private JTable roomTable;
    private DefaultTableModel model;
    private JTextField roomNoField;
    private JComboBox<String> roomTypeCombo;
    private JTextField capField, occupiedField, availableField;
    private JButton addButton, updateButton, deleteButton, backButton, refreshButton, clearButton;
    private RoomDAO roomDAO;
    
    public ManageRooms() {
        roomDAO = new RoomDAO();
        
        setTitle("Room Management System");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        // Table Setup
        String[] columns = {"Room Number", "Room Type", "Capacity", "Occupied", "Available", "Status"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        roomTable = new JTable(model);
        roomTable.setFont(new Font("Arial", Font.PLAIN, 14));
        roomTable.setRowHeight(25);
        roomTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Room List"));
        add(scrollPane, BorderLayout.CENTER);
        
        // Load data from database
        loadTableData();
        
        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Room Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Row 0
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Room Number:"), gbc);
        gbc.gridx = 1;
        roomNoField = new JTextField(15);
        inputPanel.add(roomNoField, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Room Type:"), gbc);
        gbc.gridx = 3;
        // Dropdown for room types - SINGLE, DOUBLE, TRIPLE
        String[] roomTypes = {"SINGLE", "DOUBLE", "TRIPLE"};
        roomTypeCombo = new JComboBox<>(roomTypes);
        roomTypeCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        roomTypeCombo.setPreferredSize(new Dimension(150, 25));
        inputPanel.add(roomTypeCombo, gbc);
        
        // Row 1
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Capacity:"), gbc);
        gbc.gridx = 1;
        capField = new JTextField(15);
        capField.setEditable(false); // Make non-editable
        capField.setBackground(new Color(240, 240, 240)); // Light gray background to show it's read-only
        capField.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(capField, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Occupied:"), gbc);
        gbc.gridx = 3;
        occupiedField = new JTextField("0", 15);
        occupiedField.setEditable(false); // Make non-editable
        occupiedField.setBackground(new Color(240, 240, 240));
        occupiedField.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(occupiedField, gbc);
        
        // Row 2
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Available:"), gbc);
        gbc.gridx = 1;
        availableField = new JTextField("0", 15);
        availableField.setEditable(false); // Make non-editable
        availableField.setBackground(new Color(240, 240, 240));
        availableField.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(availableField, gbc);
        
        // Add buttons for occupied adjustment
        JPanel occupiedButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JButton incrementOccupied = new JButton("+");
        JButton decrementOccupied = new JButton("-");
        styleSmallButton(incrementOccupied, new Color(76, 175, 80));
        styleSmallButton(decrementOccupied, new Color(244, 67, 54));
        occupiedButtonPanel.add(decrementOccupied);
        occupiedButtonPanel.add(incrementOccupied);
        gbc.gridx = 3;
        gbc.gridy = 3;
        inputPanel.add(occupiedButtonPanel, gbc);
        
        // Add label for instruction
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        JLabel instructionLabel = new JLabel("Note: Use +/- buttons to adjust occupied count. Capacity is automatically set by room type.");
        instructionLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        instructionLabel.setForeground(Color.GRAY);
        inputPanel.add(instructionLabel, gbc);
        
        // Auto-set capacity based on room type
        roomTypeCombo.addActionListener(e -> {
            String selectedType = (String) roomTypeCombo.getSelectedItem();
            if (selectedType != null) {
                int capacity = 0;
                switch (selectedType) {
                    case "SINGLE":
                        capacity = 1;
                        break;
                    case "DOUBLE":
                        capacity = 2;
                        break;
                    case "TRIPLE":
                        capacity = 3;
                        break;
                }
                capField.setText(String.valueOf(capacity));
                updateAvailableCapacity();
            }
        });
        
        // Increment occupied button action
        incrementOccupied.addActionListener(e -> {
            try {
                int currentOccupied = Integer.parseInt(occupiedField.getText());
                int capacity = Integer.parseInt(capField.getText());
                if (currentOccupied < capacity) {
                    occupiedField.setText(String.valueOf(currentOccupied + 1));
                    updateAvailableCapacity();
                } else {
                    JOptionPane.showMessageDialog(null, 
                        "Cannot exceed room capacity! Maximum occupied is " + capacity,
                        "Limit Reached",
                        JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                occupiedField.setText("0");
            }
        });
        
        // Decrement occupied button action
        decrementOccupied.addActionListener(e -> {
            try {
                int currentOccupied = Integer.parseInt(occupiedField.getText());
                if (currentOccupied > 0) {
                    occupiedField.setText(String.valueOf(currentOccupied - 1));
                    updateAvailableCapacity();
                }
            } catch (NumberFormatException ex) {
                occupiedField.setText("0");
            }
        });
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton("Add Room");
        updateButton = new JButton("Update Selected");
        deleteButton = new JButton("Delete Selected");
        refreshButton = new JButton("Refresh");
        clearButton = new JButton("Clear Fields");
        backButton = new JButton("Back to Dashboard");
        
        // Style buttons
        styleButton(addButton, new Color(76, 175, 80));
        styleButton(updateButton, new Color(33, 150, 243));
        styleButton(deleteButton, new Color(244, 67, 54));
        styleButton(refreshButton, new Color(255, 152, 0));
        styleButton(clearButton, new Color(158, 158, 158));
        styleButton(backButton, new Color(121, 85, 72));
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);
        
        JPanel southPanel = new JPanel(new BorderLayout(10, 10));
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        southPanel.add(inputPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);
        
        // --- CRUD LOGIC WITH DATABASE ---
        
        // CREATE
        addButton.addActionListener(e -> {
            if (validateInputs()) {
                Room room = new Room(
                    roomNoField.getText().trim().toUpperCase(),
                    (String) roomTypeCombo.getSelectedItem(),
                    Integer.parseInt(capField.getText()),
                    Integer.parseInt(occupiedField.getText()),
                    Integer.parseInt(availableField.getText())
                );
                
                if (roomDAO.addRoom(room)) {
                    JOptionPane.showMessageDialog(null, "Room added successfully!");
                    loadTableData();
                    clearFields();
                }
            }
        });
        
        // UPDATE
        updateButton.addActionListener(e -> {
            int selectedRow = roomTable.getSelectedRow();
            if (selectedRow >= 0) {
                if (validateInputs()) {
                    Room room = new Room(
                        roomNoField.getText().trim().toUpperCase(),
                        (String) roomTypeCombo.getSelectedItem(),
                        Integer.parseInt(capField.getText()),
                        Integer.parseInt(occupiedField.getText()),
                        Integer.parseInt(availableField.getText())
                    );
                    
                    if (roomDAO.updateRoom(room)) {
                        JOptionPane.showMessageDialog(null, "Room updated successfully!");
                        loadTableData();
                        clearFields();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a room to update");
            }
        });
        
        // DELETE
        deleteButton.addActionListener(e -> {
            int selectedRow = roomTable.getSelectedRow();
            if (selectedRow >= 0) {
                String roomNum = model.getValueAt(selectedRow, 0).toString();
                if (roomDAO.deleteRoom(roomNum)) {
                    JOptionPane.showMessageDialog(null, "Room deleted successfully!");
                    loadTableData();
                    clearFields();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a room to delete");
            }
        });
        
        // REFRESH
        refreshButton.addActionListener(e -> {
            loadTableData();
            clearFields();
            JOptionPane.showMessageDialog(null, "Data refreshed!");
        });
        
        // CLEAR FIELDS
        clearButton.addActionListener(e -> {
            clearFields();
            roomNoField.setEditable(true);
        });
        
        // LOAD DATA TO FIELDS ON CLICK
        roomTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = roomTable.getSelectedRow();
                if (selectedRow >= 0) {
                    roomNoField.setText(model.getValueAt(selectedRow, 0).toString());
                    String roomType = model.getValueAt(selectedRow, 1).toString();
                    roomTypeCombo.setSelectedItem(roomType);
                    capField.setText(model.getValueAt(selectedRow, 2).toString());
                    occupiedField.setText(model.getValueAt(selectedRow, 3).toString());
                    availableField.setText(model.getValueAt(selectedRow, 4).toString());
                    
                    // Make room number field non-editable during update
                    roomNoField.setEditable(false);
                }
            }
        });
        
        backButton.addActionListener(e -> {
            new AdminDashboard();
            dispose();
            // Uncomment if you have AdminDashboard class
            // new AdminDashboard();
        });
        
        // Initialize default values
        clearFields();
        
        setVisible(true);
    }
    
    private void updateAvailableCapacity() {
        try {
            int capacity = Integer.parseInt(capField.getText());
            int occupied = Integer.parseInt(occupiedField.getText());
            int available = capacity - occupied;
            if (available >= 0) {
                availableField.setText(String.valueOf(available));
            } else {
                availableField.setText("0");
                JOptionPane.showMessageDialog(null, 
                    "Occupied cannot exceed capacity!",
                    "Invalid Value",
                    JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            availableField.setText("0");
        }
    }
    
    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void styleSmallButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(40, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void loadTableData() {
        model.setRowCount(0); // Clear existing data
        List<Room> rooms = roomDAO.getAllRooms();
        
        for (Room room : rooms) {
            String status = (room.getAvailable() > 0) ? "Available" : "Full";
            model.addRow(new Object[]{
                room.getRoomNum(),
                room.getRoomType(),
                room.getCapacity(),
                room.getOccupied(),
                room.getAvailable(),
                status
            });
        }
    }
    
    private boolean validateInputs() {
        if (roomNoField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Room Number is required");
            roomNoField.requestFocus();
            return false;
        }
        
        // Check if room number already exists when adding new room
        int selectedRow = roomTable.getSelectedRow();
        boolean isUpdate = (selectedRow >= 0);
        
        if (!isUpdate) {
            String roomNum = roomNoField.getText().trim().toUpperCase();
            if (roomDAO.roomExists(roomNum)) {
                JOptionPane.showMessageDialog(null, "Room number already exists!");
                roomNoField.requestFocus();
                return false;
            }
        }
        
        try {
            int capacity = Integer.parseInt(capField.getText());
            int occupied = Integer.parseInt(occupiedField.getText());
            int available = Integer.parseInt(availableField.getText());
            
            // Validate based on room type
            String selectedType = (String) roomTypeCombo.getSelectedItem();
            int expectedCapacity = 0;
            switch (selectedType) {
                case "SINGLE":
                    expectedCapacity = 1;
                    break;
                case "DOUBLE":
                    expectedCapacity = 2;
                    break;
                case "TRIPLE":
                    expectedCapacity = 3;
                    break;
            }
            
            if (capacity != expectedCapacity) {
                JOptionPane.showMessageDialog(null, 
                    "Capacity for " + selectedType + " room must be " + expectedCapacity,
                    "Invalid Capacity",
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            if (occupied < 0 || occupied > capacity) {
                JOptionPane.showMessageDialog(null, 
                    "Occupied must be between 0 and " + capacity,
                    "Invalid Occupied Count",
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            if (available != (capacity - occupied)) {
                JOptionPane.showMessageDialog(null, 
                    "Available must equal Capacity - Occupied",
                    "Invalid Available Count",
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number format");
            return false;
        }
    }
    
    private void clearFields() {
        roomNoField.setText("");
        roomTypeCombo.setSelectedIndex(0); // Select SINGLE by default
        capField.setText("1");
        occupiedField.setText("0");
        availableField.setText("1");
        roomNoField.setEditable(true);
        roomNoField.requestFocus();
    }
    
    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new ManageRooms();
        });
    }
}