// File name: InvalidCoordinateException.java
// Description: This class represents an exception that is thrown when invalid coordinates are provided.
// 
// Time Spent: About 10 mins
// Challenges: None
//
// Revision History
// Date: By: Action
// 10/20/2025 Created the InvalidCoordinateException class
//
public class InvalidCoordinateException extends Exception {
    private double invalidX, invalidY;

    public InvalidCoordinateException(String message, double invalidX, double invalidY) {
        super(message);
        this.invalidX = invalidX;
        this.invalidY = invalidY;
    }
    public InvalidCoordinateException(String message) {
        super(message);
    }

    public double getInvalidX() {
        return invalidX;
    }

    public double getInvalidY() {
        return invalidY;
    }

    @Override
    public String toString() {
        return super.toString() + " (Invalid Coordinates: x=" + invalidX + ", y=" + invalidY + ")";
    }
}