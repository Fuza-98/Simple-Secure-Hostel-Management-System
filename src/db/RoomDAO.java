package db;

import RoomsDetails.Room;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    // CREATE - Add a new room
    public boolean addRoom(Room room) {
        String query = "INSERT INTO rooms (roomNum, roomType, capacity, occupied) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, room.getRoomNum());
            pstmt.setString(2, room.getRoomType());
            pstmt.setInt(3, room.getCapacity());
            pstmt.setInt(4, room.getOccupied());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(null, "Room number already exists!");
            } else {
                JOptionPane.showMessageDialog(null, "Error adding room: " + e.getMessage());
            }
            return false;
        }
    }

    // READ - Get all rooms
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT roomID, roomNum, roomType, capacity, occupied FROM rooms ORDER BY roomID";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Room room = new Room(
                    rs.getInt("roomID"),
                    rs.getString("roomNum"),
                    rs.getString("roomType"),
                    rs.getInt("capacity"),
                    rs.getInt("occupied")
                );
                rooms.add(room);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading rooms: " + e.getMessage());
        }

        return rooms;
    }

    // UPDATE - Update a room
    public boolean updateRoom(Room room) {
        String query = "UPDATE rooms SET roomType = ?, capacity = ?, occupied = ? WHERE roomNum = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, room.getRoomType());
            pstmt.setInt(2, room.getCapacity());
            pstmt.setInt(3, room.getOccupied());
            pstmt.setString(4, room.getRoomNum());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating room: " + e.getMessage());
            return false;
        }
    }

    // DELETE - Delete a room
    public boolean deleteRoom(String roomNum) {
        int confirm = JOptionPane.showConfirmDialog(
            null,
            "Are you sure you want to delete room " + roomNum + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return false;
        }

        String checkRoomSql = "SELECT roomID, occupied FROM rooms WHERE roomNum = ?";
        String checkApplicationsSql = "SELECT COUNT(*) FROM applications WHERE roomID = ?";
        String deleteSql = "DELETE FROM rooms WHERE roomNum = ?";

        try (Connection conn = DBConnection.getConnection()) {
            int roomId = -1;
            int occupied = 0;
            int applicationCount = 0;

            try (PreparedStatement checkRoomStmt = conn.prepareStatement(checkRoomSql)) {
                checkRoomStmt.setString(1, roomNum);

                try (ResultSet rs = checkRoomStmt.executeQuery()) {
                    if (rs.next()) {
                        roomId = rs.getInt("roomID");
                        occupied = rs.getInt("occupied");
                    } else {
                        JOptionPane.showMessageDialog(
                            null,
                            "Room not found.",
                            "Delete Failed",
                            JOptionPane.WARNING_MESSAGE
                        );
                        return false;
                    }
                }
            }

            try (PreparedStatement checkAppStmt = conn.prepareStatement(checkApplicationsSql)) {
                checkAppStmt.setInt(1, roomId);

                try (ResultSet rs = checkAppStmt.executeQuery()) {
                    if (rs.next()) {
                        applicationCount = rs.getInt(1);
                    }
                }
            }

            boolean isOccupied = occupied > 0;
            boolean hasApplications = applicationCount > 0;

            if (isOccupied && hasApplications) {
                JOptionPane.showMessageDialog(
                    null,
                    "Cannot delete this room because it is currently occupied and has related application records.",
                    "Delete Not Allowed",
                    JOptionPane.WARNING_MESSAGE
                );
                return false;
            }

            if (isOccupied) {
                JOptionPane.showMessageDialog(
                    null,
                    "Cannot delete this room because it is currently occupied.",
                    "Delete Not Allowed",
                    JOptionPane.WARNING_MESSAGE
                );
                return false;
            }

            if (hasApplications) {
                JOptionPane.showMessageDialog(
                    null,
                    "Cannot delete this room because it has related student application records.",
                    "Delete Not Allowed",
                    JOptionPane.WARNING_MESSAGE
                );
                return false;
            }

            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setString(1, roomNum);
                return deleteStmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                null,
                "Error deleting room: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }

    // Search room by number
    public Room searchRoom(String roomNum) {
        String query = "SELECT roomID, roomNum, roomType, capacity, occupied FROM rooms WHERE roomNum = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, roomNum);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Room(
                    rs.getInt("roomID"),
                    rs.getString("roomNum"),
                    rs.getString("roomType"),
                    rs.getInt("capacity"),
                    rs.getInt("occupied")
                );
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error searching room: " + e.getMessage());
        }

        return null;
    }

    // Check if room exists
    public boolean roomExists(String roomNum) {
        String query = "SELECT COUNT(*) FROM rooms WHERE roomNum = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, roomNum);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error checking room: " + e.getMessage());
        }

        return false;
    }
}