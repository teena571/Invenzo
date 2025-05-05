package service;

import CustomDataStructures.GuestBST;

public class GuestService {
    private GuestBST guestTree;

    public GuestService() {
        guestTree = new GuestBST();
    }

    public void registerGuest(String name, String id, String phone, int roomNumber) {
        guestTree.insert(name, id, phone, roomNumber);
        System.out.println("Guest registered: " + name);
    }

    public void searchGuestByName(String name) {
        GuestBST.GuestNode guestNode = guestTree.search(name);
        if (guestNode != null) {
            System.out.println("Guest found:");
            System.out.println("  Name  : " + guestNode.getGuestName());
            System.out.println("  ID    : " + guestNode.getGuestID());
            System.out.println("  Phone : " + guestNode.getPhoneNumber());
            System.out.println("  Room  : " + guestNode.getRoomNumber());
        } else {
            System.out.println("Guest not found.");
        }
    }

    public void displayAllGuests() {
        System.out.println("Guest List (Inorder Traversal):");
        guestTree.inorderTraversal();
    }
}
