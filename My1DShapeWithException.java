// File name: My1DShape.java
// Description: This part of the code responsible for defining the 1DShape class
// Challenges: None
// 
// Time Spent: #
//
// Revision History
// Date: By: Action
// 09/19/2025 Tekhour Khov Created the 1DShape class
// 09/22/2025 Add in exception handling
// 09/24/2025 Have to reseach for a way to make the constructor throw exception properly.

public abstract class My1DShapeWithException extends MyShapeWithException implements Comparable<My1DShapeWithException>{
    private MyPointWithException startPoint; // start point of the object
    private MyPointWithException endPoint; // end point of the object

    // Double check required 10/24/2025
    public My1DShapeWithException(MyPointWithException startPoint, MyPointWithException endPoint, String color)
        throws InvalidCoordinateException{
        //super() // Use defualt color for now. This is not required as constructor of super class will be called automatically

        if (startPoint == null || endPoint == null){
            throw new InvalidCoordinateException("Start point and end point cannot be null.");
        } else if (startPoint.equals(endPoint)){
            throw new InvalidCoordinateException("Start point and end point cannot be the same.");
        } else{
        this.startPoint = startPoint;
        this.endPoint = endPoint;

        //handle color menually if color is provided
        if (color != null){
            String trimmed = color.trim();
            if (!trimmed.isEmpty()){
                setFillColor(trimmed);
        }
        }
    }
    }

    public My1DShapeWithException(MyPointWithException startPoint, MyPointWithException endPoint)
        throws InvalidCoordinateException{
        //super() // Use defualt color for now. This is not required as constructor of super class will be called automatically
        // Constructor without color parameter
        if (startPoint == null || endPoint == null){
            throw new InvalidCoordinateException("Start point and end point cannot be null.");
        } else if (startPoint.equals(endPoint)){
            throw new InvalidCoordinateException("Start point and end point cannot be the same.");
        } else{
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        }
    }

    public abstract double calculateLength();

    public MyPointWithException getStartPoint(){
        return startPoint;
    }
    public MyPointWithException getEndPoint(){
        return endPoint;
    }
    
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        My1DShapeWithException other = (My1DShapeWithException) obj;
        return startPoint.equals(other.startPoint) && endPoint.equals(other.endPoint);
    }

    public int compareTo(My1DShapeWithException other){ //compare length
        return Double.compare(this.calculateLength(), other.calculateLength());
    }

    // Max now throw invalid
    public static My1DShapeWithException max(My1DShapeWithException o1, My1DShapeWithException o2) throws InvalidCoordinateException{ // return longer shape
        if (o1 == null && o2 == null) {
            throw new InvalidCoordinateException("Both shapes cannot be null.");}
        if (o1 == null) return o2;
        if (o2 == null) return o1;
        return o1.compareTo(o2) >= 0 ? o1 : o2;
    }

    @Override
    public String toString(){
        return String.format("One-Dimensional Shape: %s [Color=%s]\n"
                            + "[Start: (%.1f,%.1f), End: (%.1f,%.1f), Length: %.2f]", 
                            getName(), getFillColor(), getStartPoint().getX(), getStartPoint().getY(),
                            getEndPoint().getX(), getEndPoint().getY(), calculateLength());
    }
}
