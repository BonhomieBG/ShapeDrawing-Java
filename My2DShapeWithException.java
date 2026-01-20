// File name: Colorable.java
// Description: This part of the code responsible for defining the 2DShape class
// Challenges: Having issue with equals, compaireTo, and max method
// 
// Time Spent: About 3 hours
//
// Revision History
// Date: By: Action
// 09/19/2025 Tekhour Khov Created the 2DShape class
// 09/21/2025 I have to research about equal (object obj)
// 09/24/2025 Having issue with equals, compaireTo, and max method and fixed in 3 hours.
// 10/24/2025 Added exception handling to the class
public abstract class My2DShapeWithException extends MyShapeWithException implements Comparable<My2DShapeWithException>{
    private MyPointWithException topLeft; //Bounding top-left corner
    private MyPointWithException bottomRight; //Bounding bottom-right corner
    private boolean filled; //Indicates if shape is filled

    public My2DShapeWithException(MyPointWithException topLeft, MyPointWithException bottomRight) throws IllegalArgumentException, InvalidCoordinateException{
        if (topLeft == null || bottomRight == null){
            throw new InvalidCoordinateException("Top-left and bottom-right points cannot be null.");
        }
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public My2DShapeWithException(MyPointWithException topLeft, MyPointWithException bottomRight, String color) throws IllegalArgumentException, InvalidCoordinateException{
        super(color);
        if (topLeft == null || bottomRight == null){
            throw new InvalidCoordinateException("Top-left and bottom-right points cannot be null.");
        }
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    // Helper Methods
    protected void setTopLeftInternal(MyPointWithException topLeft) throws InvalidCoordinateException{
        if (topLeft == null){
            throw new InvalidCoordinateException("Top-left point cannot be null.");
        }
        this.topLeft = topLeft;
    }
    protected void setBottomRightInternal(MyPointWithException bottomRight) throws InvalidCoordinateException{
        if (bottomRight == null){
            throw new InvalidCoordinateException("Bottom-right point cannot be null.");
        }
        this.bottomRight = bottomRight;
    }
    protected void setBoundingBoxInternal(MyPointWithException topLeft, MyPointWithException bottomRight) throws InvalidCoordinateException{
        if (topLeft == null || bottomRight == null){
            throw new InvalidCoordinateException("Bounding points cannot be null.");
        }
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    // Abstract Methods
    public abstract double calculateArea();
    public abstract double calculatePerimeter();
    public abstract String howToDraw();

    // Concrete Methods
    public boolean equals(Object obj){ // This is hard, I have to research about it
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        My2DShapeWithException other = (My2DShapeWithException) obj;
        return Double.compare(this.calculateArea(), other.calculateArea()) == 0 && 
               topLeft.equals(other.topLeft) &&
               bottomRight.equals(other.bottomRight)&&
               filled == other.filled &&
               getFillColor().equals(other.getFillColor());
    }

    public int compareTo(My2DShapeWithException other){
        return Double.compare(this.calculateArea(), other.calculateArea()); // comparing 2 different shape based on area
    }

    public static My2DShapeWithException max(My2DShapeWithException o1, My2DShapeWithException o2){
        if (o1 == null && o2 == null) return null;
        if (o1 == null) return o2;
        if (o2 == null) return o1;
        
        return o1.compareTo(o2) >= 0 ? o1 : o2;
    }

    public String getCorner(){
        return String.format("Top-Left: %s, Bottom-Right: %s", topLeft.toString(), bottomRight.toString());
    }

    public boolean isFilled(){
        return filled;
    }

    public void setFilledStatus(boolean filled) throws IllegalArgumentException{
        try {
            this.filled = filled;
            if (filled != true && filled != false){
                throw new IllegalArgumentException("Filled must be true or false."); // If filled is invalid
            }
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    public MyPointWithException getTopLeft(){
        return topLeft;
    }

    public MyPointWithException getBottomRight(){
        return bottomRight;
    }

    @Override
    public String toString(){
        return String.format("Two-Dimensional Shape: %s"+ " [Color=%s]\n"
                            +"[Filled= %s]\n"
                            +"[Area: %.2f, Perimeter: %.2f]\n", getName(), getFillColor(), isFilled(), calculateArea(), calculatePerimeter());
    }
}