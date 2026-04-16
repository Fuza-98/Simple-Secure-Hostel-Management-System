package RoomsDetails;

public class Room {
    private int roomID;
    private String roomNum;
    private String roomType;
    private int capacity;
    private int occupied;

    public Room(int roomID, String roomNum, String roomType, int capacity, int occupied) {
        this.roomID = roomID;
        this.roomNum = roomNum;
        this.roomType = roomType;
        this.capacity = capacity;
        this.occupied = occupied;
    }

    public Room(String roomNum, String roomType, int capacity, int occupied) {
        this.roomNum = roomNum;
        this.roomType = roomType;
        this.capacity = capacity;
        this.occupied = occupied;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getOccupied() {
        return occupied;
    }

    public void setOccupied(int occupied) {
        this.occupied = occupied;
    }

    public int getAvailable() {
        return capacity - occupied;
    }
}