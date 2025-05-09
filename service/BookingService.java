package service;

import CustomDataStructures.BookingQueue;
import models.Booking;
import models.Guest;
import models.Room;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class BookingService {
    private BookingQueue bookingQueue;
    private List<Room> rooms;
    private List<Booking> activeBookings;
    private List<Booking> waitingList;

    public BookingService(List<Room> rooms) {
        this.bookingQueue = new BookingQueue();
        this.activeBookings = new ArrayList<>();
        this.waitingList = new ArrayList<>();
        this.rooms = rooms;
    }

    public boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        // Check if room is booked for the requested dates
        for (Booking booking : activeBookings) {
            if (booking.getRoom().equals(room) && !booking.isWaiting()) {
                // Check for date overlap
                if (!(checkOut.isBefore(booking.getCheckInDate()) || 
                      checkIn.isAfter(booking.getCheckOutDate()))) {
                    return false;
                }
            }
        }
        return true;
    }

    public Room getAvailableRoomByType(String desiredCategory, LocalDate checkIn, LocalDate checkOut) {
        for (Room room : rooms) {
            if (room.getCategory().equalsIgnoreCase(desiredCategory) && 
                isRoomAvailable(room, checkIn, checkOut)) {
                return room;
            }
        }
        return null;
    }

    public Booking createBooking(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut) {
        if (!isRoomAvailable(room, checkIn, checkOut)) {
            // Add to waiting list
            Booking waitingBooking = new Booking(guest, room, checkIn, checkOut);
            waitingBooking.setWaiting(true);
            waitingList.add(waitingBooking);
            System.out.println("Room is not available for the selected dates. Added to waiting list.");
            return waitingBooking;
        }

        // Create confirmed booking
        Booking booking = new Booking(guest, room, checkIn, checkOut);
        bookingQueue.enqueue(booking);
        activeBookings.add(booking);
        return booking;
    }

    public List<Booking> getBookingsByGuest(Guest guest) {
        List<Booking> guestBookings = new ArrayList<>();
        // Add active bookings
        for (Booking booking : activeBookings) {
            if (booking.getGuest().getId() == guest.getId()) {
                guestBookings.add(booking);
            }
        }
        // Add waiting list bookings
        for (Booking booking : waitingList) {
            if (booking.getGuest().getId() == guest.getId()) {
                guestBookings.add(booking);
            }
        }
        return guestBookings;
    }

    public List<Booking> getWaitingList() {
        return new ArrayList<>(waitingList);
    }

    public void processWaitingList() {
        List<Booking> toRemove = new ArrayList<>();
        for (Booking waitingBooking : waitingList) {
            if (isRoomAvailable(waitingBooking.getRoom(), 
                               waitingBooking.getCheckInDate(), 
                               waitingBooking.getCheckOutDate())) {
                // Convert waiting booking to confirmed booking
                waitingBooking.setWaiting(false);
                activeBookings.add(waitingBooking);
                bookingQueue.enqueue(waitingBooking);
                toRemove.add(waitingBooking);
            }
        }
        waitingList.removeAll(toRemove);
    }

    public boolean cancelBooking(Booking booking) {
        if (booking == null) return false;
        
        if (booking.isWaiting()) {
            return waitingList.remove(booking);
        } else {
            if (activeBookings.remove(booking)) {
                bookingQueue.removeBooking(booking);
                processWaitingList(); // Check if any waiting bookings can be confirmed
                return true;
            }
        }
        return false;
    }

    public Booking processNextBooking() {
        if (bookingQueue.isEmpty()) {
            System.out.println("No bookings to process.");
            return null;
        }

        Booking booking = bookingQueue.dequeue();
        System.out.println("Processed booking: " + booking);
        return booking;
    }

    public void viewPendingBookings() {
        System.out.println("Pending bookings in queue: " + bookingQueue.size());
        bookingQueue.displayQueue();
    }

    public List<Booking> getActiveBookings() {
        return new ArrayList<>(activeBookings);
    }
}
