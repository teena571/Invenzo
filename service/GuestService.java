package service;

import models.Guest;
import java.util.List;

// register Guest
// Search Guest By name;

public class GuestService {
    private StorageService storageService;

    public GuestService() {
        storageService = new StorageService();
    }

    public void registerGuest(String name, int id, String contact, int roomNumber) {
        if (storageService.isGuestIdUsed(id)) {
            throw new IllegalArgumentException("Guest ID " + id + " is already in use.");
        }
        
        Guest guest = new Guest(id, name, contact);
        storageService.saveGuest(guest);
        System.out.println("Guest registered: " + name);
    }

    public Guest searchGuestByName(String name) {
        Guest guest = storageService.findGuestByName(name);
        if (guest != null) {
            System.out.println("Found Guest: ID=" + guest.getId() + ", Name=" + guest.getName());
            return guest;
        } else {
            System.out.println("Guest not found.");
            return null;
        }
    }

    public Guest searchGuestById(int id) {
        Guest guest = storageService.findGuestById(id);
        if (guest != null) {
            System.out.println("Found Guest: ID=" + guest.getId() + ", Name=" + guest.getName());
            return guest;
        } else {
            System.out.println("Guest not found.");
            return null;
        }
    }

    public boolean isGuestRegistered(String name) {
        return storageService.findGuestByName(name) != null;
    }

    public boolean isGuestIdRegistered(int id) {
        return storageService.isGuestIdUsed(id);
    }

    public List<Guest> getAllGuests() {
        return storageService.loadGuests();
    }
}
