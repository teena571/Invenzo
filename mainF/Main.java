package mainF;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import CustomDataStructures.GuestBST;
import CustomDataStructures.RoomCategory;
import models.Booking;
import models.Guest;
import models.Room;
import service.BookingService;
import service.GuestService;
import service.RoomService;

public class Main {
    public static void main(String[] args) {
        RoomService roomService = new RoomService();
        GuestService guestService = new GuestService();
        RoomCategory roomCategory=new RoomCategory();

        List<Room> rooms = new ArrayList<>();
        roomService.addRoom(new Room(101, "Sea View"));
        roomService.addRoom(new Room(102, "Deluxe"));
        roomService.addRoom(new Room(103, "Standard"));

        BookingService bookingService = new BookingService(rooms);

        System.out.println("\n\n--------- WELCOME TO INVENZO ---------\n");

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Select an Option: ");
            System.out.println("1. Check Room Availability by Type");
            System.out.println("2. View Room Types and Pricing");
            System.out.println("3. Book a Room");
            System.out.println("4. Search Guest by Name");
            System.out.println("5. Register New Guest");
            System.out.println("6. Display Room Categories");
            System.out.println("7. Exit");
            System.out.print("Your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Room Type to check availability By Type: ");
                    String roomType = sc.nextLine();
                    Room availableRoom = roomService.findRoomByType(roomType);
                    if (availableRoom != null) {
                        System.out.println("Room of type '" + roomType + "' is available.");
                    } else {
                        System.out.println("No rooms of type '" + roomType + "' are available.");
                    }
                    break;

                case 2:

                    System.out.println("\n Available Room Types and Pricing:");
                    roomService.displayAllRooms(); // Use the method from RoomService
                    break;

                

                case 3:
                    System.out.print("Enter Desired Room Type (e.g., Sea View, Deluxe, Standard): ");
                    String desiredRoomType = sc.nextLine();
                
                    // Step 1: Search for the room category
                    RoomCategory.RoomCategoryNode categoryNode = roomCategory.searchCategory(roomCategory.getRoot(), desiredRoomType);
                
                    if (categoryNode == null) {
                        System.out.println("⚠️ Room category '" + desiredRoomType + "' not found.");
                        break;
                    }
                
                    // Step 2: Display hierarchy under that category
                    System.out.println("\n📂 Room Hierarchy under '" + desiredRoomType + "':");
                    roomCategory.displayHierarchy(); 
                
                    // Step 3: Ask guest name after displaying available rooms
                    System.out.print("\nEnter Guest Name to proceed with booking: ");
                    String guestName = sc.nextLine();
                
                    if (!guestService.isGuestRegistered(guestName)) {
                        System.out.println("❌ Guest not found in the system.");
                        System.out.println("   Please register first by selecting Option 5 from the main menu.");
                        break;
                    }
                
                    Guest guest = guestService.searchGuestByName(guestName);
                
                    // Step 4: Show this category's booking info (basic simulation)
                    System.out.println("\n📦 Booking Room Type: " + categoryNode.getName());
                    System.out.println("   Capacity: " + categoryNode.getCapacity());
                    System.out.println("   Base Price: ₹" + categoryNode.getBasePrice());
                
                    // Step 5: Simulate booking (you can connect to Room list if needed)
                    System.out.println("Select category (eg. Standard,Deluxe,Sea-View )");
                    String category= sc.nextLine();
                    Booking booking = bookingService.createBooking(guest, new Room(999,category)); // Dummy Room
                
                    if (booking != null) {
                        System.out.println("✅ Room of type '" + categoryNode.getName() + "' booked successfully for " + guestName + ".");
                    } else {
                        System.out.println("❌ Booking failed. Please try again.");
                    }
                    break;
                

                case 4:
                    System.out.print("Enter Guest Name: ");
                    String name = sc.nextLine();
                    guestService.searchGuestByName(name); // Use the search method from GuestService
                    break;

                case 5:
                    System.out.println("Enter Guest Name: ");
                    String newGuestName = sc.nextLine();
                    System.out.println("Enter Guest Contact: ");
                    String newGuestContact = sc.nextLine();
                    System.out.println("Enter Guest ID: ");
                    int newGuestID = sc.nextInt();
                    System.out.println("Enter RoomNumber: ");
                    int roomNumber = sc.nextInt();
                    guestService.registerGuest(newGuestName,newGuestID, newGuestContact , roomNumber); // Use the
                                                                                                       // register
                                                                                                       // method from
                                                                                                       // guestService

                    break;

                case 6:
                    System.out.println("\nRoom Category Hierarchy:");
                    roomCategory.displayHierarchy(); // Use the displayHierarchy method from RoomService
                    break;

                case 7:
                    System.out.println("Exiting... Thank you for using Invenzo!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }

            System.out.println("\n-----------------------------\n");

        } while (true);
    }
}
