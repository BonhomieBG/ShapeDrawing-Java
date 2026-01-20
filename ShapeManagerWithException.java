// File name: ShapeManager.java
// Description: This part of the code responsible for manaing shapes
// Challenges: Need to learn how to use instaceof and casting
// 
// Time Spent: 3h
//
// Revision History
// Date: By: Action
// 09/19/2025 Created the ShapeManager class
// 09/25/2025 Half way with the class
// 09/27/2025 Completed the class and verify all methods
// 10/26/2025 Verified exception handling implementation working correctly
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ShapeManagerWithException{
    private ArrayList <MyShapeWithException> shapes; // store all shapes
    private Scanner scanner = new Scanner(System.in);

    public ShapeManagerWithException(){
        shapes = new ArrayList<MyShapeWithException>();
    }

    // Add/remove Method
    public void addShape(MyShapeWithException shape){
        shapes.add(shape);
    }

    public boolean removeShape(int index){
        if (index < 0 || index >= shapes.size()){
            return false;
        }
        else{
            shapes.remove(index);
            return true;
        }
    }

    public void removeAllShapes(){
        shapes.clear();
    }
    // Display method
    public void displayAllShapes(){
        if (shapes.isEmpty()){
            System.out.println("No shapes to display. Please Create one.");
            return;
        }
        for (MyShapeWithException shape : shapes){
            System.out.println(shape);
            System.out.println("-------------------------");
        }
    }

    public void displayColors(){
        if (shapes.isEmpty()){
            System.out.println("No shapes to display. Please Create one.");
            return;
        }
        for (MyShapeWithException shape : shapes){
            System.out.print(shape.getName() + " - Color: " + shape.getFillColor());

            // Check if shape is a 2D shape
            if (shape instanceof My2DShapeWithException){
                My2DShapeWithException shape2D = (My2DShapeWithException) shape;
                System.out.println(", Filled: " + shape2D.isFilled());
            } else {
                System.out.println("There is no shape, please create one.");
                System.out.println(); // print newline for 1D shapes
            }
        }
    }

    // Sorting Methods
    public void sort1DShapesByLength(){
        if (shapes.isEmpty()){
            System.out.println("No shapes to sort. Please Create one.");
            return;
        }
        ArrayList<My1DShapeWithException> oneDShapes = new ArrayList<>();
        for (MyShapeWithException shape : shapes){ 
            if (shape instanceof My1DShapeWithException){
                oneDShapes.add((My1DShapeWithException) shape);
            }
        }
        oneDShapes.sort((s1, s2) -> Double.compare(s2.calculateLength(), s1.calculateLength()));
        System.out.println("1D Shapes sorted by length (descending):");
        for (My1DShapeWithException shape : oneDShapes){
            System.out.println(shape.toString());
            System.out.println("--------------------------");
        }
    }

    public void sort2DShapesByArea(){
        if (shapes.isEmpty()){
            System.out.println("No shapes to sort. Please Create one.");
            return;
        }
        ArrayList<My2DShapeWithException> twoDShapes = new ArrayList<>();
        for (MyShapeWithException shape : shapes){
            if (shape instanceof My2DShapeWithException){
                twoDShapes.add((My2DShapeWithException) shape);
            }
        }
        twoDShapes.sort((s1, s2) -> Double.compare(s2.calculateArea(), s1.calculateArea()));
        System.out.println("2D Shapes sorted by area (descending):");
        for (My2DShapeWithException s : twoDShapes) {
            System.out.println(s.toString());
            System.out.println("--------------------------");
        }
    }

    // Find Maximum Methods
    public void findMax1DShape(){
        if (shapes.isEmpty()){
            System.out.println("No shapes to find. Please Create one.");
            return;
        }
        My1DShapeWithException maxShape = null;
        for (MyShapeWithException shape : shapes){
            if (shape instanceof My1DShapeWithException){
                My1DShapeWithException current = (My1DShapeWithException) shape;
                if (maxShape == null || current.calculateLength() > maxShape.calculateLength()){
                    maxShape = current;
                }
            }
        }
        if (maxShape != null){
            System.out.println("Longest 1D Shape: " + maxShape + ", Length: " + maxShape.calculateLength());
        } else {
            System.out.println("No 1D shapes found.");
        }
    }

    public void findMax2DShape(){
        if (shapes.isEmpty()){
            System.out.println("No shapes to find. Please Create one.");
            return;
        }
        My2DShapeWithException maxShape = null;
        for (MyShapeWithException shape : shapes){
            if (shape instanceof My2DShapeWithException){
                My2DShapeWithException current = (My2DShapeWithException) shape;
                if (maxShape == null || current.calculateArea() > maxShape.calculateArea()){
                    maxShape = current;
                }
            }
        }
        if (maxShape != null){
            System.out.println("Largest 2D Shape: " + maxShape + ", Area: " + maxShape.calculateArea());
        } else {
            System.out.println("No 2D shapes found.");
        }
    }

    // Getter Methods
    public ArrayList<MyShapeWithException> getShapes(){
        return shapes;
    }


    // Console Methods
    public void println (String s) {
        System.out.println(s);
    }
    public void print (String s) {
        System.out.print(s);
    }

    public int readInt() throws InputMismatchException {
        if (!scanner.hasNextLine()) throw new InputMismatchException("Input closed");
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) throw new InputMismatchException("No input provided");
        try { return Integer.parseInt(line); }
        catch (NumberFormatException e) { throw new InputMismatchException("Expected an integer"); }
    }

    public double readDouble() throws InputMismatchException {
        if (!scanner.hasNextLine()) throw new InputMismatchException("Input closed");
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) throw new InputMismatchException("No input provided");
        try { return Double.parseDouble(line); }
        catch (NumberFormatException e) { throw new InputMismatchException("Expected a number"); }
    }

    public boolean readBoolean() throws IllegalArgumentException {
        if (!scanner.hasNextLine()) throw new IllegalArgumentException("Input closed");
        String line = scanner.nextLine().trim();
        if (line.equalsIgnoreCase("true")) return true;
        if (line.equalsIgnoreCase("false")) return false;
        throw new IllegalArgumentException("Expected true or false");
    }

    public String readColor() throws IllegalArgumentException {
        if (!scanner.hasNextLine()) return ""; // treat as default
        String color = scanner.nextLine().trim();
        if (color.isEmpty()) return ""; // default allowed
        if (!color.matches("[A-Za-z\\s'-]+")) throw new IllegalArgumentException("Color must contain letters only");
        return color;
    }

        // Add Shape Methods
    public void addLine() {
        println("\n--- Add Line ---");
        try {
            print("Enter start point x coordinate: ");
            double x1 = readDouble();
            print("Enter start point y coordinate: ");
            double y1 = readDouble();

            print("Enter end point x coordinate: ");
            double x2 = readDouble();
            print("Enter end point y coordinate: ");
            double y2 = readDouble();

            print("Enter color (or press Enter for default): ");
            String color = readColor(); // validated

            MyLineWithException line = new MyLineWithException(new MyPointWithException(x1, y1), new MyPointWithException(x2, y2), color);
            addShape(line);
            println("Line added successfully!");
        } catch (InputMismatchException ime) {
            println("Error: " + ime.getClass().getSimpleName());
            println("Message: " + ime.getMessage());
            println("Details: " + ime.toString());
        } catch (InvalidCoordinateException | IllegalArgumentException e) {
            println("Error: " + e.getClass().getSimpleName());
            println("Message: " + e.getMessage());
            println("Details: " + e.toString());
        }
    }

    public void addCircle() {
        println("\n--- Add Circle ---");
        try {
            print("Enter center point x coordinate: ");
            double x = readDouble();
            print("Enter center point y coordinate: ");
            double y = readDouble();
            print("Enter radius: ");
            double radius = readDouble();

            print("Enter color (or press Enter for default): ");
            String color = readColor();

            print("Do you want the circle to be filled? (true/false): ");
            boolean filled = readBoolean();

            MyCircleWithException circle = new MyCircleWithException(new MyPointWithException(x, y), radius, color);
            if (filled) circle.setFilledStatus(true);
            addShape(circle);
            println("Circle added successfully!");
        } catch (InputMismatchException ime) {
            println("Error: " + ime.getClass().getSimpleName());
            println("Message: " + ime.getMessage());
            println("Details: " + ime.toString());
        } catch (InvalidCoordinateException | InvalidRadiusException | IllegalArgumentException e) {
            println("Error: " + e.getClass().getSimpleName());
            println("Message: " + e.getMessage());
            println("Details: " + e.toString());
        }
    }

    public void addRectangle() {
        println("\n--- Add Rectangle ---");
        try {
            print("Enter top-left point x coordinate: ");
            double x1 = readDouble();
            print("Enter top-left point y coordinate: ");
            double y1 = readDouble();
            print("Enter bottom-right point x coordinate: ");
            double x2 = readDouble();
            print("Enter bottom-right point y coordinate: ");
            double y2 = readDouble();

            print("Enter color (or press Enter for default): ");
            String color = readColor();

            print("Do you want the rectangle to be filled? (true/false): ");
            boolean filled = readBoolean();

            MyRectangleWithException rectangle = new MyRectangleWithException(new MyPointWithException(x1, y1), new MyPointWithException(x2, y2));
            if (!color.isEmpty()) rectangle.setFillColor(color);
            if (filled) rectangle.setFilledStatus(true);
            addShape(rectangle);
            println("Rectangle added successfully!");

        } catch (InvalidCoordinateException | IllegalArgumentException | InputMismatchException e) {
            println("Error: " + e.getClass().getSimpleName());
            println("Message: " + e.getMessage());
            println("Details: " + e.toString());
        }
    }

    public void addSquare() {
        println("\n--- Add Square ---");
        try {
            print("Enter top-left point x coordinate: ");
            double x = readDouble();
            print("Enter top-left point y coordinate: ");
            double y = readDouble();
            print("Enter side length: ");
            double side = readDouble();

            print("Enter color (or press Enter for default): ");
            String color = readColor();

            print("Do you want the square to be filled? (true/false): ");
            boolean filled = readBoolean();

            MySquareWithException square = new MySquareWithException(new MyPointWithException(x, y), side, color);
            if (!color.isEmpty()) square.setFillColor(color);
            if (filled) square.setFilledStatus(true);
            addShape(square);
            println("Square added successfully!");
        } catch (InputMismatchException ime) {
            println("Error: " + ime.getClass().getSimpleName());
            println("Message: " + ime.getMessage());
            println("Details: " + ime.toString());
        } catch (InvalidCoordinateException | IllegalArgumentException e) {
            println("Error: " + e.getClass().getSimpleName());
            println("Message: " + e.getMessage());
            println("Details: " + e.toString());
        }
    }

    public void addTriangle(){
        println("\n--- Add Triangle ---");
        try {
            print("Enter first point x coordinate: ");
            double x1 = readDouble();
            print("Enter first point y coordinate: ");
            double y1 = readDouble();

            print("Enter second point x coordinate: ");
            double x2 = readDouble();
            print("Enter second point y coordinate: ");
            double y2 = readDouble();

            print("Enter third point x coordinate: ");
            double x3 = readDouble();
            print("Enter third point y coordinate: ");
            double y3 = readDouble();

            print("Enter color (or press Enter for default): ");
            String color = readColor();

            print("Do you want the triangle to be filled? (true/false): ");
            boolean filled = readBoolean();

            MyTriangleWithException triangle = new MyTriangleWithException(
                new MyPointWithException(x1, y1),
                new MyPointWithException(x2, y2),
                new MyPointWithException(x3, y3),
                color
            );
            if (filled) triangle.setFilledStatus(true);
            addShape(triangle);
            println("Triangle added successfully!");
        } catch (IllegalTriangleException | InvalidCoordinateException | IllegalArgumentException | InputMismatchException e) {
            println("Error: " + e.getClass().getSimpleName());
            println("Message: " + e.getMessage());
            println("Details: " + e.toString());
        }
    }

    public void removeShape() {
        if (getShapes().isEmpty()) {
            println("No shapes available to remove.");
            return;
        }

        println("\n--- Remove Shape ---");
        println("Available shapes:");
        for (int i = 0; i < getShapes().size(); i++) {
            println(i + ": " + getShapes().get(i).getName());
        }

        print("Enter the index of the shape to remove: ");
        try {
            int index = readInt();
            if (removeShape(index)) {
                println("Shape removed successfully!");
            } else {
                println("Invalid index. Shape not removed.");
            }
        } catch (InputMismatchException ime) {
            println("Error: " + ime.getClass().getSimpleName());
            println("Message: " + ime.getMessage());
            println("Details: " + ime.toString());
        }
    }

    //Editor
    public void editShape() {
        if (getShapes().isEmpty()) {
            println("No shapes available to edit.");
            return;
        }
        
        println("\n--- Edit Shape ---");
        println("Available shapes:");
        for (int i = 0; i < getShapes().size(); i++) {
            println(i + ": " + getShapes().get(i).getName());
        }
        
        print("Enter the index of the shape to edit: ");
        int index = scanner.nextInt();
        
        if (index < 0 || index >= getShapes().size()) {
            println("Invalid index.");
            return;
        }

        MyShapeWithException shape = getShapes().get(index);

        println("\nEditing " + shape.getName());
        println("1. Edit Color");

        if (shape instanceof My2DShapeWithException) {
            println("2. Edit Filled Status");
        }
        
        print("Enter your choice: ");
        int editChoice = scanner.nextInt();
        
        switch (editChoice) {
            case 1:
                print("Enter new color: ");
                String newColor = scanner.next();
                shape.setFillColor(newColor);
                println("Color updated successfully!");
                break;
            case 2:
                if (shape instanceof My2DShapeWithException) {
                    My2DShapeWithException shape2D = (My2DShapeWithException) shape;
                    print("Is filled? (true/false): ");
                    boolean filled = scanner.nextBoolean();
                    shape2D.setFilledStatus(filled);
                    println("Filled status updated successfully!");
                } else {
                    println("Invalid choice for 1D shape.");
                }
                break;
            default:
                println("Invalid choice.");
        }
    }
}

