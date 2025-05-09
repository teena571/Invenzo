package models;

import java.io.Serializable;
import java.time.LocalDate;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;
    private Guest guest;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private boolean isWaiting;

    public Booking(Guest guest, Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        this.guest = guest;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.isWaiting = false;
    }

    public Guest getGuest() { return guest; }
    public Room getRoom() { return room; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public boolean isWaiting() { return isWaiting; }
    public void setWaiting(boolean waiting) { isWaiting = waiting; }

    @Override
    public String toString() {
        return String.format("Booking [Guest=%s, Room=%d, Check-in=%s, Check-out=%s, Status=%s]",
            guest.getName(), room.getRoomNumber(), checkInDate, checkOutDate,
            isWaiting ? "Waiting" : "Confirmed");
    }
}
