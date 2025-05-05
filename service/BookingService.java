package service;

import CustomDataStructures.BookingQueue;
import models.Booking;
import models.Guest;
import models.Room;

public class BookingService {
    private BookingQueue bookingQueue;

    public BookingService(){
        bookingQueue=new BookingQueue();
    }

    public boolean isRoomAvailable(Room room) {
        return !room.isBooked();
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

}
