package CustomDataStructures;

public class CustomLinkedList<T> {
    private Node head;
    private Node tail;
    private int size;

    private class Node {
        T data;
        Node next;

        Node(T data){
            this.data=data;
            this.next=null;
        }
    }

    public CustomLinkedList(){
        head=null;
        tail=null;
        size=0;
    }

    public void add (T data){
        Node newNode = new Node(data);
        if(tail == null){
            head=newNode;
            tail=newNode;
        }else{
            tail.next=newNode;
            tail=newNode;
        }
        size++;
    }

    public void remove(int index){
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException("Index out of range.");
        }

        if(index == 0){
            head=head.next;
        }else{
            Node current=head;
            for(int i=0;i<index-1;i++){
                current=current.next;
            }
            current.next=current.next != null ? current.next.next : null ;
        }
        size--;
    }

    public T get (int index){
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException("Index out of range ");
        }

        Node current = head;
        for(int i=0;i<index;i++){
            current=current.next;
        }

        return current.data;
    }

    public int size(){
        return size;
    }

    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder();
        Node current=head;
        while(current != null){
            sb.append(current.data.toString()).append(" -> ");
            current=current.next;
        }

        return sb.length() > 0 ? sb.substring(0,sb.length()-4) : "Empty List";
    }
}
