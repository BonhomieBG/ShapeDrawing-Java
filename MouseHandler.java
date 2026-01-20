// File name: MouseHandler.java
// Description: Handles mouse events for drawing shapes and displaying coordinates
// Challenges: A bit of an issue to try and get the app to capture mouse movements and display coordinates inside the preview box
// 
// Time Spent: #
//
// Revision History
// Date: By: Action
// 12/9/2025 Tekhour Khov Created the MouseHandler class as a part of GUI
// 12/13/2025 Added coordinate preview display functionality
// 12/14/2025 Added multi-click support for shape drawing
// 

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import java.util.ArrayList;

public class MouseHandler implements EventHandler<MouseEvent> {
    private CanvasHelper canvas;
    private Label coordinatePreview;
    private Pane drawingPane;
    private double currentX;
    private double currentY;
    private boolean customDrawingMode = false;
    
    // Track clicked points for multi-click shape drawing
    private ArrayList<MyPointWithException> clickedPoints;
    private ArrayList<javafx.scene.shape.Circle> visualPoints; // Visual indicators for clicked points
    private String currentShapeType; // "LINE", "CIRCLE", "RECTANGLE", "SQUARE", "TRIANGLE", or null
    
    public MouseHandler(CanvasHelper canvas, Label coordinatePreview, Pane drawingPane){
        this.canvas = canvas;
        this.coordinatePreview = coordinatePreview;
        this.drawingPane = drawingPane;
        this.currentX = 0;
        this.currentY = 0;
        this.clickedPoints = new ArrayList<>();
        this.visualPoints = new ArrayList<>();
        this.currentShapeType = null;
    }

    @Override
    public void handle(MouseEvent event){
        // Store current mouse coordinates
        this.currentX = event.getX();
        this.currentY = event.getY();

        if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
            // Update coordinate preview as mouse moves
            String coordinate = String.format("X: %.1f, Y: %.1f", currentX, currentY);
            String area, perimeter, height, width, radius, length, side1, side2, side3;

            if (currentShapeType != null && !clickedPoints.isEmpty()) {
                coordinate += String.format(" | %s: Point %d/%d", 
                    currentShapeType, 
                    clickedPoints.size(), 
                    getRequiredClicks());
            }
            coordinatePreview.setText(coordinate);

            // Display shape properties when hovering over shapes
            if (!canvas.getShapes().isEmpty()){
                for (int i = canvas.getShapes().size() - 1; i >= 0; i--) {
                    MyShapeWithException shape = canvas.getShapes().get(i);
                    
                    if (shape instanceof MyLineWithException) {
                        MyLineWithException line = (MyLineWithException) shape;
                        if (isNearLine(currentX, currentY, line)) {
                            length = String.format("Length: %.2f", line.calculateLength());
                            coordinatePreview.setText(length);
                            break;
                        }
                    } else if (shape instanceof MyCircleWithException) {
                        MyCircleWithException circle = (MyCircleWithException) shape;
                        if (isInsideCircle(currentX, currentY, circle)) {
                            radius = String.format("Radius: %.2f", circle.getRadius());
                            area = String.format("Area: %.2f", circle.calculateArea());
                            perimeter = String.format("Perimeter: %.2f", circle.calculatePerimeter());
                            coordinatePreview.setText(radius + " | " + area + " | " + perimeter);
                            break;
                        }
                    } else if (shape instanceof MySquareWithException) {
                        MySquareWithException square = (MySquareWithException) shape;
                        if (isInsideRectangle(currentX, currentY, square.getTopLeft(), square.getBottomRight())) {
                            length = String.format("Side: %.2f", square.getSide());
                            area = String.format("Area: %.2f", square.calculateArea());
                            perimeter = String.format("Perimeter: %.2f", square.calculatePerimeter());
                            coordinatePreview.setText(length + " | " + area + " | " + perimeter);
                            break;
                        }
                    } else if (shape instanceof MyRectangleWithException) {
                        MyRectangleWithException rectangle = (MyRectangleWithException) shape;
                        if (isInsideRectangle(currentX, currentY, rectangle.getTopLeft(), rectangle.getBottomRight())) {
                            height = String.format("Height: %.2f", rectangle.getHeight());
                            width = String.format("Width: %.2f", rectangle.getWidth());
                            area = String.format("Area: %.2f", rectangle.calculateArea());
                            perimeter = String.format("Perimeter: %.2f", rectangle.calculatePerimeter());
                            coordinatePreview.setText(height + " | " + width + " | " + area + " | " + perimeter);
                            break;
                        }
                    } else if (shape instanceof MyTriangleWithException) {
                        MyTriangleWithException triangle = (MyTriangleWithException) shape;
                        if (isInsideTriangle(currentX, currentY, triangle)) {
                            area = String.format("Area: %.2f", triangle.calculateArea());
                            perimeter = String.format("Perimeter: %.2f", triangle.calculatePerimeter());
                            side1 = String.format("Side 1: %.2f", triangle.getSide1());
                            side2 = String.format("Side 2: %.2f", triangle.getSide2());
                            side3 = String.format("Side 3: %.2f", triangle.getSide3());
                            coordinatePreview.setText(area + " | " + perimeter + " | " + side1 + " | " + side2 + " | " + side3);
                            break;
                        }
                    }
                }
            }

        } else if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            // When mouse is pressed, capture coordinates for shape drawing
            handleClick(currentX, currentY);
        } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
            // Clear preview when mouse leaves canvas
            coordinatePreview.setText("X: --, Y: --");
        } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            // custom drawing, provide x and y coordinates and point count
            if (customDrawingMode && currentShapeType == null) {
                handleDragged(currentX, currentY);
                String coordinate = String.format("X: %.1f, Y: %.1f | Free Drawing: %d points", 
                currentX, currentY, clickedPoints.size());
                coordinatePreview.setText(coordinate);
            }
        } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
            // Finalize custom drawn line and display its total length
            if (customDrawingMode && currentShapeType == null && !clickedPoints.isEmpty()) {
                finalizeCustomDrawing();
            }
        }
    }
    
    private void handleClick(double x, double y) {
        // If no shape type is selected, start free drawing mode
        if (currentShapeType == null) {
            try {
                MyPointWithException point = new MyPointWithException(x, y);
                clickedPoints.add(point);
                customDrawingMode = true;
            } catch (InvalidCoordinateException e) {
                Utility.showErrorAlert("Invalid Coordinate", "Cannot use this point", e.getMessage());
            }
            return;
        }
        
        try {
            MyPointWithException point = new MyPointWithException(x, y);
            clickedPoints.add(point);
            
            // Add visual black point indicator at click location
            javafx.scene.shape.Circle dot = new javafx.scene.shape.Circle(x, y, 3);
            dot.setFill(javafx.scene.paint.Color.BLACK);
            dot.setStroke(javafx.scene.paint.Color.WHITE);
            dot.setStrokeWidth(1);
            visualPoints.add(dot);
            drawingPane.getChildren().add(dot);

            // Check if enough points are present
            if (clickedPoints.size() >= getRequiredClicks()) {
                createShape();
                resetDrawing();
            }
        } catch (InvalidCoordinateException e) {
            Utility.showErrorAlert("Invalid Coordinate", "Cannot use this point", e.getMessage());
            resetDrawing();
        }
    }

    // Free drawing mode - continuously collect points during drag
    private void handleDragged(double x, double y) {
        try {
            // Only add point if it's far enough from the last point (smooth the line)
            if (clickedPoints.isEmpty()) {
                MyPointWithException point = new MyPointWithException(x, y);
                clickedPoints.add(point);
            } else {
                MyPointWithException lastPoint = clickedPoints.get(clickedPoints.size() - 1);
                double distance = Math.sqrt(Math.pow(x - lastPoint.getX(), 2) + Math.pow(y - lastPoint.getY(), 2));
                
                // Only add point if it's at least 50 pixels away from last point (reduce number of segments)
                if (distance >= 50) {
                    MyPointWithException point = new MyPointWithException(x, y);
                    clickedPoints.add(point);
                    
                    // Draw visual indicator for the new point
                    javafx.scene.shape.Circle dot = new javafx.scene.shape.Circle(x, y, 1);
                    dot.setFill(javafx.scene.paint.Color.BLACK);
                    visualPoints.add(dot);
                    drawingPane.getChildren().add(dot);
                }
            }
        } catch (InvalidCoordinateException e) {
            // Silently skip invalid points during drag
        }
    }
    
    // Finalize the custom drawing by creating line segments
    private void finalizeCustomDrawing() {
        if (clickedPoints.size() < 2) {
            resetDrawing();
            customDrawingMode = false;
            return;
        }
        
        try {
            // Create line segments connecting all points
            double totalLength = 0.0;
            for (int i = 0; i < clickedPoints.size() - 1; i++) {
                MyPointWithException p1 = clickedPoints.get(i);
                MyPointWithException p2 = clickedPoints.get(i + 1);
                canvas.AutocreateLineFromPoints(p1, p2);
                
                // Calculate segment length
                double segmentLength = Math.sqrt(
                    Math.pow(p2.getX() - p1.getX(), 2) + 
                    Math.pow(p2.getY() - p1.getY(), 2)
                );
                totalLength += segmentLength;
            }
            
            // Display total length in coordinate preview
            String message = String.format("Custom Drawing Complete | Total Length: %.2f | Segments: %d", 
                totalLength, clickedPoints.size() - 1);
            coordinatePreview.setText(message);
            
        } catch (Exception e) {
            Utility.showErrorAlert("Error Creating Custom Drawing", "Failed to create line segments", e.getMessage());
        }
        
        // Reset for next drawing
        resetDrawing();
        customDrawingMode = false;
    }
    
    private void createShape() {
        try {
            switch (currentShapeType) {
                case "LINE":
                    canvas.AutocreateLineFromPoints(clickedPoints.get(0), clickedPoints.get(1));
                    break;
                case "CIRCLE":
                    canvas.AutocreateCircleFromPoints(clickedPoints.get(0), clickedPoints.get(1));
                    break;
                case "RECTANGLE":
                    canvas.AutocreateRectangleFromPoints(clickedPoints.get(0), clickedPoints.get(1));
                    break;
                case "SQUARE":
                    canvas.AutocreateSquareFromPoints(clickedPoints.get(0), clickedPoints.get(1));
                    break;
                case "TRIANGLE":
                    canvas.AutocreateTriangleFromPoints(clickedPoints.get(0), clickedPoints.get(1), clickedPoints.get(2));
                    break;
            }
        } catch (Exception e) {
            Utility.showErrorAlert("Error Creating Shape", "Failed to create " + currentShapeType, e.getMessage());
        }
    }
    
    private int getRequiredClicks() {
        switch (currentShapeType) {
            case "TRIANGLE": return 3;
            case "LINE":
            case "CIRCLE":
            case "RECTANGLE":
            case "SQUARE":
                return 2;
            default: return 0;
        }
    }
    
    // Methods to control drawing mode
    public void setShapeType(String shapeType) {
        this.currentShapeType = shapeType;
        resetDrawing();
    }
    
    public void cancelDrawing() {
        resetDrawing();
        currentShapeType = null;
    }
    
    private void resetDrawing() {
        clickedPoints.clear();
        
        // Remove all visual point indicators from canvas
        for (javafx.scene.shape.Circle dot : visualPoints) {
            drawingPane.getChildren().remove(dot);
        }
        visualPoints.clear();
    }
    
    public String getCurrentShapeType() {
        return currentShapeType;
    }

    public double getMouseX(){
        return currentX;
    }
    public double getMouseY(){
        return currentY;
    }
    
    // Helper methods to check if mouse is over a shape
    private boolean isNearLine(double x, double y, MyLineWithException line) {
        double x1 = line.getStartPoint().getX();
        double y1 = line.getStartPoint().getY();
        double x2 = line.getEndPoint().getX();
        double y2 = line.getEndPoint().getY();
        
        // Calculate distance from point to line segment
        double lineLength = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        double distance = Math.abs((y2 - y1) * x - (x2 - x1) * y + x2 * y1 - y2 * x1) / lineLength;
        
        // Check if point is within line segment bounds and close enough
        double minX = Math.min(x1, x2) - 5;
        double maxX = Math.max(x1, x2) + 5;
        double minY = Math.min(y1, y2) - 5;
        double maxY = Math.max(y1, y2) + 5;
        
        return distance < 5 && x >= minX && x <= maxX && y >= minY && y <= maxY;
    }
    
    private boolean isInsideCircle(double x, double y, MyCircleWithException circle) {
        double cx = circle.getCenter().getX();
        double cy = circle.getCenter().getY();
        double r = circle.getRadius();
        double distance = Math.sqrt(Math.pow(x - cx, 2) + Math.pow(y - cy, 2));
        return distance <= r;
    }
    
    private boolean isInsideRectangle(double x, double y, MyPointWithException topLeft, MyPointWithException bottomRight) {
        double minX = Math.min(topLeft.getX(), bottomRight.getX());
        double maxX = Math.max(topLeft.getX(), bottomRight.getX());
        double minY = Math.min(topLeft.getY(), bottomRight.getY());
        double maxY = Math.max(topLeft.getY(), bottomRight.getY());
        
        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }
    
    private boolean isInsideTriangle(double x, double y, MyTriangleWithException triangle) {
        double x1 = triangle.getP1().getX();
        double y1 = triangle.getP1().getY();
        double x2 = triangle.getP2().getX();
        double y2 = triangle.getP2().getY();
        double x3 = triangle.getP3().getX();
        double y3 = triangle.getP3().getY();
        
        // Use barycentric coordinates to check if point is inside triangle
        double denominator = ((y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3));
        double a = ((y2 - y3) * (x - x3) + (x3 - x2) * (y - y3)) / denominator;
        double b = ((y3 - y1) * (x - x3) + (x1 - x3) * (y - y3)) / denominator;
        double c = 1 - a - b;
        
        return a >= 0 && a <= 1 && b >= 0 && b <= 1 && c >= 0 && c <= 1;
    }
}