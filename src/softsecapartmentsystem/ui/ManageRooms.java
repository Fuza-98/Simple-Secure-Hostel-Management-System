package softsecapartmentsystem.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class ManageRooms extends JFrame {
    JTable roomTable;
    DefaultTableModel model;
    JTextField roomNoField, typeField, capField;
    JButton addButton, updateButton, deleteButton, backButton;

    public ManageRooms() {
        setTitle("Room Management System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Table Setup
        String[] columns = {"Room Number", "Room Type", "Capacity"};
        Object[][] data = {
            {"A101", "Single", "1"},
            {"A102", "Double", "2"}
        };
        model = new DefaultTableModel(data, columns);
        roomTable = new JTable(model);
        add(new JScrollPane(roomTable), BorderLayout.CENTER);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        inputPanel.add(new JLabel("Room Number:"));
        roomNoField = new JTextField();
        inputPanel.add(roomNoField);

        inputPanel.add(new JLabel("Room Type:"));
        typeField = new JTextField();
        inputPanel.add(typeField);

        inputPanel.add(new JLabel("Capacity:"));
        capField = new JTextField();
        inputPanel.add(capField);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Room");
        updateButton = new JButton("Update Selected");
        deleteButton = new JButton("Delete Selected");
        backButton = new JButton("Back to Dashboard");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(inputPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // --- CRUD LOGIC ---

        // CREATE
        addButton.addActionListener(e -> {
            if(roomNoField.getText().isEmpty()) return;
            model.addRow(new Object[]{roomNoField.getText(), typeField.getText(), capField.getText()});
            clearFields();
        });

        // UPDATE
        updateButton.addActionListener(e -> {
            int i = roomTable.getSelectedRow();
            if (i >= 0) {
                model.setValueAt(roomNoField.getText(), i, 0);
                model.setValueAt(typeField.getText(), i, 1);
                model.setValueAt(capField.getText(), i, 2);
            } else {
                JOptionPane.showMessageDialog(null, "Select a row to update");
            }
        });

        // DELETE
        deleteButton.addActionListener(e -> {
            int i = roomTable.getSelectedRow();
            if (i >= 0) {
                model.removeRow(i);
                clearFields();
            } else {
                JOptionPane.showMessageDialog(null, "Select a row to delete");
            }
        });

        // LOAD DATA TO FIELDS ON CLICK
        roomTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int i = roomTable.getSelectedRow();
                roomNoField.setText(model.getValueAt(i, 0).toString());
                typeField.setText(model.getValueAt(i, 1).toString());
                capField.setText(model.getValueAt(i, 2).toString());
            }
        });

        backButton.addActionListener(e -> {
            new AdminDashboard();
            dispose();
        });

        setVisible(true);
    }

    private void clearFields() {
        roomNoField.setText("");
        typeField.setText("");
        capField.setText("");
    }
}