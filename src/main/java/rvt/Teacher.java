package rvt;

public class Teacher extends person {
    private int salary;

    public Teacher(String name, String address, int salary) {
        super(name, address);
        this.salary = salary;
    }
    public int getSalary() {
        return this.salary;
    }

    @Override
    public String toString() {
        return super.toString() + "\nSalary: " + this.salary;
    }

    public static void main(String[] args) {
        Teacher p = new Teacher("Alice Johnson", "789 Pine Rd", 50000);
        Teacher q = new Teacher("Bob Brown", "321 Maple St", 60000);

        System.out.println(p);
        System.out.println(q);

        Student s = new Student("Charlie Davis", "654 Cedar Ln");

        int i = 0;
        while(i < 25) {
            s.study();
            i++;
        }
        System.out.println(s);
    }
}
