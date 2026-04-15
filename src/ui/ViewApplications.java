package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class ViewApplications extends JFrame {

    JTable applicationTable;
    DefaultTableModel model;

    JTextField studentIdField, studentNameField, genderField, roomField, statusField;
    JButton approveButton, rejectButton, backButton;

    JPanel panel, formPanel, buttonPanel;
    JScrollPane tableScrollPane;

    public ViewApplications() {
        setTitle("View Student Applications");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(245, 245, 255));

        // ===== Title =====
        JLabel titleLabel = new JLabel("Student Applications", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        panel.add(titleLabel, BorderLayout.NORTH);

        // ===== Table =====
        String[] columns = {"Student ID", "Student Name", "Gender", "Requested Room", "Status"};
        Object[][] data = {
            {"20241234", "Ali bin Ahmad", "Male", "A102", "Pending"},
            {"20241235", "Siti Nur Aisyah", "Female", "A104", "Pending"},
            {"20241236", "Daniel Lee", "Male", "A101", "Approved"},
            {"20241237", "Nur Izzah", "Female", "A105", "Rejected"}
        };

        model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        applicationTable = new JTable(model);
        applicationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        applicationTable.setRowHeight(25);

        tableScrollPane = new JScrollPane(applicationTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Application List"));
        panel.add(tableScrollPane, BorderLayout.CENTER);

        // ===== Bottom Section =====
        JPanel southPanel = new JPanel(new BorderLayout(10, 10));
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        southPanel.setBackground(new Color(245, 245, 255));

        // ===== Form Panel =====
        formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Selected Application Details"));
        formPanel.setBackground(new Color(245, 245, 255));

        studentIdField = new JTextField();
        studentNameField = new JTextField();
        genderField = new JTextField();
        roomField = new JTextField();
        statusField = new JTextField();

        studentIdField.setEditable(false);
        studentNameField.setEditable(false);
        genderField.setEditable(false);
        roomField.setEditable(false);
        statusField.setEditable(false);

        formPanel.add(new JLabel("Student ID:"));
        formPanel.add(studentIdField);

        formPanel.add(new JLabel("Student Name:"));
        formPanel.add(studentNameField);

        formPanel.add(new JLabel("Gender:"));
        formPanel.add(genderField);

        formPanel.add(new JLabel("Requested Room:"));
        formPanel.add(roomField);

        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusField);

        // ===== Button Panel =====
        buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 255));

        approveButton = new JButton("Approve");
        rejectButton = new JButton("Reject");
        backButton = new JButton("Back to Dashboard");

        approveButton.setBackground(new Color(40, 167, 69));
        approveButton.setForeground(Color.WHITE);

        rejectButton.setBackground(new Color(220, 53, 69));
        rejectButton.setForeground(Color.WHITE);

        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(backButton);

        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(southPanel, BorderLayout.SOUTH);

        // ===== Events =====

        applicationTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int i = applicationTable.getSelectedRow();
                if (i >= 0) {
                    studentIdField.setText(model.getValueAt(i, 0).toString());
                    studentNameField.setText(model.getValueAt(i, 1).toString());
                    genderField.setText(model.getValueAt(i, 2).toString());
                    roomField.setText(model.getValueAt(i, 3).toString());
                    statusField.setText(model.getValueAt(i, 4).toString());
                }
            }
        });

        approveButton.addActionListener(e -> {
            int i = applicationTable.getSelectedRow();
            if (i >= 0) {
                model.setValueAt("Approved", i, 4);
                statusField.setText("Approved");
                JOptionPane.showMessageDialog(null, "Application approved successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Please select an application first.");
            }
        });

        rejectButton.addActionListener(e -> {
            int i = applicationTable.getSelectedRow();
            if (i >= 0) {
                model.setValueAt("Rejected", i, 4);
                statusField.setText("Rejected");
                JOptionPane.showMessageDialog(null, "Application rejected.");
            } else {
                JOptionPane.showMessageDialog(null, "Please select an application first.");
            }
        });

        backButton.addActionListener(e -> {
            new AdminDashboard();
            dispose();
        });

        add(panel);
        setVisible(true);
    }
}