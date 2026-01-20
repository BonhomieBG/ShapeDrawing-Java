// File name: InvalidRadiusException.java
// Description: This class represents an exception that is thrown when invalid radius are provided.
// 
// Time Spent: About 10 mins
// Challenges: None
//
// Revision History
// Date: By: Action
// 10/20/2025 Created the InvalidRadiusException class

public class InvalidRadiusException extends Exception {
    private double invalidRadius;

    public InvalidRadiusException() {
        super("Radius must be positive.");
    }
    public InvalidRadiusException(String message, double invalidRadius) {
        super(message);
        this.invalidRadius = invalidRadius;
    }
    public InvalidRadiusException(String message) {
        super(message);
    }

    public double getInvalidRadius() {
        return invalidRadius;
    }

    @Override
    public String toString() {
        return super.toString() + " (Invalid Radius: " + invalidRadius + ")";
    }
}