package service;

import models.Room;
import CustomDataStructures.RoomCategory;

import java.util.ArrayList;
import java.util.List;

import CustomDataStructures.CustomLinkedList;

// add room
//display all rooms
//find room by number

public class RoomService {
    private CustomLinkedList<Room> rooms;
    RoomCategory roomCategory = new RoomCategory();

    public RoomService() {
        rooms = new CustomLinkedList<>();
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public List<Room> displayAllRooms() {
        System.out.println("All Rooms:");
        List<Room> allRooms = new ArrayList<>();

        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            allRooms.add(room);
        }

        return allRooms;
    }

    public Room findRoomByType(String roomType) {
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            if (room.getCategory().equalsIgnoreCase(roomType) && !room.isBooked()) {
                return room;
            }
        }
        return null;
    }

    public Room findRoomByNumber(int roomNumber) {

        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }

        return null;
    }

    public void displayRoomCategories() {
        roomCategory.displayCategories();
    }

    public void displayAvailableRoomsByType(String type) {
        System.out.println("\n Available rooms of type '" + type + "':");
        boolean found = false;

        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            if (room.getCategory().equalsIgnoreCase(type) && !room.isBooked()) {
                System.out.println(room);
                found = true;
            }
        }

        if (!found) {
            System.out.println(" No available rooms of this type.");
        }
    }

}
