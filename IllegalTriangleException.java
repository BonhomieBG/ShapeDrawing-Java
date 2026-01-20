// File name: IllegalTriangleException.java
// Description: This class represents an exception that is thrown when an illegal triangle is formed.
// 
// Time Spent: About 10 mins
// Challenges: None
//
// Revision History
// Date: By: Action
// 10/20/2025 Created the InvalidTriangleException class
//

public class IllegalTriangleException extends Exception {
    private double sideA, sideB, sideC;

    public IllegalTriangleException(String message, double sideA, double sideB, double sideC) {
        super(message);
        this.sideA = sideA;
        this.sideB = sideB;
        this.sideC = sideC;
    }
    public IllegalTriangleException(String message) {
        super(message);
    }

    public IllegalTriangleException() {
        super("The provided sides can't form a triangle.");
    }

    public double getSideA() {
        return sideA;
    }

    public double getSideB() {
        return sideB;
    }

    public double getSideC() {
        return sideC;
    }

    @Override
    public String toString() {
        return super.toString() + " (Sides: a=" + sideA + ", b=" + sideB + ", c=" + sideC + ")";
    }
}