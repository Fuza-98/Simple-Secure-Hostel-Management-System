package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import db.RoomDAO;
import RoomsDetails.Room;

import util.*;
import java.util.logging.*;

public class ManageRooms extends JFrame {
    
    
    private JTable roomTable;
    private DefaultTableModel model;
    private JTextField roomIdField, roomNoField;
    private JComboBox<String> roomTypeCombo;
    private JTextField capField, occupiedField, availableField;
    private JButton addButton, updateButton, deleteButton, backButton, refreshButton, clearButton, searchButton;
    private RoomDAO roomDAO;
    
    private static final Logger logger = Logger.getLogger(ManageRooms.class.getName());

    public ManageRooms() {
        
        try {
            // Create a FileHandler to write logs to a log file
            FileHandler fileHandler = new FileHandler("appLogs.log", true); // 'true' to append to the file
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Check if the user is an admin
        if (!Session.isAdmin()) {
            JOptionPane.showMessageDialog(null, "Access Denied. Admins only.");
            new Login();
            dispose();
            return;
        }
        
        roomDAO = new RoomDAO();

        setTitle("Room Management System");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Table Setup
        String[] columns = {"Room ID", "Room Number", "Room Type", "Capacity", "Occupied", "Available", "Status"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        roomTable = new JTable(model);
        roomTable.setFont(new Font("Arial", Font.PLAIN, 14));
        roomTable.setRowHeight(25);
        roomTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Room List"));
        add(scrollPane, BorderLayout.CENTER);

        loadTableData();

        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Room Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Room ID:"), gbc);
        gbc.gridx = 1;
        roomIdField = new JTextField(15);
        roomIdField.setEditable(false);
        roomIdField.setBackground(new Color(240, 240, 240));
        inputPanel.add(roomIdField, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("Room Number:"), gbc);
        gbc.gridx = 3;
        roomNoField = new JTextField(15);
        inputPanel.add(roomNoField, gbc);

        // Press Enter in room number field to search
        roomNoField.addActionListener(e -> searchAndLoadRoom());

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Room Type:"), gbc);
        gbc.gridx = 1;
        String[] roomTypes = {"SINGLE", "DOUBLE", "TRIPLE"};
        roomTypeCombo = new JComboBox<>(roomTypes);
        roomTypeCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        roomTypeCombo.setPreferredSize(new Dimension(150, 25));
        inputPanel.add(roomTypeCombo, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("Capacity:"), gbc);
        gbc.gridx = 3;
        capField = new JTextField(15);
        capField.setEditable(false);
        capField.setBackground(new Color(240, 240, 240));
        capField.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(capField, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Occupied:"), gbc);
        gbc.gridx = 1;
        occupiedField = new JTextField("0", 15);
        occupiedField.setEditable(false);
        occupiedField.setBackground(new Color(240, 240, 240));
        occupiedField.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(occupiedField, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("Available:"), gbc);
        gbc.gridx = 3;
        availableField = new JTextField("1", 15);
        availableField.setEditable(false);
        availableField.setBackground(new Color(240, 240, 240));
        availableField.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(availableField, gbc);

        // Row 3 - occupied controls
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

        // Row 4 - note
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        JLabel instructionLabel = new JLabel("Note: Use +/- buttons to adjust occupied count. Capacity is automatically set by room type.");
        instructionLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        instructionLabel.setForeground(Color.GRAY);
        inputPanel.add(instructionLabel, gbc);

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

        incrementOccupied.addActionListener(e -> {
            try {
                int currentOccupied = Integer.parseInt(occupiedField.getText());
                int capacity = Integer.parseInt(capField.getText());
                if (currentOccupied < capacity) {
                    occupiedField.setText(String.valueOf(currentOccupied + 1));
                    updateAvailableCapacity();
                } else {
                    JOptionPane.showMessageDialog(
                        null,
                        "Cannot exceed room capacity! Maximum occupied is " + capacity,
                        "Limit Reached",
                        JOptionPane.WARNING_MESSAGE
                    );
                }
            } catch (NumberFormatException ex) {
                occupiedField.setText("0");
                updateAvailableCapacity();
            }
        });

        decrementOccupied.addActionListener(e -> {
            try {
                int currentOccupied = Integer.parseInt(occupiedField.getText());
                if (currentOccupied > 0) {
                    occupiedField.setText(String.valueOf(currentOccupied - 1));
                    updateAvailableCapacity();
                }
            } catch (NumberFormatException ex) {
                occupiedField.setText("0");
                updateAvailableCapacity();
            }
        });

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton("Add Room");
        updateButton = new JButton("Update Selected");
        deleteButton = new JButton("Delete Selected");
        refreshButton = new JButton("Refresh");
        searchButton = new JButton("Search Room");
        clearButton = new JButton("Clear Fields");
        backButton = new JButton("Back to Dashboard");

        styleButton(addButton, new Color(76, 175, 80));
        styleButton(updateButton, new Color(33, 150, 243));
        styleButton(deleteButton, new Color(244, 67, 54));
        styleButton(refreshButton, new Color(255, 152, 0));
        styleButton(searchButton, new Color(103, 58, 183));
        styleButton(clearButton, new Color(158, 158, 158));
        styleButton(backButton, new Color(121, 85, 72));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);

        JPanel southPanel = new JPanel(new BorderLayout(10, 10));
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        southPanel.add(inputPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // CREATE
        addButton.addActionListener(e -> {
            if (validateInputs()) {
                Room room = new Room(
                    roomNoField.getText().trim().toUpperCase(),
                    (String) roomTypeCombo.getSelectedItem(),
                    Integer.parseInt(capField.getText()),
                    Integer.parseInt(occupiedField.getText())
                );

                if (roomDAO.addRoom(room)) {
                    logger.info("Room added: " + room.getRoomNum() + " with ID: " + room.getRoomID());
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
                        Integer.parseInt(roomIdField.getText()),
                        roomNoField.getText().trim().toUpperCase(),
                        (String) roomTypeCombo.getSelectedItem(),
                        Integer.parseInt(capField.getText()),
                        Integer.parseInt(occupiedField.getText())
                    );

                    if (roomDAO.updateRoom(room)) {
                        logger.info("Room updated: " + room.getRoomNum() + " with ID: " + room.getRoomID());
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
                String roomNum = model.getValueAt(selectedRow, 1).toString();
                if (roomDAO.deleteRoom(roomNum)) {
                    logger.info("Room deleted: " + roomNum);
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

        // SEARCH
        searchButton.addActionListener(e -> { 
            logger.info("Search started for room number: " + roomNoField.getText().trim());
        searchAndLoadRoom();});

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
                    roomIdField.setText(model.getValueAt(selectedRow, 0).toString());
                    roomNoField.setText(model.getValueAt(selectedRow, 1).toString());
                    String roomType = model.getValueAt(selectedRow, 2).toString();
                    roomTypeCombo.setSelectedItem(roomType);
                    capField.setText(model.getValueAt(selectedRow, 3).toString());
                    occupiedField.setText(model.getValueAt(selectedRow, 4).toString());
                    availableField.setText(model.getValueAt(selectedRow, 5).toString());

                    roomNoField.setEditable(false);
                }
            }
        });

        backButton.addActionListener(e -> {
            new AdminDashboard();
            dispose();
        });

        clearFields();
        setVisible(true);
        
        SessionTimeout.start(this);
    }

    private void searchAndLoadRoom() {
        String roomNum = roomNoField.getText().trim().toUpperCase();
        
         // Validate room number input
        if (!roomNum.matches("^[A-Za-z0-9\\-]+$")) {
            JOptionPane.showMessageDialog(this, "Room number can only contain letters, numbers, and hyphens.");
            return;
        }
        
        // Check if input exceeds 4 characters
        if (roomNum.length() > 4) {
            JOptionPane.showMessageDialog(this, "Room number or type must not exceed 4 characters.");
            return;
        }

        if (roomNum.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a room number to search.");
            roomNoField.requestFocus();
            return;
        }

        Room room = roomDAO.searchRoom(roomNum);

        if (room == null) {
            JOptionPane.showMessageDialog(null, "Room not found.");
            clearFields();
            roomNoField.setText(roomNum);
            roomNoField.requestFocus();
            return;
        }

        roomIdField.setText(String.valueOf(room.getRoomID()));
        roomNoField.setText(room.getRoomNum());
        roomTypeCombo.setSelectedItem(room.getRoomType());
        capField.setText(String.valueOf(room.getCapacity()));
        occupiedField.setText(String.valueOf(room.getOccupied()));
        availableField.setText(String.valueOf(room.getAvailable()));

        roomNoField.setEditable(false);

        for (int i = 0; i < model.getRowCount(); i++) {
            String tableRoomNum = model.getValueAt(i, 1).toString();
            if (tableRoomNum.equalsIgnoreCase(roomNum)) {
                roomTable.setRowSelectionInterval(i, i);
                roomTable.scrollRectToVisible(roomTable.getCellRect(i, 0, true));
                break;
            }
        }
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
                JOptionPane.showMessageDialog(
                    null,
                    "Occupied cannot exceed capacity!",
                    "Invalid Value",
                    JOptionPane.WARNING_MESSAGE
                );
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
        model.setRowCount(0);
        List<Room> rooms = roomDAO.getAllRooms();

        for (Room room : rooms) {
            String status = (room.getAvailable() > 0) ? "Available" : "Full";
            model.addRow(new Object[]{
                room.getRoomID(),
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
                JOptionPane.showMessageDialog(
                    null,
                    "Capacity for " + selectedType + " room must be " + expectedCapacity,
                    "Invalid Capacity",
                    JOptionPane.WARNING_MESSAGE
                );
                return false;
            }

            if (occupied < 0 || occupied > capacity) {
                JOptionPane.showMessageDialog(
                    null,
                    "Occupied must be between 0 and " + capacity,
                    "Invalid Occupied Count",
                    JOptionPane.WARNING_MESSAGE
                );
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number format");
            return false;
        }
    }

    private void clearFields() {
        roomIdField.setText("");
        roomNoField.setText("");
        roomTypeCombo.setSelectedIndex(0);
        capField.setText("1");
        occupiedField.setText("0");
        availableField.setText("1");
        roomNoField.setEditable(true);
        roomNoField.requestFocus();
    }
}