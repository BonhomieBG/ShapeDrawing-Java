// File name: Canvas.java
// Description: Custom canvas class to work with Shape Collection that was created.
// 
// Time Spent: About 15 hours
//
// Revision History
// Date: By: Action
// 12/7/2025 Created the Canvas class
// 12/13/2025 Canvas class now is subclass of ShapeManagerWithException
// 12/13/2025 Added undo/redo functionality with separate stack

import java.util.Stack;
import javafx.scene.layout.Pane;
import java.util.Optional;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ChoiceDialog;

public class CanvasHelper extends ShapeManagerWithException{
    // Shapes are stored in parent class ArrayList via super.addShape()
    private Pane drawingPane;
    private Stack<MyShapeWithException> Stack; // Java support stack like assembly language push/pop
    private Runnable updateCallback; // Callback to update property panel

    public CanvasHelper(Pane drawingPane){
        super();
        this.drawingPane = drawingPane;
        this.Stack = new Stack<>();
    }
    
    public void setUpdateCallback(Runnable callback) {
        this.updateCallback = callback;
    }

    @Override
    public void addShape(MyShapeWithException shape){
        if (shape == null) {
            Utility.showErrorAlert("Error", "Cannot Add Shape", "Shape object is null");
            return;
        }
        try {
            super.addShape(shape);
            javafx.scene.shape.Shape fxShape = shape.toFXShape();
            if (fxShape == null) {
                Utility.showErrorAlert("Error", "Cannot Render Shape", "Failed to convert shape to visual representation");
                return;
            }
            drawingPane.getChildren().add(fxShape);
            if (updateCallback != null) updateCallback.run();
        } catch (Exception e) {
            Utility.showErrorAlert("Error", "Failed to Add Shape", e.getMessage());
        }
    }

    @Override
    public boolean removeShape(int index){
        if (index >= 0 && index < super.getShapes().size()) {
            MyShapeWithException removedShape = super.getShapes().get(index);
            
            // Remove from canvas by finding matching shape
            drawingPane.getChildren().removeIf(node -> {
                if (node instanceof javafx.scene.shape.Shape) {
                    javafx.scene.shape.Shape shape = (javafx.scene.shape.Shape) node;
                    // Compare shape properties to find the right one
                    return isSameShape(shape, removedShape);
                }
                return false;
            });
            
            super.removeShape(index);
            return true;
        }
        return false;
    }

    @Override
    public void removeAllShapes() throws IndexOutOfBoundsException, NullPointerException{
        try{
            Optional<ButtonType> result = Utility.showWarningAlert(
                "Remove All Shapes",
                "This will delete all shapes",
                "Are you sure you want to delete all shapes? This action cannot be undone!"
            );
            if (result.isPresent() && result.get() == Utility.yesButtonType) {
                clearCanvas();
            }
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            Utility.showErrorAlert("Remove Error", "Cannot remove all shapes", e.getMessage());
        }
    }

    // Clear canvas without confirmation dialog (for use when confirmation is handled elsewhere)
    public void clearCanvas() {
        // Clear the visual canvas first
        drawingPane.getChildren().clear();
        // Clear the shapes collection
        super.removeAllShapes();
        // Clear undo/redo stack
        clearStack();
    }

    // Remove recently added shape (undo last shape)
    public void undoShape() throws IndexOutOfBoundsException, NullPointerException{
        try{
            int lastShapeIndex = super.getShapes().size() - 1;
            if (lastShapeIndex > 0) {
                MyShapeWithException removedShape = super.getShapes().get(lastShapeIndex);
                Stack.push(removedShape); // Push to stack for redo
                
                // Remove from canvas
                drawingPane.getChildren().removeIf(node -> {
                    if (node instanceof javafx.scene.shape.Shape) {
                        return isSameShape((javafx.scene.shape.Shape) node, removedShape);
                    }
                    return false;
                });
                
                // Remove from collection
                super.removeShape(lastShapeIndex);
            }
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            Utility.showErrorAlert("Undo Error", "Cannot undo shape", e.getMessage());
        }
    }

    // Add back recently removed shape (redo last undone shape)
    public void redoShape() throws IndexOutOfBoundsException, NullPointerException{
        try{
            if (!Stack.isEmpty()) {
                MyShapeWithException shapeToRestore = Stack.pop(); // Get from stack
                super.addShape(shapeToRestore);
                drawingPane.getChildren().add(shapeToRestore.toFXShape());
            }
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            Utility.showErrorAlert("Redo Error", "Cannot redo shape", e.getMessage());
        }
    }

    // Clear history
    public void clearStack(){
        Stack.clear();
    }

    public MyPointWithException captureMouse(double x, double y){
        try {
            return new MyPointWithException(x, y);
        } catch (InvalidCoordinateException e) {
            Utility.showErrorAlert("Coordinate Error", "Invalid mouse coordinates", e.getMessage());
            return null;
        }
    }

    // Replace CLI for direct shape creation provided user want to input attributes directly
    @Override
    public void addLine() {
        try {
            TextInputDialog dialog = new TextInputDialog();
            
            dialog.setTitle("Add Line");
            dialog.setHeaderText("Enter line coordinates");
            dialog.setContentText("Start point X:");
            Optional<String> x1Result = dialog.showAndWait();
            if (!x1Result.isPresent()) return;
            double x1 = Double.parseDouble(x1Result.get());
            
            dialog.setContentText("Start point Y:");
            Optional<String> y1Result = dialog.showAndWait();
            if (!y1Result.isPresent()) return;
            double y1 = Double.parseDouble(y1Result.get());
            
            dialog.setContentText("End point X:");
            Optional<String> x2Result = dialog.showAndWait();
            if (!x2Result.isPresent()) return;
            double x2 = Double.parseDouble(x2Result.get());
            
            dialog.setContentText("End point Y:");
            Optional<String> y2Result = dialog.showAndWait();
            if (!y2Result.isPresent()) return;
            double y2 = Double.parseDouble(y2Result.get());
            
            // Stroke settings
            ChoiceDialog<String> strokeDialog = new ChoiceDialog<>("Yes", "Yes", "No");
            strokeDialog.setTitle("Stroke Settings");
            strokeDialog.setHeaderText("Show stroke/outline?");
            strokeDialog.setContentText("Choose option:");
            Optional<String> strokeStatusResult = strokeDialog.showAndWait();
            boolean hasStroke = strokeStatusResult.isPresent() && strokeStatusResult.get().equals("Yes");
            
            String strokeColor = "";
            if (hasStroke) {
                dialog.setContentText("Stroke color (or leave empty for default):");
                Optional<String> strokeColorResult = dialog.showAndWait();
                strokeColor = strokeColorResult.orElse("");
            }
            
            MyLineWithException line = new MyLineWithException(
                new MyPointWithException(x1, y1),
                new MyPointWithException(x2, y2)
            );

            // Apply settings after shape creation
            if (hasStroke) {
                line.setStrokeStatus(true);
                if (!strokeColor.isEmpty()) line.setStrokeColor(strokeColor);
            } else {
                line.setStrokeStatus(false);
            }
            
            addShape(line);
            Utility.showInfoAlert("Success", "Line","Line added successfully!");
            
        } catch (NumberFormatException e) {
            Utility.showErrorAlert("Input Error", "Invalid number format", e.getMessage());
        } catch (InvalidCoordinateException | IllegalArgumentException e) {
            Utility.showErrorAlert("Shape Error", e.getClass().getSimpleName(), e.getMessage());
        }
    }
    
    @Override
    public void addCircle() {
        try {
            TextInputDialog dialog = new TextInputDialog();
            
            dialog.setTitle("Add Circle");
            dialog.setHeaderText("Enter circle properties");
            dialog.setContentText("Center point X:");
            Optional<String> xResult = dialog.showAndWait();
            if (!xResult.isPresent()) return;
            double x = Double.parseDouble(xResult.get());
            
            dialog.setContentText("Center point Y:");
            Optional<String> yResult = dialog.showAndWait();
            if (!yResult.isPresent()) return;
            double y = Double.parseDouble(yResult.get());
            
            dialog.setContentText("Radius:");
            Optional<String> radiusResult = dialog.showAndWait();
            if (!radiusResult.isPresent()) return;
            double radius = Double.parseDouble(radiusResult.get());
            
            // Stroke settings
            ChoiceDialog<String> strokeDialog = new ChoiceDialog<>("Yes", "Yes", "No");
            strokeDialog.setTitle("Stroke Settings");
            strokeDialog.setHeaderText("Show stroke/outline?");
            strokeDialog.setContentText("Choose option:");
            Optional<String> strokeStatusResult = strokeDialog.showAndWait();
            boolean hasStroke = strokeStatusResult.isPresent() && strokeStatusResult.get().equals("Yes");
            
            String strokeColor = "";
            if (hasStroke) {
                dialog.setContentText("Stroke color (or leave empty for default):");
                Optional<String> strokeColorResult = dialog.showAndWait();
                strokeColor = strokeColorResult.orElse("");
            }
            
            // Fill settings
            ChoiceDialog<String> fillDialog = new ChoiceDialog<>("No", "Yes", "No");
            fillDialog.setTitle("Fill Settings");
            fillDialog.setHeaderText("Fill the shape?");
            fillDialog.setContentText("Choose option:");
            Optional<String> filledResult = fillDialog.showAndWait();
            boolean isFilled = filledResult.isPresent() && filledResult.get().equals("Yes");
            
            String fillColor = "";
            if (isFilled) {
                dialog.setContentText("Fill color (or leave empty for default):");
                Optional<String> fillColorResult = dialog.showAndWait();
                fillColor = fillColorResult.orElse("");
            }
            
            MyCircleWithException circle = new MyCircleWithException(
                new MyPointWithException(x, y),
                radius
            );
            
            // Apply settings after shape creation
            if (hasStroke) {
                circle.setStrokeStatus(true);
                if (!strokeColor.isEmpty()) circle.setStrokeColor(strokeColor);
            } else {
                circle.setStrokeStatus(false);
            }
            
            if (isFilled) {
                circle.setFilledStatus(true);
                if (!fillColor.isEmpty()) circle.setFillColor(fillColor);
            } else {
                circle.setFilledStatus(false);
            }
            
            addShape(circle);
            Utility.showInfoAlert("Success","Circle", "Circle added successfully!");
            
        } catch (NumberFormatException e) {
            Utility.showErrorAlert("Input Error", "Invalid number format", e.getMessage());
        } catch (InvalidCoordinateException | InvalidRadiusException | IllegalArgumentException e) {
            Utility.showErrorAlert("Shape Error", e.getClass().getSimpleName(), e.getMessage());
        }
    }
    
    @Override
    public void addRectangle() {
        try {
            TextInputDialog dialog = new TextInputDialog();
            
            dialog.setTitle("Add Rectangle");
            dialog.setHeaderText("Enter rectangle coordinates");
            dialog.setContentText("Top-left point X:");
            Optional<String> x1Result = dialog.showAndWait();
            if (!x1Result.isPresent()) return;
            double x1 = Double.parseDouble(x1Result.get());
            
            dialog.setContentText("Top-left point Y:");
            Optional<String> y1Result = dialog.showAndWait();
            if (!y1Result.isPresent()) return;
            double y1 = Double.parseDouble(y1Result.get());
            
            dialog.setContentText("Bottom-right point X:");
            Optional<String> x2Result = dialog.showAndWait();
            if (!x2Result.isPresent()) return;
            double x2 = Double.parseDouble(x2Result.get());
            
            dialog.setContentText("Bottom-right point Y:");
            Optional<String> y2Result = dialog.showAndWait();
            if (!y2Result.isPresent()) return;
            double y2 = Double.parseDouble(y2Result.get());
            
            // Stroke settings
            ChoiceDialog<String> strokeDialog = new ChoiceDialog<>("Yes", "Yes", "No");
            strokeDialog.setTitle("Stroke Settings");
            strokeDialog.setHeaderText("Show stroke/outline?");
            strokeDialog.setContentText("Choose option:");
            Optional<String> strokeStatusResult = strokeDialog.showAndWait();
            boolean hasStroke = strokeStatusResult.isPresent() && strokeStatusResult.get().equals("Yes");
            
            String strokeColor = "";
            if (hasStroke) {
                dialog.setContentText("Stroke color (or leave empty for default):");
                Optional<String> strokeColorResult = dialog.showAndWait();
                strokeColor = strokeColorResult.orElse("");
            }
            
            // Fill settings
            ChoiceDialog<String> fillDialog = new ChoiceDialog<>("No", "Yes", "No");
            fillDialog.setTitle("Fill Settings");
            fillDialog.setHeaderText("Fill the shape?");
            fillDialog.setContentText("Choose option:");
            Optional<String> filledResult = fillDialog.showAndWait();
            boolean isFilled = filledResult.isPresent() && filledResult.get().equals("Yes");
            
            String fillColor = "";
            if (isFilled) {
                dialog.setContentText("Fill color (or leave empty for default):");
                Optional<String> fillColorResult = dialog.showAndWait();
                fillColor = fillColorResult.orElse("");
            }
            
            MyRectangleWithException rectangle = new MyRectangleWithException(
                new MyPointWithException(x1, y1),
                new MyPointWithException(x2, y2)
            );

            // Apply settings after shape creation
            if (hasStroke) {
                rectangle.setStrokeStatus(true);
                if (!strokeColor.isEmpty()) rectangle.setStrokeColor(strokeColor);
            } else {
                rectangle.setStrokeStatus(false);
            }
            
            if (isFilled) {
                rectangle.setFilledStatus(true);
                if (!fillColor.isEmpty()) rectangle.setFillColor(fillColor);
            } else {
                rectangle.setFilledStatus(false);
            }

            addShape(rectangle);
            Utility.showInfoAlert("Success", "Rectangle", "Rectangle added successfully!");
            
        } catch (NumberFormatException e) {
            Utility.showErrorAlert("Input Error", "Invalid number format", e.getMessage());
        } catch (InvalidCoordinateException | IllegalArgumentException e) {
            Utility.showErrorAlert("Shape Error", e.getClass().getSimpleName(), e.getMessage());
        }
    }
    
    @Override
    public void addSquare() {
        try {
            TextInputDialog dialog = new TextInputDialog();
            
            dialog.setTitle("Add Square");
            dialog.setHeaderText("Enter square properties");
            dialog.setContentText("Top-left point X:");
            Optional<String> xResult = dialog.showAndWait();
            if (!xResult.isPresent()) return;
            double x = Double.parseDouble(xResult.get());
            
            dialog.setContentText("Top-left point Y:");
            Optional<String> yResult = dialog.showAndWait();
            if (!yResult.isPresent()) return;
            double y = Double.parseDouble(yResult.get());
            
            dialog.setContentText("Side length:");
            Optional<String> sideResult = dialog.showAndWait();
            if (!sideResult.isPresent()) return;
            double side = Double.parseDouble(sideResult.get());
            
            // Stroke settings
            ChoiceDialog<String> strokeDialog = new ChoiceDialog<>("Yes", "Yes", "No");
            strokeDialog.setTitle("Stroke Settings");
            strokeDialog.setHeaderText("Does it have stroke/outline?");
            strokeDialog.setContentText("Choose option:");
            Optional<String> strokeStatusResult = strokeDialog.showAndWait();
            boolean hasStroke = strokeStatusResult.isPresent() && strokeStatusResult.get().equals("Yes");
            
            String strokeColor = "";
            if (hasStroke) {
                dialog.setContentText("Stroke color (or leave empty for default):");
                Optional<String> strokeColorResult = dialog.showAndWait();
                strokeColor = strokeColorResult.orElse("");
            }
            
            // Fill settings
            ChoiceDialog<String> fillDialog = new ChoiceDialog<>("No", "Yes", "No");
            fillDialog.setTitle("Fill Settings");
            fillDialog.setHeaderText("Fill the shape?");
            fillDialog.setContentText("Choose option:");
            Optional<String> filledResult = fillDialog.showAndWait();
            boolean isFilled = filledResult.isPresent() && filledResult.get().equals("Yes");
            
            String fillColor = "";
            if (isFilled) {
                dialog.setContentText("Fill color (or leave empty for default):");
                Optional<String> fillColorResult = dialog.showAndWait();
                fillColor = fillColorResult.orElse("");
            }
            
            MySquareWithException square = new MySquareWithException(
                new MyPointWithException(x, y),
                side
            );

            // Apply settings after shape creation
            if (hasStroke) {
                square.setStrokeStatus(true);
                if (!strokeColor.isEmpty()) square.setStrokeColor(strokeColor);
            } else {
                square.setStrokeStatus(false);
            }
            
            if (isFilled) {
                square.setFilledStatus(true);
                if (!fillColor.isEmpty()) square.setFillColor(fillColor);
            } else {
                square.setFilledStatus(false);
            }
            
            addShape(square);
            Utility.showInfoAlert("Success", "Square", "Square added successfully!");
            
        } catch (NumberFormatException e) {
            Utility.showErrorAlert("Input Error", "Invalid number format", e.getMessage());
        } catch (InvalidCoordinateException | IllegalArgumentException e) {
            Utility.showErrorAlert("Shape Error", e.getClass().getSimpleName(), e.getMessage());
        }
    }
    
    @Override
    public void addTriangle() {
        try {
            TextInputDialog dialog = new TextInputDialog();
            
            dialog.setTitle("Add Triangle");
            dialog.setHeaderText("Enter triangle coordinates");
            dialog.setContentText("First point X:");
            Optional<String> x1Result = dialog.showAndWait();
            if (!x1Result.isPresent()) return;
            double x1 = Double.parseDouble(x1Result.get());
            
            dialog.setContentText("First point Y:");
            Optional<String> y1Result = dialog.showAndWait();
            if (!y1Result.isPresent()) return;
            double y1 = Double.parseDouble(y1Result.get());
            
            dialog.setContentText("Second point X:");
            Optional<String> x2Result = dialog.showAndWait();
            if (!x2Result.isPresent()) return;
            double x2 = Double.parseDouble(x2Result.get());
            
            dialog.setContentText("Second point Y:");
            Optional<String> y2Result = dialog.showAndWait();
            if (!y2Result.isPresent()) return;
            double y2 = Double.parseDouble(y2Result.get());
            
            dialog.setContentText("Third point X:");
            Optional<String> x3Result = dialog.showAndWait();
            if (!x3Result.isPresent()) return;
            double x3 = Double.parseDouble(x3Result.get());
            
            dialog.setContentText("Third point Y:");
            Optional<String> y3Result = dialog.showAndWait();
            if (!y3Result.isPresent()) return;
            double y3 = Double.parseDouble(y3Result.get());
            
            // Stroke settings
            ChoiceDialog<String> strokeDialog = new ChoiceDialog<>("Yes", "Yes", "No");
            strokeDialog.setTitle("Stroke Settings");
            strokeDialog.setHeaderText("Show stroke/outline?");
            strokeDialog.setContentText("Choose option:");
            Optional<String> strokeStatusResult = strokeDialog.showAndWait();
            boolean hasStroke = strokeStatusResult.isPresent() && strokeStatusResult.get().equals("Yes");
            
            String strokeColor = "";
            if (hasStroke) {
                dialog.setContentText("Stroke color (or leave empty for default):");
                Optional<String> strokeColorResult = dialog.showAndWait();
                strokeColor = strokeColorResult.orElse("");
            }
            
            // Fill settings
            ChoiceDialog<String> fillDialog = new ChoiceDialog<>("No", "Yes", "No");
            fillDialog.setTitle("Fill Settings");
            fillDialog.setHeaderText("Fill the shape?");
            fillDialog.setContentText("Choose option:");
            Optional<String> filledResult = fillDialog.showAndWait();
            boolean isFilled = filledResult.isPresent() && filledResult.get().equals("Yes");
            
            String fillColor = "";
            if (isFilled) {
                dialog.setContentText("Fill color (or leave empty for default):");
                Optional<String> fillColorResult = dialog.showAndWait();
                fillColor = fillColorResult.orElse("");
            }
            
            MyTriangleWithException triangle = new MyTriangleWithException(
                new MyPointWithException(x1, y1),
                new MyPointWithException(x2, y2),
                new MyPointWithException(x3, y3)
            );
            
            // Apply settings after shape creation
            if (hasStroke) {
                triangle.setStrokeStatus(true);
                if (!strokeColor.isEmpty()) triangle.setStrokeColor(strokeColor);
            } else {
                triangle.setStrokeStatus(false);
            }
            
            if (isFilled) {
                triangle.setFilledStatus(true);
                if (!fillColor.isEmpty()) triangle.setFillColor(fillColor);
            } else {
                triangle.setFilledStatus(false);
            }
            
            addShape(triangle);
            Utility.showInfoAlert("Success", "Triangle", "Triangle added successfully!");
            
        } catch (NumberFormatException e) {
            Utility.showErrorAlert("Input Error", "Invalid number format", e.getMessage());
        } catch (IllegalTriangleException | InvalidCoordinateException | IllegalArgumentException e) {
            Utility.showErrorAlert("Shape Error", e.getClass().getSimpleName(), e.getMessage());
        }
    }

    // Add Shapes from mouse clicks - these calculate topLeft/bottomRight automatically
    
    public void AutocreateLineFromPoints(MyPointWithException p1, MyPointWithException p2) {
        if (p1 == null || p2 == null) {
            Utility.showErrorAlert("Error", "Invalid Coordinates", "Line points cannot be null");
            return;
        }
        try {
            MyLineWithException line = new MyLineWithException(p1, p2);
            
            // Validate line width
            if (!Double.isNaN(ShapeGUIShell.gridLine) && Double.isFinite(ShapeGUIShell.gridLine)) {
                if (ShapeGUIShell.gridLine > 0) {
                    line.setGridLineWidth(ShapeGUIShell.gridLine);
                } else {
                    line.setGridLineWidth(1.0);
                }
            } else {
                line.setGridLineWidth(1.0);
            }
            
            // Set colors BEFORE adding to canvas
            if (ShapeGUIShell.UserstrokeColor != null && !ShapeGUIShell.UserstrokeColor.isBlank()) {
                line.setStrokeColor(ShapeGUIShell.UserstrokeColor);
            }
            addShape(line);
        } catch (InvalidCoordinateException e) {
            Utility.showErrorAlert("Error", "Invalid line coordinates", e.getMessage());
        } catch (Exception e) {
            Utility.showErrorAlert("Error", "Failed to create line", "An unexpected error occurred: " + e.getMessage());
        }
    }
    
    public void AutocreateCircleFromPoints(MyPointWithException p1, MyPointWithException p2) {
        if (p1 == null || p2 == null) {
            Utility.showErrorAlert("Error", "Invalid Coordinates", "Circle points cannot be null");
            return;
        }
        try {
            // Calculate topLeft and bottomRight from two clicked points
            MyPointWithException topLeft = calculateTopLeft(p1, p2);
            MyPointWithException bottomRight = calculateBottomRight(p1, p2);
            
            if (topLeft == null || bottomRight == null) {
                Utility.showErrorAlert("Error", "Invalid Coordinates", "Failed to calculate circle bounds");
                return;
            }
            
            // Set gradient colors from user inputs to ColorPicker BEFORE creating shape
            if (ShapeGUIShell.UserStartgradientColor != null && ShapeGUIShell.UserEndgradientColor != null) {
                ColorPicker.setGradientColors(ShapeGUIShell.UserStartgradientColor, ShapeGUIShell.UserEndgradientColor);
            }
            
            MyCircleWithException circle = new MyCircleWithException(topLeft, bottomRight);
            
            // Validate line width
            if (!Double.isNaN(ShapeGUIShell.gridLine) && Double.isFinite(ShapeGUIShell.gridLine)) {
                if (ShapeGUIShell.gridLine > 0) {
                    circle.setGridLineWidth(ShapeGUIShell.gridLine);
                } else {
                    circle.setGridLineWidth(1.0);
                }
            } else {
                circle.setGridLineWidth(1.0);
            }
            
            if (ShapeGUIShell.isFilled) circle.setFilledStatus(true);
            if (ShapeGUIShell.UserstrokeColor != null && !ShapeGUIShell.UserstrokeColor.isBlank()) {
                circle.setStrokeColor(ShapeGUIShell.UserstrokeColor);
            }
            if (ShapeGUIShell.UserfillColor != null && !ShapeGUIShell.UserfillColor.isBlank()) {
                circle.setFillColor(ShapeGUIShell.UserfillColor);
            }
            
            addShape(circle);
        } catch (InvalidCoordinateException e) {
            Utility.showErrorAlert("Error", "Invalid circle coordinates", e.getMessage());
        } catch (Exception e) {
            Utility.showErrorAlert("Error", "Failed to create circle", "An unexpected error occurred: " + e.getMessage());
        }
    }
    
    public void AutocreateRectangleFromPoints(MyPointWithException p1, MyPointWithException p2) {
        if (p1 == null || p2 == null) {
            Utility.showErrorAlert("Error", "Invalid Coordinates", "Rectangle points cannot be null");
            return;
        }
        try {
            MyPointWithException topLeft = calculateTopLeft(p1, p2);
            MyPointWithException bottomRight = calculateBottomRight(p1, p2);
            
            if (topLeft == null || bottomRight == null) {
                Utility.showErrorAlert("Error", "Invalid Coordinates", "Failed to calculate rectangle bounds");
                return;
            }
            
            if (ShapeGUIShell.UserStartgradientColor != null && ShapeGUIShell.UserEndgradientColor != null) {
                ColorPicker.setGradientColors(ShapeGUIShell.UserStartgradientColor, ShapeGUIShell.UserEndgradientColor);
            }
            
            MyRectangleWithException rectangle = new MyRectangleWithException(topLeft, bottomRight);
            
            // Validate line width
            if (!Double.isNaN(ShapeGUIShell.gridLine) && Double.isFinite(ShapeGUIShell.gridLine)) {
                if (ShapeGUIShell.gridLine > 0) {
                    rectangle.setGridLineWidth(ShapeGUIShell.gridLine);
                } else {
                    rectangle.setGridLineWidth(1.0);
                }
            } else {
                rectangle.setGridLineWidth(1.0);
            }
            
            if (ShapeGUIShell.isFilled) rectangle.setFilledStatus(true);
            if (ShapeGUIShell.UserstrokeColor != null && !ShapeGUIShell.UserstrokeColor.isBlank()) {
                rectangle.setStrokeColor(ShapeGUIShell.UserstrokeColor);
            }
            if (ShapeGUIShell.UserfillColor != null && !ShapeGUIShell.UserfillColor.isBlank()) {
                rectangle.setFillColor(ShapeGUIShell.UserfillColor);
            }
            
            addShape(rectangle);
        } catch (InvalidCoordinateException | IllegalArgumentException e) {
            Utility.showErrorAlert("Error", "Invalid rectangle coordinates", e.getMessage());
        } catch (Exception e) {
            Utility.showErrorAlert("Error", "Failed to create rectangle", "An unexpected error occurred: " + e.getMessage());
        }
    }
    
    public void AutocreateSquareFromPoints(MyPointWithException p1, MyPointWithException p2) {
        if (p1 == null || p2 == null) {
            Utility.showErrorAlert("Error", "Invalid Coordinates", "Square points cannot be null");
            return;
        }
        try {
            // Calculate topLeft and bottomRight from two clicked points
            MyPointWithException topLeft = calculateTopLeft(p1, p2);
            MyPointWithException bottomRight = calculateBottomRight(p1, p2);
            
            if (topLeft == null || bottomRight == null) {
                Utility.showErrorAlert("Error", "Invalid Coordinates", "Failed to calculate square bounds");
                return;
            }
            
            if (ShapeGUIShell.UserStartgradientColor != null && ShapeGUIShell.UserEndgradientColor != null) {
                ColorPicker.setGradientColors(ShapeGUIShell.UserStartgradientColor, ShapeGUIShell.UserEndgradientColor);
            }
            
            MySquareWithException square = new MySquareWithException(topLeft, bottomRight);
            
            // Validate line width
            if (!Double.isNaN(ShapeGUIShell.gridLine) && Double.isFinite(ShapeGUIShell.gridLine)) {
                if (ShapeGUIShell.gridLine > 0) {
                    square.setGridLineWidth(ShapeGUIShell.gridLine);
                } else {
                    square.setGridLineWidth(1.0);
                }
            } else {
                square.setGridLineWidth(1.0);
            }
            
            if (ShapeGUIShell.isFilled) square.setFilledStatus(true);
            if (ShapeGUIShell.UserstrokeColor != null && !ShapeGUIShell.UserstrokeColor.isBlank()) {
                square.setStrokeColor(ShapeGUIShell.UserstrokeColor);
            }
            if (ShapeGUIShell.UserfillColor != null && !ShapeGUIShell.UserfillColor.isBlank()) {
                square.setFillColor(ShapeGUIShell.UserfillColor);
            }
            
            addShape(square);
        } catch (InvalidCoordinateException | IllegalArgumentException e) {
            Utility.showErrorAlert("Error", "Invalid square coordinates", e.getMessage());
        } catch (Exception e) {
            Utility.showErrorAlert("Error", "Failed to create square", "An unexpected error occurred: " + e.getMessage());
        }
    }
    
    public void AutocreateTriangleFromPoints(MyPointWithException p1, MyPointWithException p2, MyPointWithException p3) {
        if (p1 == null || p2 == null || p3 == null) {
            Utility.showErrorAlert("Error", "Invalid Coordinates", "Triangle points cannot be null");
            return;
        }
        try {
            // Set gradient colors from user inputs to ColorPicker BEFORE creating shape
            if (ShapeGUIShell.UserStartgradientColor != null && ShapeGUIShell.UserEndgradientColor != null) {
                ColorPicker.setGradientColors(ShapeGUIShell.UserStartgradientColor, ShapeGUIShell.UserEndgradientColor);
            }
            
            MyTriangleWithException triangle = new MyTriangleWithException(p1, p2, p3);

            // Validate line width
            if (!Double.isNaN(ShapeGUIShell.gridLine) && Double.isFinite(ShapeGUIShell.gridLine)) {
                if (ShapeGUIShell.gridLine > 0) {
                    triangle.setGridLineWidth(ShapeGUIShell.gridLine);
                } else {
                    triangle.setGridLineWidth(1.0);
                }
            } else {
                triangle.setGridLineWidth(1.0);
            }
            
            if (ShapeGUIShell.isFilled) triangle.setFilledStatus(true);
            if (ShapeGUIShell.UserstrokeColor != null && !ShapeGUIShell.UserstrokeColor.isBlank()) {
                triangle.setStrokeColor(ShapeGUIShell.UserstrokeColor);
            }
            if (ShapeGUIShell.UserfillColor != null && !ShapeGUIShell.UserfillColor.isBlank()) {
                triangle.setFillColor(ShapeGUIShell.UserfillColor);
            }
            
            addShape(triangle);
        } catch (InvalidCoordinateException | IllegalTriangleException e) {
            Utility.showErrorAlert("Error", "Invalid triangle", e.getMessage());
        } catch (Exception e) {
            Utility.showErrorAlert("Error", "Failed to create triangle", "An unexpected error occurred: " + e.getMessage());
        }
    }
    
    // Helper methods to calculate topLeft and bottomRight from two arbitrary points
    private MyPointWithException calculateTopLeft(MyPointWithException p1, MyPointWithException p2) throws InvalidCoordinateException {
        double minX = Math.min(p1.getX(), p2.getX());
        double minY = Math.min(p1.getY(), p2.getY());
        return new MyPointWithException(minX, minY);
    }
    
    private MyPointWithException calculateBottomRight(MyPointWithException p1, MyPointWithException p2) throws InvalidCoordinateException {
        double maxX = Math.max(p1.getX(), p2.getX());
        double maxY = Math.max(p1.getY(), p2.getY());
        return new MyPointWithException(maxX, maxY);
    }
    
    // Helper method to compare JavaFX shape to the original shape provided by MyShapeWithException 
    private boolean isSameShape(javafx.scene.shape.Shape fxShape, MyShapeWithException shape) {
        if (fxShape instanceof javafx.scene.shape.Line && shape instanceof MyLineWithException) {
            javafx.scene.shape.Line line = (javafx.scene.shape.Line) fxShape;
            MyLineWithException myLine = (MyLineWithException) shape;
            return Math.abs(line.getStartX() - myLine.getStartPoint().getX()) < 0.1 &&
                   Math.abs(line.getStartY() - myLine.getStartPoint().getY()) < 0.1 &&
                   Math.abs(line.getEndX() - myLine.getEndPoint().getX()) < 0.1 &&
                   Math.abs(line.getEndY() - myLine.getEndPoint().getY()) < 0.1;
        } else if (fxShape instanceof javafx.scene.shape.Circle && shape instanceof MyCircleWithException) {
            javafx.scene.shape.Circle circle = (javafx.scene.shape.Circle) fxShape;
            MyCircleWithException myCircle = (MyCircleWithException) shape;
            return Math.abs(circle.getCenterX() - myCircle.getCenter().getX()) < 0.1 &&
                   Math.abs(circle.getCenterY() - myCircle.getCenter().getY()) < 0.1 &&
                   Math.abs(circle.getRadius() - myCircle.getRadius()) < 0.1;
        } else if (fxShape instanceof javafx.scene.shape.Rectangle && shape instanceof MyRectangleWithException) {
            javafx.scene.shape.Rectangle rect = (javafx.scene.shape.Rectangle) fxShape;
            MyRectangleWithException myRect = (MyRectangleWithException) shape;
            return Math.abs(rect.getX() - myRect.getTopLeft().getX()) < 0.1 &&
                   Math.abs(rect.getY() - myRect.getTopLeft().getY()) < 0.1 &&
                   Math.abs(rect.getWidth() - myRect.getWidth()) < 0.1 &&
                   Math.abs(rect.getHeight() - myRect.getHeight()) < 0.1;
        } else if (fxShape instanceof javafx.scene.shape.Polygon && shape instanceof MyTriangleWithException) {
            javafx.scene.shape.Polygon poly = (javafx.scene.shape.Polygon) fxShape;
            MyTriangleWithException myTri = (MyTriangleWithException) shape;
            if (poly.getPoints().size() == 6) { // Triangle has 3 points = 6 coordinates
                return Math.abs(poly.getPoints().get(0) - myTri.getP1().getX()) < 0.1 &&
                       Math.abs(poly.getPoints().get(1) - myTri.getP1().getY()) < 0.1 &&
                       Math.abs(poly.getPoints().get(2) - myTri.getP2().getX()) < 0.1 &&
                       Math.abs(poly.getPoints().get(3) - myTri.getP2().getY()) < 0.1 &&
                       Math.abs(poly.getPoints().get(4) - myTri.getP3().getX()) < 0.1 &&
                       Math.abs(poly.getPoints().get(5) - myTri.getP3().getY()) < 0.1;
            }
        }
        return false;
    }
}

