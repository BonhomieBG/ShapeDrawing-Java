// File name: MyRectangle.java
// Description: This part of the code responsible for defining the MyRectangle class
// Challenges: None
// 
// Time Spent: About 30 mins
//
// Revision History
// Date: By: Action
// 09/19/2025 Created the MyRectangle class
// 10/26/2025 Add exception handling to the constructor

public class MyRectangleWithException extends My2DShapeWithException{
    private double width; 
    private double height;

    public MyRectangleWithException(MyPointWithException topLeft, double width, double height) throws InvalidCoordinateException, IllegalArgumentException{
        super(topLeft, new MyPointWithException(topLeft.getX()+width, topLeft.getY()+height));
        setWidth(width);
        setHeight(height);
    }

    public MyRectangleWithException(MyPointWithException topLeft, MyPointWithException bottomRight) throws InvalidCoordinateException, IllegalArgumentException{
        super(topLeft, bottomRight);
        this.width = Math.abs(bottomRight.getX()-topLeft.getX());
        this.height = Math.abs(bottomRight.getY()-topLeft.getY());
    }

    public MyRectangleWithException(MyPointWithException topLeft, MyPointWithException bottomRight, String color) throws InvalidCoordinateException, IllegalArgumentException{
        super(topLeft, bottomRight);
        this.width = Math.abs(bottomRight.getX()-topLeft.getX());
        this.height = Math.abs(bottomRight.getY()-topLeft.getY());
        super.setFillColor(color);
    }

    public double calculateArea(){
        return width*height;
    }

    public double calculatePerimeter(){
        return 2*(width+height);
    }

    public double getHeight(){
        return height;
    }

    public double getWidth(){
        return width;
    }

    // Added setter

    public void setHeight(double height) throws IllegalArgumentException{
        if (height <0){
            throw new IllegalArgumentException("Height can't be negative number.");
        } else if (!Double.isFinite(height)){
            throw new IllegalArgumentException("Height must be a valid number.");
        }
        this.height = height;
    }

    public void setWidth(double width) throws IllegalArgumentException{
        if (width <0){
            throw new IllegalArgumentException("Width can't be negative number.");
        } else if (!Double.isFinite(width)){
            throw new IllegalArgumentException("Width must be a valid number.");
        }
        this.width = width;
    }

    public String getName(){
        return "Rectangle";
    }

    public String howToDraw(){
        return String.format("Width= %.1f, Height= %.1f", width,height);
    }

    @Override
    public String howToColor(){
        if (super.isFilled()==true){
            return "Fill the rectangle with " + super.getFillColor() + " color.";
        } else{
            return "Outline the rectangle with " + super.getStrokeColor() + " color.";
        }
    }

    @Override
    public String toString(){
        return String.format("Two Dimensional Shape: %s: [Color= %s] \n"+
                            "[Filled=%s]\n"+
                            "[Area: %.2f, Perimeter: %.2f]\n"+
                            "%s\n"
                            +"%s", 
                            getName(), getStrokeColor(), isFilled(), calculateArea(), calculatePerimeter(),
                            howToDraw(), howToColor());
    }

    public javafx.scene.shape.Shape toFXShape(){
        javafx.scene.shape.Rectangle fxRectangle = new javafx.scene.shape.Rectangle();
        if (gridLineWidth > 0) {
            fxRectangle.setStrokeWidth(gridLineWidth);
        } else if (gridLineWidth <0){
            fxRectangle.setStrokeWidth(1.0);
        }
        fxRectangle.setX(getTopLeft().getX());
        fxRectangle.setY(getTopLeft().getY());
        fxRectangle.setWidth(getWidth());
        fxRectangle.setHeight(getHeight());
        
        if (isFilled()){
            // Use gradient if enabled, otherwise solid color
            if (ColorPicker.isUseGradient()) {
                fxRectangle.setFill(ColorPicker.createGradient());
            } else {
                fxRectangle.setFill(javafx.scene.paint.Color.web(getFillColor()));
            }
        } else{
            fxRectangle.setStroke(javafx.scene.paint.Color.web(getStrokeColor()));
            fxRectangle.setFill(javafx.scene.paint.Color.TRANSPARENT);
        }
        return fxRectangle;
    }
}