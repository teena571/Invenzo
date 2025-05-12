package service;

import models.Guest;
import java.io.*;
import java.util.*;

public class StorageService {
    private static final String GUEST_FILE = "guests.txt";
    private static final String BOOKING_FILE = "bookings.dat";
    private static final Set<Integer> usedGuestIds = new HashSet<>();

    public StorageService() {
        loadGuestIds();
    }

    private void loadGuestIds() {
        try (BufferedReader reader = new BufferedReader(new FileReader(GUEST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        usedGuestIds.add(id);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid guest ID in file: " + parts[0]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, that's okay
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveGuest(Guest guest) {
        List<Guest> guests = loadGuests();
        guests.add(guest);
        usedGuestIds.add(guest.getId());
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(GUEST_FILE))) {
            for (Guest g : guests) {
                writer.println(String.format("%d,%s,%s", 
                    g.getId(), 
                    g.getName(), 
                    g.getContact()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Guest> loadGuests() {
        List<Guest> guests = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(GUEST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        String contact = parts[2].trim();
                        guests.add(new Guest(id, name, contact));
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid guest data in file: " + line);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, return empty list
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return guests;
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