package CustomDataStructures;

import models.Booking;
import java.util.List;
import java.util.ArrayList;

//The bookingQueue uses a queue data structure to manage bookings in a FIFO.
// This helps in managing requests in the order they arrive.
// Managing Booking requests during peak time.
public class BookingQueue {
    private Node front;
    private Node rear;
    private int size;

    private class Node {
        Booking data;
        Node next;

        Node(Booking data){
            this.data=data;
            this.next=null;
        }
    }

    public BookingQueue(){
        front=null;
        rear=null;
        size=0;
    }

    public void enqueue(Booking booking){
        Node newNode=new Node(booking);
        if(rear == null){
            front=newNode;
            rear=newNode;
        }else {
            rear.next=newNode;
            rear=newNode;
        }
        size++;
    }

    public Booking dequeue(){
        if(isEmpty()){
            throw new IllegalStateException("Queue is empty");
        }
        Booking booking=front.data;
        front=front.next;
        size--;
        if(front == null) {
            rear=null;
        }
        return booking;
    }

    //Peek at the front booking without removing it.
    public Booking peek(){
        if(isEmpty()){
            throw new IllegalStateException("Queue is empty.");
        }
        return front.data;
    }

    public boolean isEmpty(){
        return front==null;
    }

    public int size(){
        return size;
    }

    public void displayQueue(){
        if(isEmpty()){
            System.out.println("No pending boookings.");
            return;
        }

        System.out.println("Booking Queue: ");
        Node current=front;
        while(current != null){
            System.out.println(current.data);
            current=current.next;
        }
    }

    public List<Booking> getAllBookings(){
        List<Booking> bookings= new ArrayList<>();

        Node current = front;
        while(current != null){
            bookings.add(current.data);
            current=current.next;
        }
        return bookings;
    }

    public void removeBooking(Booking target) {
        if (front == null) return;
    
        if (front.data.equals(target)) {
            front = front.next;
            if (front == null) rear = null;
            return;
        }
    
        Node prev = front;
        Node curr = front.next;
    
        while (curr != null) {
            if (curr.data.equals(target)) {
                prev.next = curr.next;
                if (curr == rear) rear = prev;
                return;
            }
            prev = curr;
            curr = curr.next;
        }
    }
    
}
