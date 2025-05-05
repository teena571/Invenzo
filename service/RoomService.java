package service;

import models.Room;
import CustomDataStructures.CustomLinkedList;

public class RoomService {
    private CustomLinkedList<Room> rooms;
    
    public RoomService(){
        rooms = new CustomLinkedList<>();
    }

    public void addRoom(Room room){
        rooms.add(room);
        System.out.println("Room added: "+room);
    }

    public void displayAllRooms(){
        System.out.println("All Rooms: ");
        for(int i=0;i<rooms.size();i++){
            System.out.println(rooms.get(i));
        }
    }

    public Room findRoomByNumber(int roomNumber){
        for(int i=0;i<rooms.size();i++){
            Room room = rooms.get(i);
            if(room.getRoomNumber() == roomNumber){
                return room;
            }
        }
        return null;
    }
}
