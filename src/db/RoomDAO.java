/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

/**
 *
 * @author LUQMAN_AF
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import RoomsDetails.Room;
import db.DBConnection;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Acer
 */
public class RoomDAO {
    
    // CREATE - Add a new room
    public boolean addRoom(Room room) {
        String query = "INSERT INTO rooms (roomNum, roomType, capacity, occupied, available) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, room.getRoomNum());
            pstmt.setString(2, room.getRoomType());
            pstmt.setInt(3, room.getCapacity());
            pstmt.setInt(4, room.getOccupied());
            pstmt.setInt(5, room.getAvailable());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Duplicate entry error
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
        String query = "SELECT * FROM rooms ORDER BY roomNum";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Room room = new Room(
                    rs.getString("roomNum"),
                    rs.getString("roomType"),
                    rs.getInt("capacity"),
                    rs.getInt("occupied"),
                    rs.getInt("available")
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
        String query = "UPDATE rooms SET roomType = ?, capacity = ?, occupied = ?, available = ? WHERE roomNum = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, room.getRoomType());
            pstmt.setInt(2, room.getCapacity());
            pstmt.setInt(3, room.getOccupied());
            pstmt.setInt(4, room.getAvailable());
            pstmt.setString(5, room.getRoomNum());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating room: " + e.getMessage());
            return false;
        }
    }
    
    // DELETE - Delete a room
    public boolean deleteRoom(String roomNum) {
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Are you sure you want to delete room " + roomNum + "?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm != JOptionPane.YES_OPTION) {
            return false;
        }
        
        String query = "DELETE FROM rooms WHERE roomNum = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, roomNum);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting room: " + e.getMessage());
            return false;
        }
    }
    
    // Search room by number
    public Room searchRoom(String roomNum) {
        String query = "SELECT * FROM rooms WHERE roomNum = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, roomNum);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Room(
                    rs.getString("roomNum"),
                    rs.getString("roomType"),
                    rs.getInt("capacity"),
                    rs.getInt("occupied"),
                    rs.getInt("available")
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