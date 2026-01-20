// File name: Colorable.java
// Description: This part of the code responsible for defining and implementing MyPoint class
// Challenges: None
// 
// Time Spent: About 3 mins
//
// Revision History
// Date: By: Action
// 09/19/2025 Created the Mypoint class
// 09/22/2025 Added exception handling for invalid coordinates
// 09/24/2025 Recheck the class and error corrections

import java.io.Serializable;

public class MyPointWithException implements Serializable {
    private static final long serialVersionUID = 1L;
    private double x; // x coordinate
    private double y; // y coordinate

    public MyPointWithException(double x, double y) throws InvalidCoordinateException{
        if (Double.isNaN(x) || Double.isNaN(y) || !Double.isFinite(x) || !Double.isFinite(y)){
            throw new InvalidCoordinateException("Coordinates must be valid numbers and not infinite.");
        }
        if (x<0 || y<0){
            throw new InvalidCoordinateException("Coordinates cannot be negative.");
        }
        this.x = x;
        this.y = y;
        
    }

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }

    // Use finate instead of infinate for validation due to it being more comprehensive
    public void setX(double x) throws InvalidCoordinateException{
        if (Double.isNaN(x) || !Double.isFinite(x)){
            throw new InvalidCoordinateException("X coordinate must be a valid number and not infinite.");
        }
        if (x<0){
            throw new InvalidCoordinateException("X coordinate cannot be negative.");
        }
        this.x = x;
    }
    public void setY(double y) throws InvalidCoordinateException{
        if (Double.isNaN(y) || !Double.isFinite(y)){
            throw new InvalidCoordinateException("Y coordinate must be a valid number and not infinite.");
        }
        if (y<0){
            throw new InvalidCoordinateException("Y coordinate cannot be negative.");
        }
        this.y = y;
    }

    public double distance(MyPointWithException other){
        return Math.sqrt(Math.pow((other.x - this.x), 2) + Math.pow((other.y - this.y), 2));
    }

    public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    MyPointWithException other = (MyPointWithException) obj;
    return Double.compare(x, other.x) == 0 && Double.compare(y, other.y) == 0;
    }

    public String toString(){
        return String.format("(%.1f, %.1f)", x, y);
    }
}
