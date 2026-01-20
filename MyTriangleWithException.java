// File name: MyTriangle.java
// Description: This part of the code responsible for defining the MyTriangle class
// Challenges: None
// 
// Time Spent: About 7 hours
//
// Revision History
// Date: By: Action
// 09/19/2025  Created the MyTriangle class
// 09/22/2025  Added methods to calculate area and perimeter
// 09/24/2025  Added exception handling for invalid coordinates and sides
// Add helper methods isValidCoordinate and validateSides

public class MyTriangleWithException extends My2DShapeWithException{
    private MyPointWithException p1, p2, p3;
    private double side1, side2, side3;

    public MyTriangleWithException(MyPointWithException p1, MyPointWithException p2, MyPointWithException p3, String color) throws InvalidCoordinateException, IllegalTriangleException{      
        super(calculateTopLeft(p1, p2, p3), calculateBottomRight(p1, p2, p3), color);

        setP1(p1);
        setP2(p2);
        setP3(p3);
        validateSides(p1.distance(p2), p2.distance(p3), p3.distance(p1));

        //calculate sides
        setSide1(p1.distance(p2));
        setSide2(p2.distance(p3));
        setSide3(p3.distance(p1));
    }

    public MyTriangleWithException(double side1, double side2, double side3, String color) throws InvalidCoordinateException, IllegalTriangleException{
        super(new MyPointWithException(0, 0), new MyPointWithException(side1, (2 * calculateArea(side1, side2, side3)) / side1), color);

        setSide1(side1);
        setSide2(side2);
        setSide3(side3);
        validateSides(side1, side2, side3);

        // Create points
        this.p1 = new MyPointWithException(0, 0);
        this.p2 = new MyPointWithException(side1, 0);
        double height = (2 * calculateArea(side1, side2, side3)) / side1;
        this.p3 = new MyPointWithException(side1 / 2.0, height);
    }

    public MyTriangleWithException(MyPointWithException p1, MyPointWithException p2, MyPointWithException p3) throws InvalidCoordinateException, IllegalTriangleException{      
        super(calculateTopLeft(p1, p2, p3), calculateBottomRight(p1, p2, p3));

        setP1(p1);
        setP2(p2);
        setP3(p3);
        validateSides(p1.distance(p2), p2.distance(p3), p3.distance(p1));

        //calculate sides
        setSide1(p1.distance(p2));
        setSide2(p2.distance(p3));
        setSide3(p3.distance(p1));
    }

    // Need review, added exception handling
    // helper methods
    private static void isValidCoordinate(MyPointWithException p) throws InvalidCoordinateException {
        if (p == null) {
            throw new InvalidCoordinateException("Triangle draw points can't be null or invalid");
        }
        if (Double.isNaN(p.getX()) || Double.isNaN(p.getY()) || !Double.isFinite(p.getX()) || !Double.isFinite(p.getY())){
            throw new InvalidCoordinateException("Triangle draw points must be valid numbers and not infinite.");
        }
    }

    private static void validateSides(double side1, double side2, double side3) throws InvalidCoordinateException, IllegalTriangleException{
        if (!Double.isFinite(side1)|| !Double.isFinite(side2)|| !Double.isFinite(side3)
            || Double.isNaN(side1)|| Double.isNaN(side2)|| Double.isNaN(side3)){
            throw new InvalidCoordinateException("Triangle sides must be valid numbers and not infinite.");
        }
        if (side1 <=0 || side2 <=0 || side3 <=0){
            throw new InvalidCoordinateException("Triangle sides must be greater than zero.");
        }
        if (!(side1+side2>side3 && side3+side1>side2 && side2+side3>side1)){
            throw new IllegalTriangleException("Triangle sides do not satisfy the triangle inequality.");
        }
    }

    private static MyPointWithException calculateTopLeft(MyPointWithException p1, MyPointWithException p2, MyPointWithException p3) throws InvalidCoordinateException{
    // Use helper method for invalid coordinate instead
    // if (p1 == null || p2 == null || p3== null){
    //     throw new InvalidCoordinateException("Triangle starting points can't be null or invalid");
    // }
    // if (Double.isNaN(p1.getX()) || Double.isNaN(p1.getY()) || !Double.isFinite(p1.getX()) || !Double.isFinite(p1.getY()) ||
    //     Double.isNaN(p2.getX()) || Double.isNaN(p2.getY()) || !Double.isFinite(p2.getX()) || !Double.isFinite(p2.getY()) ||
    //     Double.isNaN(p3.getX()) || Double.isNaN(p3.getY()) || !Double.isFinite(p3.getX()) || !Double.isFinite(p3.getY())){
    //     throw new InvalidCoordinateException("Triangle starting points must be valid numbers and not infinite.");
    // }
    isValidCoordinate(p1);
    isValidCoordinate(p2);
    isValidCoordinate(p3);

    double minX = Math.min(Math.min(p1.getX(), p2.getX()), p3.getX());
    double maxY = Math.max(Math.max(p1.getY(), p2.getY()), p3.getY()); // Changed to maxY
    return new MyPointWithException(minX, maxY);
    }

    private static MyPointWithException calculateBottomRight(MyPointWithException p1, MyPointWithException p2, MyPointWithException p3) throws InvalidCoordinateException{
    //     if (p1 == null || p2 == null || p3== null){
    //     throw new InvalidCoordinateException("Triangle starting points can't be null or invalid");
    // }T(MyPo
    // if (Double.isNaN(p1.getX()) || Double.isNaN(p1.getY()) || !Double.isFinite(p1.getX()) || !Double.isFinite(p1.getY()) ||
    //     Double.isNaN(p2.getX()) || Double.isNaN(p2.getY()) || !Double.isFinite(p2.getX()) || !Double.isFinite(p2.getY()) ||
    //     Double.isNaN(p3.getX()) || Double.isNaN(p3.getY()) || !Double.isFinite(p3.getX()) || !Double.isFinite(p3.getY())){
    //     throw new InvalidCoordinateException("Triangle starting points must be valid numbers and not infinite.");
    // }
    isValidCoordinate(p1);
    isValidCoordinate(p2);
    isValidCoordinate(p3);

    double maxX = Math.max(Math.max(p1.getX(), p2.getX()), p3.getX());
    double minY = Math.min(Math.min(p1.getY(), p2.getY()), p3.getY()); // Changed to minY
    return new MyPointWithException(maxX, minY);
    }
    // End of helper methods

    private static double calculateArea(double side1, double side2, double side3){
    double s = (side1 + side2 + side3) / 2.0;  // semi-perimeter
    return Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));  // Heron's formula
    }

    public double calculateArea(){
        return calculateArea(side1, side2, side3);
    }
    public double calculatePerimeter(){
        return side1 + side2 + side3;
    }
    public String getName(){
        return "Triangle";
    }
    public double getSide1(){
        return side1;
    }
    public double getSide2(){
        return side2;
    }
    public double getSide3(){
        return side3;
    }
    public MyPointWithException getP1(){
        return p1;
    }
    public MyPointWithException getP2(){
        return p2;
    }
    public MyPointWithException getP3(){
        return p3;
    }
    // need review end

    // Added setters method
    public void setSide1(double side1) throws InvalidCoordinateException, IllegalTriangleException{
        if (!Double.isFinite(side1) || Double.isNaN(side1) || side1 <=0){
            throw new InvalidCoordinateException("Triangle sides must be valid numbers and greater than zero.");
        }
        this.side1 = side1;
    }
    public void setSide2(double side2) throws InvalidCoordinateException, IllegalTriangleException{
        if (!Double.isFinite(side2) || Double.isNaN(side2) || side2 <=0){
            throw new InvalidCoordinateException("Triangle sides must be valid numbers and greater than zero.");
        }
        this.side2 = side2;
    }
    public void setSide3(double side3) throws InvalidCoordinateException, IllegalTriangleException{
        if (!Double.isFinite(side3) || Double.isNaN(side3) || side3 <=0){
            throw new InvalidCoordinateException("Triangle sides must be valid numbers and greater than zero.");
        }
        this.side3 = side3;
    }
    public void setP1(MyPointWithException p1) throws InvalidCoordinateException{
        if (p1 == null){
            throw new InvalidCoordinateException("Triangle draw points can't be null or invalid");
        }
        this.p1 = p1;
    }
    public void setP2(MyPointWithException p2) throws InvalidCoordinateException{
        if (p2 == null){
            throw new InvalidCoordinateException("Triangle draw points can't be null or invalid");
        }
        this.p2 = p2;
    }
    public void setP3(MyPointWithException p3) throws InvalidCoordinateException{
        if (p3 == null){
            throw new InvalidCoordinateException("Triangle draw points can't be null or invalid");
        }
        this.p3 = p3;
    }

    public String getCorners(){
        return String.format("P1: (%s), P2: (%s), P3: (%s)", p1, p2, p3);
    }
    public String howToDraw(){
        return String.format("Side1= %.1f, Side2= %.1f, Side3= %.1f", side1, side2, side3);
    }

    @Override
    public String howToColor(){
        if (super.isFilled()==true){
            return "Fill the triangle with " + super.getFillColor() + " color.";
        } else{
            return "Outline the triangle with " + super.getStrokeColor() + " color.";
        }
    }

    @Override
    public String toString(){
        return String.format("Two Dimensional Shape: %s: [Color= %s] \n"+
                        "[Filled=%s]\n"+
                        "[Area: %.2f, Perimeter: %.2f]\n"+
                        "%s\n"+
                        "%s\n"+
                        "%s", getName(), getStrokeColor(), isFilled(), calculateArea(), calculatePerimeter(),
                        getCorners(), howToDraw(), howToColor());
    }

    public javafx.scene.shape.Polygon toFXShape(){
        javafx.scene.shape.Polygon polygon = new javafx.scene.shape.Polygon();
        if (gridLineWidth > 0) {
            polygon.setStrokeWidth(gridLineWidth);
        } else if (gridLineWidth <0){
            polygon.setStrokeWidth(1.0);
        }

        polygon.getPoints().addAll(
            p1.getX(), p1.getY(),
            p2.getX(), p2.getY(),
            p3.getX(), p3.getY()
        );
        if (isFilled()){
            // Use gradient if enabled, otherwise solid color
            if (ColorPicker.isUseGradient()) {
                polygon.setFill(ColorPicker.createGradient());
            } else {
                polygon.setFill(javafx.scene.paint.Color.web(getFillColor()));
            }
        } else {
            polygon.setStroke(javafx.scene.paint.Color.web(getStrokeColor()));
            polygon.setFill(javafx.scene.paint.Color.TRANSPARENT);
        }
        return polygon;
    }
}
