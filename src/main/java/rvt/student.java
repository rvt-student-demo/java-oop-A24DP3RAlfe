package rvt;

class Student extends person {
    private int credits;

    public Student() {
        super("", "");
        this.credits = 0;
    }

    public Student(String name, String address) {
        super(name, address);
        this.credits = 0;
    }

    public void study() {
        this.credits++;
    }

    public int credits() {
        return this.credits;
    }

    @Override
    public String toString() {
        return super.toString() + "\nCredits: " + this.credits;
    }
    
}
