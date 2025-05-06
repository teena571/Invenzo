package CustomDataStructures;

import java.util.ArrayList;
import java.util.List;

//add categories
//search categories
//display all categories


public class RoomCategory {
    public static class RoomCategoryNode {
        private String name;
        private int capacity;
        private double basePrice;
        private List<RoomCategoryNode> children;

        public RoomCategoryNode(String name,int capacity,double basePrice){
            this.name=name;
            this.capacity=capacity;
            this.basePrice=basePrice;
            this.children=new ArrayList<>();
        }

        public String getName(){
            return name;
        }

        public int getCapacity(){
            return capacity;
        }

        public double getBasePrice(){
            return basePrice;
        }

        public List<RoomCategoryNode> getChildren (){
            return children;
        }

        public void addSubCategory(RoomCategoryNode child){
            children.add(child);
        }
    }

    private RoomCategoryNode root;

    public RoomCategory(){
        root = new RoomCategoryNode("Rooms", 0, 0);
    }

    public RoomCategoryNode getRoot(){
        return root;
    }

    public boolean addCategory(String parentName,String name,int capacity,double price){
        RoomCategoryNode parent = searchCategory(root,parentName);

        if(parent != null) {
            parent.addSubCategory(new RoomCategoryNode(name, capacity, price));
            return true;
        }
        return false;
    }

    //rec func
    public RoomCategoryNode searchCategory(RoomCategoryNode node,String name){
        if(node == null) return null;
        if(node.getName().equalsIgnoreCase(name)) return node;

        for(RoomCategoryNode child : node.getChildren()){
            RoomCategoryNode found = searchCategory(child, name);
            if(found != null) return found;
        }

        return null;
    }

    public void displayHierarchy(){
        displayHierarchyHelper(root,0);
    }

    private void displayHierarchyHelper(RoomCategoryNode node,int level){
        if(node == null) return;
        System.out.println("  ".repeat(level) + "- " + node.getName() +
            " (Capacity: " + node.getCapacity() + ", Price: ₹" + node.getBasePrice() + ")");
        for (RoomCategoryNode child : node.getChildren()) {
            displayHierarchyHelper(child, level + 1);
        }
    }
}
