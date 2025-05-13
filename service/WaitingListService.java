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
        File file = new File(WAITING_LIST_FILE);
        if (!file.exists()) {
            // Create empty waiting list if file doesn't exist
            waitingList = new HashMap<>();
            saveWaitingList();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                waitingList = (Map<Room, Queue<Booking>>) obj;
            } else {
                waitingList = new HashMap<>();
            }
        } catch (FileNotFoundException e) {
            waitingList = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading waiting list: " + e.getMessage());
            waitingList = new HashMap<>();
        }
    }

    private void saveWaitingList() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(WAITING_LIST_FILE))) {
            oos.writeObject(waitingList);
        } catch (IOException e) {
            System.err.println("Error saving waiting list: " + e.getMessage());
        }
    }

    public void addToWaitingList(Booking booking) {
        if (booking == null || booking.getRoom() == null) {
            System.err.println("Invalid booking or room");
            return;
        }
        Room room = booking.getRoom();
        waitingList.computeIfAbsent(room, k -> new LinkedList<>()).add(booking);
        booking.setWaiting(true);
        saveWaitingList();
        System.out.println("Added booking to waiting list for Room " + room.getRoomNumber());
    }

    public void processWaitingList(Room room) {
        if (room == null) {
            System.err.println("Invalid room");
            return;
        }
        Queue<Booking> queue = waitingList.get(room);
        if (queue != null && !queue.isEmpty()) {
            Booking nextBooking = queue.peek();
            if (nextBooking != null && bookingService.isRoomAvailable(room, nextBooking.getCheckInDate(), nextBooking.getCheckOutDate())) {
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
        if (room == null) {
            return new ArrayList<>();
        }
        Queue<Booking> queue = waitingList.get(room);
        return queue != null ? new ArrayList<>(queue) : new ArrayList<>();
    }

    public List<Booking> getWaitingListForGuest(Guest guest) {
        if (guest == null) {
            return new ArrayList<>();
        }
        List<Booking> guestWaitingList = new ArrayList<>();
        for (Queue<Booking> queue : waitingList.values()) {
            for (Booking booking : queue) {
                if (booking.getGuest() != null && booking.getGuest().equals(guest)) {
                    guestWaitingList.add(booking);
                }
            }
        }
        return guestWaitingList;
    }

    public void removeFromWaitingList(Booking booking) {
        if (booking == null || booking.getRoom() == null) {
            System.err.println("Invalid booking or room");
            return;
        }
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
            if (queue != null) {
                allBookings.addAll(queue);
            }
        }
        return allBookings;
    }
} 