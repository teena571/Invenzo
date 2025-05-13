package models;

import java.io.Serializable;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    private int roomNumber;
    private String category;
    private boolean isBooked;

    public Room(int roomNumber,String category){
        this.roomNumber=roomNumber;
        this.category=category;
        this.isBooked=false;
    }

    public int getRoomNumber(){ return roomNumber; }
    public String getCategory() { return category; }
    public boolean isBooked() { return isBooked; }
    public void bookRoom() { this.isBooked = true; }
    public void cancelBooking() { this.isBooked=false; }

    @Override
    public String toString(){
        return "Room No. : "+roomNumber+" , Category : "+category+", Status: "+(isBooked ? "Booked" : "Available");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return roomNumber == room.roomNumber;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(roomNumber);
    }
}
