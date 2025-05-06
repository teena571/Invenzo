package service;

import CustomDataStructures.GuestBST;
import models.Guest;

// register Guest
// Search Guest By name;

public class GuestService {
    private GuestBST guestTree;

    public GuestService() {
        guestTree = new GuestBST();
    }

    public void registerGuest(String name, int id, String phone, int roomNumber) {
        guestTree.insert(name, id, phone, roomNumber);
        System.out.println("Guest registered: " + name);
    }

    public Guest searchGuestByName(String name) {
        GuestBST.GuestNode guestNode = guestTree.search(name);
        if (guestNode != null) {
            System.out.println("Found Guest: ID=" + guestNode.getGuestID() + ", Name=" + guestNode.getGuestName());
            return new Guest(guestNode.getGuestID(),guestNode.getGuestName(),guestNode.getPhoneNumber());
        } else {
            System.out.println("Guest not found.");
            return null;
        }
    }    
    


    public boolean isGuestRegistered(String name){
        return guestTree.search(name) != null;
    }

    public void displayAllGuests() {
        System.out.println("Guest List (Inorder Traversal):");
        guestTree.inorderTraversal();
    }
}
