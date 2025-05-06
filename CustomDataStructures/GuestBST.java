package CustomDataStructures;

//Insert a new guest record.   - 
//search for a guest by name.
// display all guest records in alphabetical order.  

public class GuestBST {

    public static class GuestNode {
        String guestName;
        int guestID;
        String phoneNumber;
        int roomNumber;
        GuestNode left,right;
    
        public GuestNode(String guestName,int guestID,String phoneNumber, int roomNumber){
            this.guestName=guestName;
            this.guestID=guestID;
            this.phoneNumber=phoneNumber;
            this.roomNumber=roomNumber;
            this.left=this.right=null;
        }

        public String getGuestName() { return guestName; }
        public int getGuestID() { return guestID; }
        public String getPhoneNumber() { return phoneNumber; }
        public int getRoomNumber() { return roomNumber; }
        
    }

    private GuestNode root;
    
    public void insert(String guestName,int guestID,String phoneNumber,int roomNumber){
        root=insertRec(root, new GuestNode(guestName, guestID, phoneNumber, roomNumber));
    }

    private GuestNode insertRec(GuestNode root,GuestNode newNode){
        if(root == null) return newNode;

        if(newNode.guestName.compareToIgnoreCase(root.guestName) < 0){
            root.left=insertRec(root.left, newNode);
        }else{
            root.right=insertRec(root.right,newNode);
        }
        return root;
    }

    public GuestNode search(String guestName){
        return searchRec(root,guestName);
    }

    private GuestNode searchRec(GuestNode root,String name){
        if(root == null || root.guestName.equalsIgnoreCase(name)) return root;

        if(name.compareToIgnoreCase(root.guestName) < 0){
            return searchRec(root.left, name);
        }else{
            return searchRec(root.right,name);
        }
    }

    public void inorderTraversal(){
        inorderRec(root);
    }

    private void inorderRec(GuestNode root){
        if(root != null) {
            inorderRec(root.left);
            System.out.println("Name: "+root.guestName+" , ID: "+root.guestID+ " ,Phone: "+root.phoneNumber+ " ,Room : "+root.roomNumber);
            inorderRec(root.right);
        }
    }
}
