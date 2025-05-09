package service;

import models.Guest;
import java.io.*;
import java.util.*;

public class StorageService {
    private static final String GUEST_FILE = "guests.dat";
    private static final String BOOKING_FILE = "bookings.dat";
    private static final Set<Integer> usedGuestIds = new HashSet<>();

    public StorageService() {
        loadGuestIds();
    }

    private void loadGuestIds() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(GUEST_FILE))) {
            List<Guest> guests = (List<Guest>) ois.readObject();
            for (Guest guest : guests) {
                usedGuestIds.add(guest.getId());
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, that's okay
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveGuest(Guest guest) {
        List<Guest> guests = loadGuests();
        guests.add(guest);
        usedGuestIds.add(guest.getId());
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(GUEST_FILE))) {
            oos.writeObject(guests);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Guest> loadGuests() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(GUEST_FILE))) {
            return (List<Guest>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean isGuestIdUsed(int id) {
        return usedGuestIds.contains(id);
    }

    public Guest findGuestById(int id) {
        List<Guest> guests = loadGuests();
        for (Guest guest : guests) {
            if (guest.getId() == id) {
                return guest;
            }
        }
        return null;
    }

    public Guest findGuestByName(String name) {
        List<Guest> guests = loadGuests();
        for (Guest guest : guests) {
            if (guest.getName().equalsIgnoreCase(name)) {
                return guest;
            }
        }
        return null;
    }
} 