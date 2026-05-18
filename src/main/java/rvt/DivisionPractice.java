package rvt;

import java.util.Scanner;

public class DivisionPractice {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueLoop = true;

        while (continueLoop) {
            System.out.print("Enter the numerator: ");
            String numeratorInput = scanner.nextLine();


            if (!numeratorInput.isEmpty() && (numeratorInput.charAt(0) == 'q' || numeratorInput.charAt(0) == 'Q')) {
                continueLoop = false;
                break;
            }


            int numerator;
            try {
                numerator = Integer.parseInt(numeratorInput);
            } catch (NumberFormatException e) {
                System.out.println("You entered bad data.");
                System.out.println("Please try again.\n");
                continue;
            }

            System.out.print("Enter the divisor: ");
            String divisorInput = scanner.nextLine();


            int divisor;
            try {
                divisor = Integer.parseInt(divisorInput);
            } catch (NumberFormatException e) {
                System.out.println("You entered bad data.");
                System.out.println("Please try again.\n");
                continue;
            }


            if (divisor == 0) {
                System.out.println("You can't divide " + numerator + " by 0");
                System.out.println();
                continue;
            }

    
            int result = numerator / divisor;
            System.out.println(numerator + " / " + divisor + " is " + result);
            System.out.println();
        }

        scanner.close();
    }
}
