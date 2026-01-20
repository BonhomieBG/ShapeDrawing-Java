// File name: MyCircle.java
// Description: This part of the code responsible for defining the MyCircle class
// Challenges: None
// 
// Time Spent: About 15 mins
//
// Revision History
// Date: By: Action
// 09/19/2025 Tekhour Khov Created the MyCircle class
// 10/24/2025 Added exception handling for invalid coordinates and radius
// Add color to constructor
// 12/09/2025 Added toFXShape method for JavaFX integration

public class MyCircleWithException extends My2DShapeWithException{
    private MyPointWithException center; // Circle center
    private double radius; // Circle radius

    public MyCircleWithException(MyPointWithException center, double radius, String color) throws InvalidRadiusException, InvalidCoordinateException{
        super(makeTopLeft(center, radius), makeBottomRight(center, radius),color);
        setCenter(center);
        setRadius(radius);
    }

    public MyCircleWithException(MyPointWithException center, double radius) throws InvalidRadiusException, InvalidCoordinateException{
        super(makeTopLeft(center, radius), makeBottomRight(center, radius));
        setCenter(center);
        setRadius(radius);
    }

    public MyCircleWithException(MyPointWithException topLeft, MyPointWithException bottomRight, String color) throws InvalidCoordinateException{
        super(topLeft, bottomRight, color);
        double centerX = (topLeft.getX() + bottomRight.getX()) / 2;
        double centerY = (topLeft.getY() + bottomRight.getY()) / 2;
        setCenter(new MyPointWithException(centerX, centerY));
        // Center is the midpoint of circle
        this.radius = Math.min(Math.abs(bottomRight.getX() - topLeft.getX()), Math.abs(bottomRight.getY() - topLeft.getY())) / 2;
    }

    public MyCircleWithException(MyPointWithException topLeft, MyPointWithException bottomRight) throws InvalidCoordinateException{
        super(topLeft, bottomRight);
        double centerX = (topLeft.getX() + bottomRight.getX()) / 2;
        double centerY = (topLeft.getY() + bottomRight.getY()) / 2;
        setCenter(new MyPointWithException(centerX, centerY));
        // Center is the midpoint of circle
        this.radius = Math.min(Math.abs(bottomRight.getX() - topLeft.getX()), Math.abs(bottomRight.getY() - topLeft.getY())) / 2;
    }

    // helper to calculate top left point with exception handling
    private static MyPointWithException makeTopLeft(MyPointWithException center, double radius) throws InvalidRadiusException, InvalidCoordinateException{
        if (center == null) throw new InvalidCoordinateException("Center cannot be null.");
        if (!Double.isFinite(radius) || radius <= 0) {
            throw new InvalidRadiusException("Radius must be a valid number > 0.", radius);
        }
        return new MyPointWithException(center.getX()-radius, center.getY()-radius);
    }

    private static MyPointWithException makeBottomRight(MyPointWithException center, double radius) throws InvalidRadiusException, InvalidCoordinateException{
        if (center == null) throw new InvalidCoordinateException("Center cannot be null.");
        if (!Double.isFinite(radius) || radius <= 0) {
            throw new InvalidRadiusException("Radius must be a valid number > 0.", radius);
        }
        return new MyPointWithException(center.getX()+radius, center.getY()+radius);
    }
    // end of helper static methods

    public double calculateArea(){
        return Math.PI * radius * radius;
    }

    public double calculatePerimeter(){
        return 2 * Math.PI * radius;
    }

    public double getDiameter(){
        return 2 * radius;
    }

    public String getName(){
        return "Circle";
    }

    public double getRadius(){
        return radius;
    }

    public MyPointWithException getCenter(){
        return center;
    }

    public String howToDraw(){
        return String.format("Center: (%.1f, %.1f), Radius: %.1f", center.getX(), center.getY(), radius);
    }

    // Added Setter
    public void setRadius(double radius) throws InvalidRadiusException, InvalidCoordinateException {
        if (!Double.isFinite(radius) || radius <= 0) {
            throw new InvalidRadiusException("Radius must be a finite number > 0.", radius);
        }
        this.radius = radius;
        updateBoundingBox(); // sync bounding box with new radius
    }

    public void setCenter(MyPointWithException center) throws InvalidCoordinateException {
        if (center == null) throw new InvalidCoordinateException("Center cannot be null.");
        this.center = center;
        updateBoundingBox(); // sync bounding box with new center
    }

    public void setCenterAndRadius(MyPointWithException center, double radius) throws InvalidCoordinateException, InvalidRadiusException {
        if (center == null) throw new InvalidCoordinateException("Center cannot be null.");
        if (!Double.isFinite(radius) || radius <= 0) {
            throw new InvalidRadiusException("Radius must be a finite number > 0.", radius);
        }
        this.center = center;
        this.radius = radius;
        updateBoundingBox();
    }

    // helper to keep superclass bounding box in sync
    private void updateBoundingBox() {
        try {
            MyPointWithException tl = new MyPointWithException(center.getX() - radius, center.getY() - radius);
            MyPointWithException br = new MyPointWithException(center.getX() + radius, center.getY() + radius);
            setBoundingBoxInternal(tl, br); // assumes My2DShapeWithException provides this protected method
        } catch (InvalidCoordinateException ex) {
            throw new IllegalStateException("Failed to update bounding box: " + ex.getMessage(), ex);
        }
    }

    @Override
    public String howToColor(){
        if (super.isFilled()==true){
            return "Fill the circle with " + super.getFillColor() + " color.";
        } else{
            return "Outline the circle with " + super.getStrokeColor() + " color.";
        }
    }

    @Override
    public String toString(){
        return String.format("Two Dimensional Shape: %s: [Color= %s] \n"+
                            "[Filled=%s]\n"+
                            "[Area: %.2f, Perimeter: %.2f]\n"+
                            "Top-left: (%.1f, %.1f), Bottom-right: (%.1f, %.1f)\n"+
                            "%s\n%s", getName(), getFillColor(), isFilled(), calculateArea(), calculatePerimeter(),
                            getTopLeft().getX(), getTopLeft().getY(), getBottomRight().getX(), getBottomRight().getY(),
                            howToDraw(), howToColor());
    }

    // Custom implement of JavaFX for circle to pass directly. No implementation on the constructor accept fill and color
    public javafx.scene.shape.Circle toFXShape() {
        javafx.scene.shape.Circle fxCircle = new javafx.scene.shape.Circle();
        fxCircle.setCenterX(center.getX());
        fxCircle.setCenterY(center.getY());
        fxCircle.setRadius(getRadius());

        if (isFilled()) {
            // Use gradient if enabled, otherwise solid color
            if (ColorPicker.isUseGradient()) {
                fxCircle.setFill(ColorPicker.createGradient());
            } else {
                fxCircle.setFill(javafx.scene.paint.Color.web(getFillColor()));
            }
        } else {
            fxCircle.setStroke(javafx.scene.paint.Color.web(getStrokeColor()));
            fxCircle.setFill(javafx.scene.paint.Color.TRANSPARENT);
        }
        return fxCircle;
    }
}