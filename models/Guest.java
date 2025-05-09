package models;

import java.io.Serializable;

public class Guest implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String contact;

    public Guest(int id, String name, String contact) {
        this.id = id;
        this.name = name;
        this.contact = contact;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getContact() { return contact; }

    @Override
    public String toString() {
        return "Guest [ID=" + id + ", Name=" + name + ", Contact=" + contact + "]";
    }
}