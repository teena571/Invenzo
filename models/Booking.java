package models;

public class Booking {
    private Guest guest;
    private Room room;
    private int GuestID;

    public Booking(Guest guest,Room room){
        this.guest=guest;
        this.room=room;
        this.GuestID=guest.getId();
    }

    public Guest getGuest() { return guest; }
    public Room getRoom() { return room; }
    public int getGuestID() { return GuestID; }

    @Override
    public String toString(){
        return "Booking - Guest: "+guest.getName()+", Room: "+room.getRoomNumber();
    }
}
