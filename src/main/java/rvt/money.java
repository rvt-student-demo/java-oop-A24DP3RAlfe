package rvt;

public class money {
    private final int euros;
    private final int cents;

    public money(int euros, int cents) {
        if (cents > 99) {
            euros = euros + cents / 100;
            cents= cents % 100;
        
        }

        this.euros = euros;
        this.cents = cents;
    }
    public int euros() {
        return this.euros;
    }
    public int cents() {
        return this.cents;
    }

    // Part 1
    public  money plus(money addition) {
        int newEuros = this.euros + addition.euros;
        int newCents = this.cents + addition.cents;
        return new money(newEuros, newCents);
    }

    // Part 2
    public boolean less(money compared) {
        if (this.euros < compared.euros) {
            return true;
        }
        if (this.euros == compared.euros && this.cents < compared.cents) {
            return true;
        }
        return false;
    }

    // Part 3 

    public money minus(money decreaser) {
        int totalCentsFirst = this.euros * 100 + this.cents;
        int totalCentsSecond = decreaser.euros * 100 + decreaser.cents;

        int diffCents = totalCentsFirst - totalCentsSecond;

        if (diffCents < 0) {
            return new money(0, 0);
        }

        int resultEuros = diffCents / 100;
        int resultCents = diffCents % 100;
        return new money(resultEuros, resultCents);
    }
    public String toString() {
        String zero = "";
        if (this.cents < 10) {
            zero = "0";
        }
        return this.euros + "." + zero + this.cents + "e";
    }
    
}
