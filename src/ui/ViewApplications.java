package ui;

import db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import util.SessionTimeout;

public class ViewApplications extends JFrame {
    
    private String role;

    private JTable applicationTable;
    private DefaultTableModel model;

    private JTextField applicationIdField;
    private JTextField studentIdField;
    private JTextField studentNameField;
    private JTextField roomIdField;
    private JTextField roomNumField;
    private JTextField roomTypeField;
    private JTextField statusField;
    private JTextField dateField;
    private JTextArea specialRequestArea;

    private JButton approveButton, rejectButton, refreshButton, backButton, deleteButton;

    public ViewApplications(String role) {
        this.role = role;

        if (!"admin".equalsIgnoreCase(role)) {
            JOptionPane.showMessageDialog(null, "Access denied. Admins only.");
            dispose();
            return;
        }
        
        setTitle("View Student Applications");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Student Applications", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {
            "Application ID", "Student ID", "Student Name",
            "Room ID", "Room Number", "Room Type",
            "Status", "Application Date"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        applicationTable = new JTable(model);
        applicationTable.setRowHeight(24);
        applicationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane tableScrollPane = new JScrollPane(applicationTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Application List"));
        add(tableScrollPane, BorderLayout.CENTER);

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Selected Application Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        applicationIdField = createReadOnlyField();
        studentIdField = createReadOnlyField();
        studentNameField = createReadOnlyField();
        roomIdField = createReadOnlyField();
        roomNumField = createReadOnlyField();
        roomTypeField = createReadOnlyField();
        statusField = createReadOnlyField();
        dateField = createReadOnlyField();

        specialRequestArea = new JTextArea(3, 20);
        specialRequestArea.setLineWrap(true);
        specialRequestArea.setWrapStyleWord(true);
        specialRequestArea.setEditable(false);
        JScrollPane requestScroll = new JScrollPane(specialRequestArea);

        gbc.gridx = 0; gbc.gridy = 0;
        detailsPanel.add(new JLabel("Application ID:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(applicationIdField, gbc);

        gbc.gridx = 2;
        detailsPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 3;
        detailsPanel.add(studentIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        detailsPanel.add(new JLabel("Student Name:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(studentNameField, gbc);

        gbc.gridx = 2;
        detailsPanel.add(new JLabel("Room ID:"), gbc);
        gbc.gridx = 3;
        detailsPanel.add(roomIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        detailsPanel.add(new JLabel("Room Number:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(roomNumField, gbc);

        gbc.gridx = 2;
        detailsPanel.add(new JLabel("Room Type:"), gbc);
        gbc.gridx = 3;
        detailsPanel.add(roomTypeField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        detailsPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(statusField, gbc);

        gbc.gridx = 2;
        detailsPanel.add(new JLabel("Date Applied:"), gbc);
        gbc.gridx = 3;
        detailsPanel.add(dateField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        detailsPanel.add(new JLabel("Special Request:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        detailsPanel.add(requestScroll, gbc);
        gbc.gridwidth = 1;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        approveButton = new JButton("Approve");
        rejectButton = new JButton("Reject");
        deleteButton = new JButton("Delete Rejected");
        refreshButton = new JButton("Refresh");
        backButton = new JButton("Back");

        styleButton(approveButton, new Color(76, 175, 80));
        styleButton(rejectButton, new Color(244, 67, 54));
        styleButton(deleteButton, new Color(158, 158, 158));
        styleButton(refreshButton, new Color(255, 152, 0));
        styleButton(backButton, new Color(121, 85, 72));

        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton); 
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(detailsPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);

        applicationTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loadSelectedRowToFields();
            }
        });

        approveButton.addActionListener(e -> approveApplication());
        rejectButton.addActionListener(e -> rejectApplication());
        deleteButton.addActionListener(e -> deleteRejectedApplication());
        refreshButton.addActionListener(e -> {
            loadApplications();
            clearFields();
        });
        backButton.addActionListener(e -> {
            new AdminDashboard(ViewApplications.this.role);
            dispose();
        });

        loadApplications();
        setVisible(true);
        
        SessionTimeout.start(this);
    }

    private JTextField createReadOnlyField() {
        JTextField field = new JTextField(15);
        field.setEditable(false);
        field.setBackground(new Color(240, 240, 240));
        return field;
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
    }
    

    private void loadApplications() {
        model.setRowCount(0);

        String sql =
            "SELECT a.applicationID, a.studentID, s.full_name, " +
            "       a.roomID, r.roomNum, r.roomType, " +
            "       a.status, a.date, a.specialRequest " +
            "FROM applications a " +
            "JOIN students s ON a.studentID = s.studentID " +
            "JOIN rooms r ON a.roomID = r.roomID " +
            "ORDER BY a.date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("applicationID"),
                    rs.getString("studentID"),
                    rs.getString("full_name"),
                    rs.getInt("roomID"),
                    rs.getString("roomNum"),
                    rs.getString("roomType"),
                    rs.getString("status"),
                    rs.getTimestamp("date")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load applications: " + e.getMessage());
        }
    }

    private void loadSelectedRowToFields() {
        int row = applicationTable.getSelectedRow();
        if (row < 0) return;

        applicationIdField.setText(model.getValueAt(row, 0).toString());
        studentIdField.setText(model.getValueAt(row, 1).toString());
        studentNameField.setText(model.getValueAt(row, 2).toString());
        roomIdField.setText(model.getValueAt(row, 3).toString());
        roomNumField.setText(model.getValueAt(row, 4).toString());
        roomTypeField.setText(model.getValueAt(row, 5).toString());
        statusField.setText(model.getValueAt(row, 6).toString());
        dateField.setText(model.getValueAt(row, 7).toString());

        loadSpecialRequest(Integer.parseInt(applicationIdField.getText()));
    }

    private void loadSpecialRequest(int applicationId) {
        String sql = "SELECT specialRequest FROM applications WHERE applicationID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, applicationId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String req = rs.getString("specialRequest");
                    specialRequestArea.setText(req != null ? req : "-");
                }
            }

        } catch (SQLException e) {
            specialRequestArea.setText("-");
        }
    }

    private void approveApplication() {
        int row = applicationTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an application first.");
            return;
        }

        int applicationId = Integer.parseInt(model.getValueAt(row, 0).toString());
        int roomId = Integer.parseInt(model.getValueAt(row, 3).toString());
        String currentStatus = model.getValueAt(row, 6).toString();

        if (!"Pending".equalsIgnoreCase(currentStatus)) {
            JOptionPane.showMessageDialog(this, "Only pending applications can be approved.");
            return;
        }

        String checkRoomSql = "SELECT capacity, occupied FROM rooms WHERE roomID = ?";
        String approveSql = "UPDATE applications SET status = 'Approved' WHERE applicationID = ?";
        String occupySql = "UPDATE rooms SET occupied = occupied + 1 WHERE roomID = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            int capacity;
            int occupied;

            try (PreparedStatement psCheck = conn.prepareStatement(checkRoomSql)) {
                psCheck.setInt(1, roomId);

                try (ResultSet rs = psCheck.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        JOptionPane.showMessageDialog(this, "Room not found.");
                        return;
                    }
                    capacity = rs.getInt("capacity");
                    occupied = rs.getInt("occupied");
                }
            }

            if (occupied >= capacity) {
                conn.rollback();
                JOptionPane.showMessageDialog(this, "Room is already full. Cannot approve.");
                return;
            }

            try (PreparedStatement psApprove = conn.prepareStatement(approveSql);
                 PreparedStatement psOccupy = conn.prepareStatement(occupySql)) {

                psApprove.setInt(1, applicationId);
                psApprove.executeUpdate();

                psOccupy.setInt(1, roomId);
                psOccupy.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }

            JOptionPane.showMessageDialog(this, "Application approved successfully.");
            loadApplications();
            clearFields();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to approve application: " + e.getMessage());
        }
    }

    private void rejectApplication() {
        int row = applicationTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an application first.");
            return;
        }

        int applicationId = Integer.parseInt(model.getValueAt(row, 0).toString());
        String currentStatus = model.getValueAt(row, 6).toString();

        if (!"Pending".equalsIgnoreCase(currentStatus)) {
            JOptionPane.showMessageDialog(this, "Only pending applications can be rejected.");
            return;
        }

        String sql = "UPDATE applications SET status = 'Rejected' WHERE applicationID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, applicationId);

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Application rejected.");
                loadApplications();
                clearFields();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to reject application: " + e.getMessage());
        }
    }

    private void clearFields() {
        applicationIdField.setText("");
        studentIdField.setText("");
        studentNameField.setText("");
        roomIdField.setText("");
        roomNumField.setText("");
        roomTypeField.setText("");
        statusField.setText("");
        dateField.setText("");
        specialRequestArea.setText("");
    }
    
    private void deleteRejectedApplication() {
    int row = applicationTable.getSelectedRow();

    if (row < 0) {
        JOptionPane.showMessageDialog(this, "Please select an application first.");
        return;
    }

    int applicationId = Integer.parseInt(model.getValueAt(row, 0).toString());
    String currentStatus = model.getValueAt(row, 6).toString();

    if (!"Rejected".equalsIgnoreCase(currentStatus)) {
        JOptionPane.showMessageDialog(this, "Only rejected applications can be deleted.");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to delete this rejected application?",
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION
    );

    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    String sql = "DELETE FROM applications WHERE applicationID = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, applicationId);

        int rowsDeleted = ps.executeUpdate();

        if (rowsDeleted > 0) {
            JOptionPane.showMessageDialog(this, "Rejected application deleted successfully.");
            loadApplications();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete application.");
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Failed to delete application: " + e.getMessage());
    }
}
}