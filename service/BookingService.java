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
    private WaitingListService waitingListService;

    public BookingService(List<Room> rooms) {
        this.bookingQueue = new BookingQueue();
        this.activeBookings = new ArrayList<>();
        this.rooms = rooms;
        this.waitingListService = new WaitingListService(this);
    }

    public boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        for (Booking booking : activeBookings) {
            if (booking.getRoom().equals(room) && 
                !(checkOut.isBefore(booking.getCheckInDate()) || 
                  checkIn.isAfter(booking.getCheckOutDate()))) {
                return false;
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
        if (isRoomAvailable(room, checkIn, checkOut)) {
            Booking booking = new Booking(guest, room, checkIn, checkOut);
            activeBookings.add(booking);
            System.out.println("Booking created successfully for Room " + room.getRoomNumber());
            return booking;
        } else {
            // Add to waiting list
            Booking waitingBooking = new Booking(guest, room, checkIn, checkOut);
            waitingBooking.setWaiting(true);
            waitingListService.addToWaitingList(waitingBooking);
            System.out.println("Added booking to waiting list for Room " + room.getRoomNumber());
            return waitingBooking;
        }
    }

    public List<Booking> getBookingsByGuest(Guest guest) {
        List<Booking> guestBookings = new ArrayList<>();
        for (Booking booking : activeBookings) {
            if (booking.getGuest().equals(guest)) {
                guestBookings.add(booking);
            }
        }
        return guestBookings;
    }

    public List<Booking> getWaitingList() {
        List<Booking> allWaitingBookings = new ArrayList<>();
        for (Room room : rooms) {
            allWaitingBookings.addAll(waitingListService.getWaitingListForRoom(room));
        }
        return allWaitingBookings;
    }

    public void processWaitingList() {
        for (Room room : rooms) {
            waitingListService.processWaitingList(room);
        }
    }

    public void cancelBooking(Booking booking) {
        activeBookings.remove(booking);
        System.out.println("Booking cancelled for Room " + booking.getRoom().getRoomNumber());
        
        // Process waiting list after cancellation
        waitingListService.processWaitingList(booking.getRoom());
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

    public List<Booking> getWaitingListForGuest(Guest guest) {
        return waitingListService.getWaitingListForGuest(guest);
    }

    public WaitingListService getWaitingListService() {
        return waitingListService;
    }
}
