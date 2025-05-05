package mainF;

import java.util.Scanner;

import models.Guest;
import models.Room;
import service.BookingService;
import service.GuestService;
import service.RoomService;

public class Main {
    public static void main(String[] args) {
        BookingService bookingService = new BookingService();
        RoomService roomService = new RoomService();
        GuestService guestService = new GuestService();

        System.out.println("\n\n--------- WELCOME TO INVENZO ---------\n");

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Select an Option: ");
            System.out.println("1. View Room Types and Pricing");
            System.out.println("2. Check Room Availability by Type");
            System.out.println("3. Book a Room");
            System.out.println("4. Search Guest by Name / Register New Guest");
            System.out.println("5. Exit");
            System.out.print("Your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {
                case 1:
                    // Show room types and pricing
                    System.out.println("\n📋 Available Room Types and Pricing:");
                    roomService.displayAllRooms();
                    break;

                case 2:
                    // Check availability of rooms
                    System.out.println("Enter Room Type to check availability: ");
                    String roomType = sc.nextLine();
                    boolean isAvailable = roomService.checkRoomAvailability(roomType);
                    if (isAvailable) {
                        System.out.println("✅ Rooms of type " + roomType + " are available.");
                    } else {
                        System.out.println("❌ No rooms of type " + roomType + " are available.");
                    }
                    break;

                case 3:
                    // Book a room based on room type
                    System.out.println("Enter Guest Name: ");
                    String guestName = sc.nextLine();

                    if (!guestService.isGuestRegistered(guestName)) {
                        System.out.println("❌ Guest not found in the system.");
                        System.out.println("👉 Please register first by selecting Option 4 from the main menu.");
                        break;
                    }

                    System.out.println("Enter Desired Room Type (Sea View / Deluxe / Standard): ");
                    String desiredRoomType = sc.nextLine();

                    Room availableRoom = roomService.getAvailableRoomByType(desiredRoomType);
                    if (availableRoom != null) {
                        boolean bookingStatus = bookingService.bookRoom(availableRoom.getRoomNumber(), guestName);
                        if (bookingStatus) {
                            System.out.println("✅ Room " + availableRoom.getRoomNumber() + " (" + desiredRoomType + ") successfully booked for " + guestName + ".");
                        } else {
                            System.out.println("❌ Failed to book room. Please try again.");
                        }
                    } else {
                        System.out.println("⚠️ No available rooms of type '" + desiredRoomType + "'.");
                    }
                    break;

                case 4:
                    // Register new guest or search guest
                    System.out.println("Enter Guest Name: ");
                    String name = sc.nextLine();
                    Guest foundGuest = guestService.searchGuestByName(name);
                    if (foundGuest != null) {
                        System.out.println("✅ Guest Found: " + foundGuest.getName());
                    } else {
                        System.out.println("Guest not found. Proceeding to registration...");
                        System.out.print("Enter Guest Contact: ");
                        String contact = sc.nextLine();
                        boolean registered = guestService.registerGuest(name, contact);
                        if (registered) {
                            System.out.println("✅ Guest registered successfully.");
                        } else {
                            System.out.println("❌ Registration failed.");
                        }
                    }
                    break;

                case 5:
                    System.out.println("Exiting... Thank you for using Invenzo!");
                    sc.close();
                    return;

                default:
                    System.out.println("❌ Invalid choice. Please select a valid option.");
            }

            System.out.println("\n-----------------------------\n");

        } while (true);
    }
}
