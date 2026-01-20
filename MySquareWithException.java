// File name: MySquare.java
// Description: This part of the code responsible for defining the MySquare class
// Challenges: None
// 
// Time Spent: About 30 mins
//
// Revision History
// Date: By: Action
// 09/19/2025 Created the MySquare class
// 10/26/2025 Add exception handling to the constructor
// 12/09/2025 Add toFXShape for converting to JavaFX shape

public class MySquareWithException extends My2DShapeWithException{
    private double side;

    public MySquareWithException(MyPointWithException topleft, double side, String color) throws InvalidCoordinateException, IllegalArgumentException{
        super(topleft, new MyPointWithException(topleft.getX()+side, topleft.getY()+side), color);
        setSide(side);
    }

    public MySquareWithException(MyPointWithException topleft, double side) throws InvalidCoordinateException, IllegalArgumentException{
        super(topleft, new MyPointWithException(topleft.getX()+side, topleft.getY()+side));
        setSide(side);
    }
    
    public MySquareWithException(MyPointWithException topleft, MyPointWithException bottomRight, String color) throws InvalidCoordinateException, IllegalArgumentException{
        super(topleft,bottomRight, color);
        if (topleft == null || bottomRight == null){
            throw new InvalidCoordinateException("Top left and bottom right points cannot be null.");
        }
        double height= Math.abs(bottomRight.getX()-topleft.getX());
        double wide = Math.abs(bottomRight.getY()-topleft.getY());
        this.side= Math.min(wide,height);
    }

    public MySquareWithException(MyPointWithException topleft, MyPointWithException bottomRight) throws InvalidCoordinateException, IllegalArgumentException{
        super(topleft,bottomRight);
        if (topleft == null || bottomRight == null){
            throw new InvalidCoordinateException("Top left and bottom right points cannot be null.");
        }
        double height= Math.abs(bottomRight.getX()-topleft.getX());
        double wide = Math.abs(bottomRight.getY()-topleft.getY());
        this.side= Math.min(wide,height);
    }

    // Added Missing Setter

    public void setSide(double side) throws IllegalArgumentException{
        if (side <0){
            throw new IllegalArgumentException("Side must be a valid number > 0.");
        }
        if (Double.isNaN(side)|| !Double.isFinite(side)){
            throw new IllegalArgumentException("Side must be a valid numbers");
        }
        this.side=side;
    }

    public double calculateArea(){
        return side * side;
    }
    public double calculatePerimeter(){
        return side*4;
    }
    public String getName(){
        return "Square";
    }
    public double getSide(){
        return side;
    }
    public String howToDraw(){
        return String.format("Side= %.1f", side);
    }
    
    @Override
    public String howToColor(){
        if (super.isFilled()==true){
            return "Fill the square with " + super.getFillColor() + " color.";
        } else{
            return "Outline the square with " + super.getFillColor() + " color.";
        }
    }

    @Override
    public String toString(){
        return String.format("Two Dimensional Shape: %s: [Color= %s] \n"+
                            "[Filled=%s]\n"+
                            "[Area: %.2f, Perimeter: %.2f]\n"+
                            "Side: %.1f\n"+
                            "%s\n%s", getName(), getFillColor(), isFilled(), calculateArea(), calculatePerimeter(),
                            getSide(),
                            howToDraw(), howToColor());
    }

    // Square is made of 4 side. Each side is equal so height and width of rectangle can be put at the same length to make a square.
    public javafx.scene.shape.Rectangle toFXShape() {
        javafx.scene.shape.Rectangle rectangle = new javafx.scene.shape.Rectangle();

        if (gridLineWidth > 0) {
            rectangle.setStrokeWidth(gridLineWidth);
        } else if (gridLineWidth <0){
            rectangle.setStrokeWidth(1.0);
        }

        rectangle.setX(getTopLeft().getX());
        rectangle.setY(getTopLeft().getY());
        rectangle.setWidth(getSide());
        rectangle.setHeight(getSide());
        // Set fill color based on the shape's fill status and color
        if (isFilled()){
            // Use gradient if enabled, otherwise solid color
            if (ColorPicker.isUseGradient()) {
                rectangle.setFill(ColorPicker.createGradient());
            } else {
                rectangle.setFill(javafx.scene.paint.Color.web(getFillColor()));
            }
        } else {
            rectangle.setStroke(javafx.scene.paint.Color.web(getStrokeColor()));
            rectangle.setFill(javafx.scene.paint.Color.TRANSPARENT);
        }
        return rectangle;
    }

}