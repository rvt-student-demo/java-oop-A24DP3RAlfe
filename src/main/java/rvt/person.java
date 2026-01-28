package rvt;

public class person {
    public String name;
    public String address;
    public int pk;
    public String email;

    public person(String name, String address) {
        this.name = name;
        this.address = address;
    }
    public String getName() {
        return this.name;
    }
    public String getAddress() {
        return this.address;
    }

    @Override
    public String toString() {
        return "Name: " + this.name + "\nAddress: " + this.address;
    }

    public static void main(String[] args) {
        person p = new person("John Doe", "123 Main St");
        person q = new person("Jane Smith", "456 Oak Ave");
        
        System.out.println(p);
        System.out.println(q);
    }
    
}

