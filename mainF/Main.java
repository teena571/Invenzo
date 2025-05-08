package mainF;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// import CustomDataStructures.GuestBST;
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

        List<Room> rooms = new ArrayList<>();
        roomService.addRoom(new Room(101, "Sea View"));
        roomService.addRoom(new Room(102, "Sea View"));
        roomService.addRoom(new Room(103, "Sea View"));

        roomService.addRoom(new Room(104, "Deluxe"));
        roomService.addRoom(new Room(105, "Deluxe"));
        roomService.addRoom(new Room(106, "Deluxe"));

        roomService.addRoom(new Room(107, "Standard"));
        roomService.addRoom(new Room(107, "Standard"));
        roomService.addRoom(new Room(109, "Standard"));

        RoomCategory roomCategory = new RoomCategory();

        roomCategory.addCategory("Rooms", "Sea View", 6, 5000);
        roomCategory.addCategory("Rooms", "Deluxe", 6, 3500);
        roomCategory.addCategory("Rooms", "Standard", 6, 2500);

        roomCategory.addCategory("Sea View", "101", 2, 5000);
        roomCategory.addCategory("Sea View", "102", 2, 5000);
        roomCategory.addCategory("Sea View", "103", 2, 5000);

        roomCategory.addCategory("Deluxe", "104", 2, 3500);
        roomCategory.addCategory("Deluxe", "105", 2, 3500);
        roomCategory.addCategory("Deluxe", "106", 2, 3500);

        roomCategory.addCategory("Standard", "107", 2, 2500);
        roomCategory.addCategory("Standard", "108", 2, 2500);
        roomCategory.addCategory("Standard", "109", 2, 2500);

        

        BookingService bookingService = new BookingService(rooms);

        System.out.println("\n\n--------- WELCOME TO INVENZO ---------\n");

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Select an Option: ");
            System.out.println("1. Check Room Availability by Type");
            System.out.println("2. View Room Types");
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
                    System.out.print("Enter Room Type to check availability By Type: (eg. Standard, Deluxe, Sea-View): ");
                    String roomType = sc.nextLine();
                    Room availableRoom = roomService.findRoomByType(roomType);
                    if (availableRoom != null) {
                        System.out.println("Room of type '" + roomType + "' is available.");
                    } else {
                        System.out.println("No rooms of type '" + roomType + "' are available.");
                    }
                    break;

                case 2:
                    System.out.println("\n Room Availability List:");
                    List<Room> allRoomList = roomService.displayAllRooms();
                
                    for (Room room : allRoomList) {
                        String status = room.isBooked() ? " Not Available" : " Available";
                        System.out.println("Room No: " + room.getRoomNumber() + " | Type: " + room.getCategory() + " | Status: " + status);
                    }
                    break;
                

                case 3:
                    System.out.print("Enter Desired Room Type (e.g., Sea View, Deluxe, Standard): ");
                    String desiredRoomType = sc.nextLine();

                    RoomCategory.RoomCategoryNode categoryNode = roomCategory.searchCategory(roomCategory.getRoot(),
                            desiredRoomType);

                    if (categoryNode == null) {
                        System.out.println(" Room category '" + desiredRoomType + "' not found.");
                        break;
                    }

                    System.out.println("\n Room Hierarchy under '" + desiredRoomType + "':");
                    roomCategory.displayHierarchy();

                    System.out.print("\nEnter Guest Name to proceed with booking: ");
                    String guestName = sc.nextLine();

                    if (!guestService.isGuestRegistered(guestName)) {
                        System.out.println(" Guest not found in the system.");
                        System.out.println(" Please register first by selecting Option 5 from the main menu.");
                        break;
                    }

                    Guest guest = guestService.searchGuestByName(guestName);

                    System.out.println("\n📦 Booking Room Type: " + categoryNode.getName());
                    System.out.println("   Capacity: " + categoryNode.getCapacity());
                    System.out.println("   Base Price: ₹" + categoryNode.getBasePrice());

                    roomService.displayAvailableRoomsByType(desiredRoomType);

                    System.out.print("Enter Room Number to book: ");
                    int roomNumberToBook = sc.nextInt();
                    sc.nextLine();

                    Room roomToBook = roomService.findRoomByNumber(roomNumberToBook);

                    if (roomToBook == null || !roomToBook.getCategory().equalsIgnoreCase(desiredRoomType)
                            || roomToBook.isBooked()) {
                        System.out.println("❌ Invalid room number or room is already booked.");
                        break;
                    }

                    Booking booking = bookingService.createBooking(guest, roomToBook);
                    if (booking != null) {
                        roomToBook.bookRoom(); // Mark room as booked
                        System.out
                                .println("✅ Room " + roomToBook.getRoomNumber() + " of type '" + roomToBook.getCategory() +
                                        "' booked successfully for " + guestName + ".");
                    } else {
                        System.out.println("❌ Booking failed. Please try again.");
                    }
                    break;

                case 4:
                    System.out.print("Enter Guest Name: ");
                    String name = sc.nextLine();
                    guestService.searchGuestByName(name);
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
                    guestService.registerGuest(newGuestName, newGuestID, newGuestContact, roomNumber);

                    break;

                case 6:
                    System.out.println("\nRoom Category Hierarchy:");
                    roomCategory.displayCategories();
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
