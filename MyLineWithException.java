// File name: MyLine.java
// Description: This part of the code responsible for defining the MyLine class
// Challenges: None
// 
// Time Spent: About 15 minutes
//
// Revision History
// Date: By: Action
// 09/19/2025 Created the MyLine class
// 09/22/2025 Invalid coordinate exception handling added
// 12/09/2025 Added toFXShape method for converting to JavaFX shape

public class MyLineWithException extends My1DShapeWithException{
    public MyLineWithException(MyPointWithException startPoint, MyPointWithException endPoint, String color) throws InvalidCoordinateException {
        super(startPoint, endPoint,color);
        if (startPoint == null || endPoint == null){
            throw new InvalidCoordinateException("Start point and end point cannot be null.");
        }
    }

    public MyLineWithException(MyPointWithException startPoint, MyPointWithException endPoint) throws InvalidCoordinateException {
        super(startPoint, endPoint);
        if (startPoint == null || endPoint == null){
            throw new InvalidCoordinateException("Start point and end point cannot be null.");
        }
    }

    public double calculateLength(){
        double deltaX = getEndPoint().getX() - getStartPoint().getX();
        double deltaY = getEndPoint().getY() - getStartPoint().getY();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public String getName(){
        return "Line";
    }

    public String howToColor(){
        return "Color the line with " + getStrokeColor() + " color.";
    }

    @Override
    public String toString(){
        return String.format("One-Dimensional Shape: %s [Color= %s]\n" + //
                        "[Start: (%.1f,%.1f), End: (%.1f,%.1f), Length: %.2f]",
                        getName(), getStrokeColor(),
                        getStartPoint().getX(), getStartPoint().getY(),
                        getEndPoint().getX(), getEndPoint().getY(),
                        calculateLength());
    }

    // javafx start constructor accept start x, start y, end x, end y
    public javafx.scene.shape.Shape toFXShape() {
        javafx.scene.shape.Line line = new javafx.scene.shape.Line();
        if (gridLineWidth > 0) {
            line.setStrokeWidth(gridLineWidth);
        } else if (gridLineWidth <0){
            line.setStrokeWidth(1.0);
        }

        line.setStartX(getStartPoint().getX());
        line.setStartY(getStartPoint().getY());
        line.setEndX(getEndPoint().getX());
        line.setEndY(getEndPoint().getY());
        line.setStroke(javafx.scene.paint.Color.web(getStrokeColor())); // Line can't fill as it is 1D shape
        return line;
    }

}
