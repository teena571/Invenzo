package models;

public class Booking {
    private Guest guest;
    private Room room;

    public Booking(Guest guest,Room room){
        this.guest=guest;
        this.room=room;
    }

    public Guest getGuest() { return guest; }
    public Room getRoom() { return room; }

    @Override
    public String toString(){
        return "Booking - Guest: "+guest.getName()+", Room: "+room.getRoomNumber();
    }
}
