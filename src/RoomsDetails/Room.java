/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package RoomsDetails;


public class Room {
    private String roomNum;
    private String roomType;
    private int capacity;
    private int occupied;
    private int available;
    
    public Room(String roomNum, String roomType, int capacity, int occupied, int available) {
        this.roomNum = roomNum;
        this.roomType = roomType;
        this.capacity = capacity;
        this.occupied = occupied;
        this.available = available;
    }
    
    // Getters and Setters
    public String getRoomNum() { return roomNum; }
    public void setRoomNum(String roomNum) { this.roomNum = roomNum; }
    
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    
    public int getOccupied() { return occupied; }
    public void setOccupied(int occupied) { this.occupied = occupied; }
    
    public int getAvailable() { return available; }
    public void setAvailable(int available) { this.available = available; }
}
