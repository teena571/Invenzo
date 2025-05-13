package service;

import models.Guest;
import models.Room;
import models.Booking;
import java.time.LocalDate;
import java.util.*;
import java.io.*;

public class WaitingListService {
    private Map<Room, Queue<Booking>> waitingList;
    private BookingService bookingService;
    private static final String WAITING_LIST_FILE = "waiting_list.dat";

    public WaitingListService(BookingService bookingService) {
        this.waitingList = new HashMap<>();
        this.bookingService = bookingService;
        loadWaitingList();
    }

    private void loadWaitingList() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(WAITING_LIST_FILE))) {
            waitingList = (Map<Room, Queue<Booking>>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, that's okay
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveWaitingList() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(WAITING_LIST_FILE))) {
            oos.writeObject(waitingList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToWaitingList(Booking booking) {
        Room room = booking.getRoom();
        waitingList.computeIfAbsent(room, k -> new LinkedList<>()).add(booking);
        booking.setWaiting(true);
        saveWaitingList();
        System.out.println("Added booking to waiting list for Room " + room.getRoomNumber());
    }

    public void processWaitingList(Room room) {
        Queue<Booking> queue = waitingList.get(room);
        if (queue != null && !queue.isEmpty()) {
            Booking nextBooking = queue.peek();
            if (bookingService.isRoomAvailable(room, nextBooking.getCheckInDate(), nextBooking.getCheckOutDate())) {
                nextBooking = queue.poll();
                nextBooking.setWaiting(false);
                bookingService.createBooking(nextBooking.getGuest(), room, 
                    nextBooking.getCheckInDate(), nextBooking.getCheckOutDate());
                saveWaitingList();
                System.out.println("Processed waiting list booking for Room " + room.getRoomNumber());
            }
        }
    }

    public List<Booking> getWaitingListForRoom(Room room) {
        Queue<Booking> queue = waitingList.get(room);
        return queue != null ? new ArrayList<>(queue) : new ArrayList<>();
    }

    public List<Booking> getWaitingListForGuest(Guest guest) {
        List<Booking> guestWaitingList = new ArrayList<>();
        for (Queue<Booking> queue : waitingList.values()) {
            for (Booking booking : queue) {
                if (booking.getGuest().equals(guest)) {
                    guestWaitingList.add(booking);
                }
            }
        }
        return guestWaitingList;
    }

    public void removeFromWaitingList(Booking booking) {
        Queue<Booking> queue = waitingList.get(booking.getRoom());
        if (queue != null) {
            queue.remove(booking);
            saveWaitingList();
            System.out.println("Removed booking from waiting list for Room " + booking.getRoom().getRoomNumber());
        }
    }

    public List<Booking> getAllWaitingListBookings() {
        List<Booking> allBookings = new ArrayList<>();
        for (Queue<Booking> queue : waitingList.values()) {
            allBookings.addAll(queue);
        }
        return allBookings;
    }
} 