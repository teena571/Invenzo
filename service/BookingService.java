package service;

import CustomDataStructures.BookingQueue;
import models.Booking;
import models.Guest;
import models.Room;
import java.util.List;
import java.util.ArrayList;
import java.util.List;


public class BookingService {
    private BookingQueue bookingQueue;
    private List<Room>rooms;
    private List<Booking> activeBookings;

    public BookingService(List<Room> rooms){
        this.bookingQueue=new BookingQueue();
        this.activeBookings=new ArrayList<>();
        this.rooms=rooms;
    }

    public boolean isRoomAvailable(Room room){
        return !room.isBooked();
    }

    public Room getAvailabilityRoomByType(String desiredCategory){
        for(Room room : rooms){
            if(room.getCategory().equalsIgnoreCase(desiredCategory) && !room.isBooked()){
                return room;
            }
        }
        return null;
    }
    
    
    public Booking createBooking(Guest guest, Room room) {
        if (!isRoomAvailable(room)) {
            System.out.println("Room is already booked.");
            return null;
        }
    
        room.bookRoom();
        Booking booking = new Booking(guest, room);
        bookingQueue.enqueue(booking);
        return booking;
    }
    

    public Booking processNextBooking(){
        if(bookingQueue.isEmpty()){
            System.out.println("No bookings to process.");
            return null;
        }

        Booking booking = bookingQueue.dequeue();
        System.out.println("Processed booking: "+booking);
        return booking;
    }

    public void viewPendingBookings(){
        System.out.println("Pending bookings in queue: "+bookingQueue.size());
        bookingQueue.displayQueue();
    }

    // public boolean checkIn(Guest guest) {
    //     List<Booking> pendingBookings = bookingQueue.getAllBookings();
    //     for (Booking booking : pendingBookings) {
    //         if (booking.getGuest().getId().equals(guest.getId())) {
    //             activeBookings.add(booking);                      
    //             bookingQueue.removeBooking(booking);              
    //             System.out.println("Guest " + guest.getName() + " checked in successfully.");
    //             return true;
    //         }
    //     }
    //     System.out.println(" No pending booking found for guest: " + guest.getName());
    //     return false;
    // }

    // public boolean checkOut(Guest guest) {
    //     for (Booking booking : activeBookings) {
    //         if (booking.getGuest().getId().equals(guest.getId())) {
    //             booking.getRoom().setBooked(false);   
    //             activeBookings.remove(booking);      
    //             System.out.println("Guest " + guest.getName() + " checked out. Room " +
    //                 booking.getRoom().getRoomNumber() + " is now available.");
    //             return true;
    //         }
    //     }
    //     System.out.println(" No active booking found for guest: " + guest.getName());
    //     return false;
    // }
    
    

}
