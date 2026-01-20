// File name: GUIHandler.java
// Description: This class is a part of GUI that handles menu creation and event handling.
// 
// Time Spent: #
// Challenges: JavaFX is completely new and need to understand how it is work. Need time to learn about each class that it has.
//
// Revision History
// Date: By: Action
// 12/7/2025 Created the GUIHandler class
import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import java.io.*;

public class ShapeGUIShell extends Application{
    public static boolean isFilled = true;
    public static String UserstrokeColor = "black";
    public static String UserfillColor = "black";
    public static String UserStartgradientColor = null;
    public static String UserEndgradientColor = null;
    public static double gridLine = 0.0;

    public static void CheckerGrid(javafx.scene.canvas.GraphicsContext gc, double width, double height){
        gc.setStroke(javafx.scene.paint.Color.LIGHTGRAY);
        gc.setLineWidth(0.5);

        int gridSize = 20;
        // Vertical lines
        for (int x = 0; x < width; x += gridSize) {
            gc.strokeLine(x, 0, x, height);
        }
        // Horizontal lines
        for (int y = 0; y < height; y += gridSize) {
            gc.strokeLine(0, y, width, y);
        }
    }

    public static void CheckerGridDefault(javafx.scene.canvas.GraphicsContext gc){
        gc.setStroke(javafx.scene.paint.Color.LIGHTGRAY);
        gc.setLineWidth(0.5);

        int gridSize = 20;
        // Vertical lines
        for (int x = 0; x < gc.getCanvas().getWidth(); x += gridSize) {
            gc.strokeLine(x, 0, x, gc.getCanvas().getHeight());
        }
        // Horizontal lines
        for (int y = 0; y < gc.getCanvas().getHeight(); y += gridSize) {
            gc.strokeLine(0, y, gc.getCanvas().getWidth(), y);
        }
    }

    @Override
    public void start(Stage stage) {
        // Create menu bar and layout
        MenuBar menuBar = new MenuBar();
        // Create canvas for grid background
        javafx.scene.canvas.Canvas gridCanvas = new javafx.scene.canvas.Canvas();
        javafx.scene.canvas.GraphicsContext gc = gridCanvas.getGraphicsContext2D();
        
        // Create drawing pane on top of grid
        javafx.scene.layout.Pane drawingPane = new javafx.scene.layout.Pane();
        CanvasHelper canvas = new CanvasHelper(drawingPane);
        
        // Stack grid and drawing pane
        javafx.scene.layout.StackPane canvasStack = new javafx.scene.layout.StackPane();
        canvasStack.getChildren().addAll(gridCanvas, drawingPane);
        
        // Bind canvas and pane sizes to the stack pane size
        gridCanvas.widthProperty().bind(canvasStack.widthProperty());
        gridCanvas.heightProperty().bind(canvasStack.heightProperty());
        drawingPane.prefWidthProperty().bind(canvasStack.widthProperty());
        drawingPane.prefHeightProperty().bind(canvasStack.heightProperty());
        
        // Redraw grid when canvas size changes
        gridCanvas.widthProperty().addListener((obs, oldVal, newVal) -> {
            gc.clearRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());
            CheckerGrid(gc, gridCanvas.getWidth(), gridCanvas.getHeight());
        });
        gridCanvas.heightProperty().addListener((obs, oldVal, newVal) -> {
            gc.clearRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());
            CheckerGrid(gc, gridCanvas.getWidth(), gridCanvas.getHeight());
        });
        
        // Initial grid draw
        CheckerGrid(gc, 800, 600);
        
        // Create coordinate preview label
        javafx.scene.control.Label coordinatePreview = new javafx.scene.control.Label("X: --, Y: --");
        coordinatePreview.setStyle("-fx-padding: 5px; -fx-font-size: 12px;");
        
        // Create mouse handler
        MouseHandler mouseHandler = new MouseHandler(canvas, coordinatePreview, drawingPane);
        drawingPane.setOnMouseMoved(mouseHandler);
        drawingPane.setOnMousePressed(mouseHandler);
        drawingPane.setOnMouseDragged(mouseHandler);
        drawingPane.setOnMouseReleased(mouseHandler);
        drawingPane.setOnMouseExited(mouseHandler);
        
        // Create main menu container
        javafx.scene.layout.HBox mainMenu = new javafx.scene.layout.HBox(10);
        mainMenu.setStyle("-fx-padding: 10px; -fx-background-color: #f0f0f0;");

        // Create shape selection buttons (for drawing mode)
        javafx.scene.layout.HBox shapeButtons = new javafx.scene.layout.HBox(10);
        shapeButtons.setStyle("-fx-padding: 10px; -fx-background-color: #f0f0f0;");
        
        javafx.scene.control.Button lineBtn = new javafx.scene.control.Button("Draw Line");
        javafx.scene.control.Button circleBtn = new javafx.scene.control.Button("Draw Circle");
        javafx.scene.control.Button rectangleBtn = new javafx.scene.control.Button("Draw Rectangle");
        javafx.scene.control.Button squareBtn = new javafx.scene.control.Button("Draw Square");
        javafx.scene.control.Button triangleBtn = new javafx.scene.control.Button("Draw Triangle");
        javafx.scene.control.Button cancelBtn = new javafx.scene.control.Button("Cancel");
        javafx.scene.control.Button backBtn = new javafx.scene.control.Button("Back to Menu");
        
        lineBtn.setOnAction(e -> mouseHandler.setShapeType("LINE"));
        circleBtn.setOnAction(e -> mouseHandler.setShapeType("CIRCLE"));
        rectangleBtn.setOnAction(e -> mouseHandler.setShapeType("RECTANGLE"));
        squareBtn.setOnAction(e -> mouseHandler.setShapeType("SQUARE"));
        triangleBtn.setOnAction(e -> mouseHandler.setShapeType("TRIANGLE"));
        cancelBtn.setOnAction(e -> mouseHandler.cancelDrawing());
        
        shapeButtons.getChildren().addAll(lineBtn, circleBtn, rectangleBtn, squareBtn, triangleBtn, cancelBtn, backBtn, coordinatePreview);

        javafx.scene.control.Button addShapeBtn = new javafx.scene.control.Button("Add Shape Manually");
        javafx.scene.control.Button drawShapeBtn = new javafx.scene.control.Button("Draw Shape");
        
        addShapeBtn.setOnAction(e -> {
            javafx.scene.control.ChoiceDialog<String> dialog = new javafx.scene.control.ChoiceDialog<>("Line", 
                "Line", "Circle", "Rectangle", "Square", "Triangle");
            dialog.setTitle("Add New Shape Manually");
            dialog.setHeaderText("Select Shape Type");
            dialog.setContentText("Choose a shape to create:");
            Optional<String> Result = dialog.showAndWait();

            try {
                if (Result.isPresent()) {
                    String shapeType = Result.get().toUpperCase();
                    switch (shapeType) {
                        case "LINE":
                            canvas.addLine();
                            break;
                        case "CIRCLE":
                            canvas.addCircle();
                            break;
                        case "RECTANGLE":
                            canvas.addRectangle();
                            break;
                        case "SQUARE":
                            canvas.addSquare();
                            break;
                        case "TRIANGLE":
                            canvas.addTriangle();
                            break;
                        default:
                            Utility.showErrorAlert("Error", "Invalid Shape", "Please select a valid shape type.");
                    }
                }
            } catch (Exception ex) {
                Utility.showErrorAlert("Error", "Exception Occurred", ex.getMessage());
            }
        });

        mainMenu.getChildren().addAll(addShapeBtn, drawShapeBtn);

        // Create File menu
        Menu fileMenu = new Menu("File");
        MenuItem newItem = new MenuItem("New Drawing");
        MenuItem openItem = new MenuItem("Open");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().addAll(newItem, openItem, saveItem, exitItem);
        
        // Create Edit menu
        Menu editMenu = new Menu("Edit");
        MenuItem undoItem = new MenuItem("Undo");
        MenuItem redoItem = new MenuItem("Redo");
        MenuItem clearItem = new MenuItem("Clear All Shapes");
        MenuItem resetItem = new MenuItem("Reset All Settings");
        editMenu.getItems().addAll(undoItem, redoItem, clearItem, resetItem);

        //Options menu
        Menu optionsMenu = new Menu("Options");
        MenuItem colorItem = new MenuItem("Set Background Color");
        MenuItem outLineColorItem = new MenuItem("Set Outline Color");
        MenuItem filledItem = new MenuItem("Toggle Filled/Hollow");
        MenuItem SetWidthItem = new MenuItem("Set Line Width");
        MenuItem GradientItem = new MenuItem("Enable/Disable Gradient Fill");
        MenuItem GradientColorsItem = new MenuItem("Set Gradient Colors");
        MenuItem GradientDirectionItem = new MenuItem("Set Gradient Direction");
        optionsMenu.getItems().addAll(colorItem, outLineColorItem, filledItem, SetWidthItem, GradientItem, GradientColorsItem, GradientDirectionItem);

        // Create Help menu
        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        MenuItem howToUseItem = new MenuItem("How to Use");
        aboutItem.setOnAction(e -> Utility.showInfoAlert("About", "Welcome to Shape Management GUI", "Version Beta 0.0.1"));
        helpMenu.getItems().addAll(aboutItem, howToUseItem);
        
        // Add menus to menu bar
        menuBar.getMenus().addAll(fileMenu, editMenu, optionsMenu, helpMenu);
        
        // Create layout with buttons at top
        javafx.scene.layout.VBox topContainer = new javafx.scene.layout.VBox();
        topContainer.getChildren().addAll(menuBar, mainMenu);
        
        // Button handlers to switch between menus
        drawShapeBtn.setOnAction(e -> {
            topContainer.getChildren().remove(mainMenu);
            topContainer.getChildren().add(shapeButtons);
        });
        
        backBtn.setOnAction(e -> {
            topContainer.getChildren().remove(shapeButtons);
            topContainer.getChildren().add(mainMenu);
            mouseHandler.cancelDrawing();
        });

        // Right side of the drawing pane
        javafx.scene.layout.VBox rightContainer = new javafx.scene.layout.VBox(10);
        rightContainer.setStyle("-fx-padding: 10px; -fx-background-color: #f0f0f0;");
        
        Label propertiesTitle = new Label("Current Settings");
        propertiesTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 5px;");
        
        Label strokeColorLabel = new Label("Outline: " + UserstrokeColor);
        Label fillStatusLabel = new Label("Filled: " + (isFilled ? "Yes" : "No"));
        Label lineWidthLabel = new Label("Line Width: " + (gridLine == 0.0 ? "Default" : gridLine));
        Label gradientStatusLabel = new Label("Gradient: " + (ColorPicker.isUseGradient() ? "Enabled" : "Disabled"));
        Label shapeCountLabel = new Label("Total Shapes: " + canvas.getShapes().size());
        Label gradientColorsLabel = new Label("Gradient Colors: " + 
            (UserStartgradientColor != null && UserEndgradientColor != null ? 
            UserStartgradientColor + " to " + UserEndgradientColor : "Default"));
        
        // Method to update all property labels
        Runnable updatePropertyPanel = () -> {
            strokeColorLabel.setText("Outline: " + UserstrokeColor);
            fillStatusLabel.setText("Filled: " + (isFilled ? "Yes" : "No"));
            lineWidthLabel.setText("Line Width: " + (gridLine == 0.0 ? "Default" : gridLine));
            gradientStatusLabel.setText("Gradient: " + (ColorPicker.isUseGradient() ? "Enabled" : "Disabled"));
            shapeCountLabel.setText("Total Shapes: " + canvas.getShapes().size());
            gradientColorsLabel.setText("Gradient Colors: " + 
                (UserStartgradientColor != null && UserEndgradientColor != null ? 
                UserStartgradientColor + " to " + UserEndgradientColor : "Default"));
        };
        
        // Connect the update callback to canvas
        canvas.setUpdateCallback(updatePropertyPanel);
        ColorPicker.setUpdateCallback(updatePropertyPanel);
        
        // Grid Size Control Section
        javafx.scene.layout.VBox gridControlBox = new javafx.scene.layout.VBox(5);
        gridControlBox.setStyle("-fx-padding: 10px; -fx-background-color: #e0e0e0; -fx-border-color: #cccccc; -fx-border-width: 1px;");
        
        Label gridTitle = new Label("Grid Settings");
        gridTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        javafx.scene.layout.HBox gridSizeBox = new javafx.scene.layout.HBox(10);
        gridSizeBox.setStyle("-fx-alignment: center-left;");
        Label gridSizeLabel = new Label("Grid Size:");
        javafx.scene.control.TextField gridSizeField = new javafx.scene.control.TextField("20");
        gridSizeField.setPrefWidth(60);
        gridSizeField.setStyle("-fx-font-size: 12px;");
        
        // Real-time grid update on text change
        gridSizeField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!newValue.trim().isEmpty()) {
                    int newGridSize = Integer.parseInt(newValue.trim());
                    if (newGridSize <0) {
                        Utility.showErrorAlert("Error", "Invalid Grid Size", "Grid size must be a positive integer.");
                        return;
                    }
                    if (newGridSize > 0 && newGridSize <= 200) {
                        // Update the CheckerGrid method to use the new size
                        gc.clearRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());
                        gc.setStroke(javafx.scene.paint.Color.LIGHTGRAY);
                        gc.setLineWidth(0.5);
                        
                        // Vertical lines
                        for (int x = 0; x < gridCanvas.getWidth(); x += newGridSize) {
                            gc.strokeLine(x, 0, x, gridCanvas.getHeight());
                        }
                        // Horizontal lines
                        for (int y = 0; y < gridCanvas.getHeight(); y += newGridSize) {
                            gc.strokeLine(0, y, gridCanvas.getWidth(), y);
                        }
                    }
                }
            } catch (NumberFormatException ex) {
                Utility.showErrorAlert("Error", "Invalid Grid Size", "Please enter a valid positive integer for grid size.");
            } catch (Exception ex) {
                Utility.showErrorAlert("Error", "Unexpected Error", "Failed to update grid size: " + ex.getMessage());
            }
        });
        
        gridSizeBox.getChildren().addAll(gridSizeLabel, gridSizeField);
        
        // Line Width Control (for shape drawing)
        javafx.scene.layout.HBox lineWidthBox = new javafx.scene.layout.HBox(10);
        lineWidthBox.setStyle("-fx-alignment: center-left;");
        Label lineWidthInputLabel = new Label("Line Width:");
        javafx.scene.control.TextField lineWidthField = new javafx.scene.control.TextField(gridLine == 0.0 ? "1.0" : String.valueOf(gridLine));
        lineWidthField.setPrefWidth(60);
        lineWidthField.setStyle("-fx-font-size: 12px;");
        
        // Real-time line width update
        lineWidthField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!newValue.trim().isEmpty()) {
                    double newLineWidth = Double.parseDouble(newValue.trim());
                    if (newLineWidth > 0 && newLineWidth <= 100) {
                        gridLine = newLineWidth;
                        updatePropertyPanel.run();
                    } else if (newLineWidth < 0){
                        Utility.showErrorAlert("Error", "Invalid Line Width", "Line width must be greater than 0.");
                    }
                }
            } catch (NumberFormatException ex) {
                Utility.showErrorAlert("Error", "Invalid Line Width", "Please enter a valid number for line width.");
            }
        });
        
        lineWidthBox.getChildren().addAll(lineWidthInputLabel, lineWidthField);
        gridControlBox.getChildren().addAll(gridTitle, gridSizeBox, lineWidthBox);
        
        // Quick Color Selection Panel
        javafx.scene.layout.VBox colorSelectionBox = new javafx.scene.layout.VBox(5);
        colorSelectionBox.setStyle("-fx-padding: 10px; -fx-background-color: #e0e0e0; -fx-border-color: #cccccc; -fx-border-width: 1px;");
        
        Label colorTitle = new Label("Quick Colors");
        colorTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        // Create color buttons in a grid layout
        javafx.scene.layout.GridPane colorGrid = new javafx.scene.layout.GridPane();
        colorGrid.setHgap(5);
        colorGrid.setVgap(5);
        
        // Common colors to display
        String[][] colors = {
            {"black", "white", "gray", "lightgray"},
            {"red", "green", "blue", "yellow"},
            {"orange", "purple", "pink", "cyan"},
            {"brown", "lime", "navy", "magenta"}
        };
        
        // Create buttons for each color
        for (int row = 0; row < colors.length; row++) {
            for (int col = 0; col < colors[row].length; col++) {
                String colorName = colors[row][col];
                javafx.scene.control.Button colorBtn = new javafx.scene.control.Button();
                colorBtn.setPrefSize(40, 30);
                
                // Set button background to the color
                try {
                    javafx.scene.paint.Color fxColor = javafx.scene.paint.Color.valueOf(colorName);
                    colorBtn.setStyle(String.format("-fx-background-color: %s; -fx-border-color: #888888; -fx-border-width: 1px;", colorName));
                    
                    // Tooltip showing color name
                    javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(colorName);
                    colorBtn.setTooltip(tooltip);
                    
                    // Click handler to set as stroke and fill color
                    colorBtn.setOnAction(ev -> {
                        UserstrokeColor = colorName;
                        UserfillColor = colorName;
                        updatePropertyPanel.run();
                    });
                    
                    colorGrid.add(colorBtn, col, row);
                } catch (IllegalArgumentException e) {
                    // Skip invalid color names
                }
            }
        }
        
        colorSelectionBox.getChildren().addAll(colorTitle, colorGrid);
        
        rightContainer.getChildren().addAll(propertiesTitle, strokeColorLabel, fillStatusLabel, 
                                           lineWidthLabel, gradientStatusLabel, shapeCountLabel, gradientColorsLabel,
                                           new javafx.scene.control.Separator(), gridControlBox,
                                           new javafx.scene.control.Separator(), colorSelectionBox);

        BorderPane root = new BorderPane();
        root.setTop(topContainer);
        root.setCenter(canvasStack);
        root.setRight(rightContainer);
        // Set event handlers after canvas is created
        
        // File Menu handlers
        newItem.setOnAction(e -> {
            java.util.Optional<javafx.scene.control.ButtonType> result = Utility.showConfirmationAlert(
                "New Drawing", 
                "Clear Canvas?", 
                "This will delete all shapes. Continue?"
            );
            if (result.isPresent() && result.get() == Utility.yesButtonType) {
                canvas.clearCanvas();
                mouseHandler.cancelDrawing();
                drawingPane.setStyle("-fx-background-color: transparent;");
                gc.clearRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());
                CheckerGrid(gc, gridCanvas.getWidth(), gridCanvas.getHeight());
                updatePropertyPanel.run();
            }
        });
        
        openItem.setOnAction(e -> {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Drawing");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Shape Files", "*.shape"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                canvas.clearCanvas();
                
                // Load shapes
                ArrayList<MyShapeWithException> shapes = (ArrayList<MyShapeWithException>) ois.readObject();
                for (MyShapeWithException shape : shapes) {
                    canvas.addShape(shape);
                }
                
                // setting have to load in the same order as saving
                isFilled = (Boolean) ois.readObject();
                ColorPicker.setUseGradient((Boolean) ois.readObject());
                ColorPicker.setGradientDirection((String) ois.readObject());
                UserstrokeColor = (String) ois.readObject();
                UserfillColor = (String) ois.readObject();
                UserStartgradientColor = (String) ois.readObject();
                UserEndgradientColor = (String) ois.readObject();
                gridLine = (Double) ois.readObject();

                updatePropertyPanel.run();

                Utility.showInfoAlert("Open Successful", "Drawing Loaded", "Your drawing has been loaded successfully.");
            } catch (IOException | ClassNotFoundException a) {
                Utility.showErrorAlert("Open Error", "Could not open drawing", a.getMessage());
            }
        }
    });
        
        saveItem.setOnAction(e -> {
                    FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Drawing");
        fileChooser.setInitialFileName("drawing.shape");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Shape Files", "*.shape"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                //save shapes
                oos.writeObject(canvas.getShapes());
                //save settings
                oos.writeObject(isFilled);
                oos.writeObject(ColorPicker.isUseGradient());
                oos.writeObject(ColorPicker.getGradientDirection());
                oos.writeObject(UserstrokeColor);
                oos.writeObject(UserfillColor);
                oos.writeObject(UserStartgradientColor);
                oos.writeObject(UserEndgradientColor);
                oos.writeObject(gridLine);

                Utility.showInfoAlert("Save Successful", "Drawing Saved", "Your drawing has been saved successfully.");
            } catch (IOException a) {
                Utility.showErrorAlert("Save Error", "Could not save drawing", a.getMessage());
            }
        }
        });
        
        exitItem.setOnAction(e -> {
            java.util.Optional<javafx.scene.control.ButtonType> result = Utility.showConfirmationAlert(
                "Exit", 
                "Exit Application?", 
                "Are you sure you want to exit?"
            );
            if (result.isPresent() && result.get() == Utility.yesButtonType) {
                System.exit(0);
            }
        });
        
        // Edit Menu handlers
        undoItem.setOnAction(e -> {
            canvas.undoShape();
            updatePropertyPanel.run();
        });

        redoItem.setOnAction(e -> {
            canvas.redoShape();
            updatePropertyPanel.run();
        });

        clearItem.setOnAction(e -> {
            java.util.Optional<javafx.scene.control.ButtonType> result = Utility.showConfirmationAlert(
                "Clear All Shapes", 
                "Clear All Shapes?", 
                "This will delete all shapes. Continue?"
            );
            if (result.isPresent() && result.get() == Utility.yesButtonType) {
                canvas.clearCanvas();
                mouseHandler.cancelDrawing();
                updatePropertyPanel.run();
            }
        });

        resetItem.setOnAction(e -> {
            java.util.Optional<javafx.scene.control.ButtonType> result = Utility.showConfirmationAlert(
                "Reset Settings", 
                "Reset All Settings?", 
                "This will reset all settings to default (gradient disabled, default colors)."
            );
            if (result.isPresent() && result.get() == Utility.yesButtonType) {
                ColorPicker.setUseGradient(false);
                ColorPicker.setGradientColors("white", "black");
                ColorPicker.setGradientDirection("HORIZONTAL");
                drawingPane.setStyle("-fx-background-color: transparent;");
                gc.clearRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());
                CheckerGrid(gc, gridCanvas.getWidth(), gridCanvas.getHeight());
                updatePropertyPanel.run();
                Utility.showInfoAlert("Reset", "Settings Reset", "All settings have been reset to defaults.");
            }
        });
        
        // Options Menu handlers
        colorItem.setOnAction(e -> {
            javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog("white");
            dialog.setTitle("Set Background Color");
            dialog.setHeaderText("Enter Background Color");
            dialog.setContentText("Color name (e.g., lightblue, lightgray, Checkerboard. Do not use Spaces or any special characters):");
            java.util.Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                try {
                    String input = result.get().trim();
                    
                    // STRICT VALIDATION - Empty not allowed for canvas background
                    if (input.isEmpty()) {
                        throw new IllegalArgumentException("Canvas background color cannot be empty");
                    }
                    
                    if (input.equalsIgnoreCase("checkerboard") || input.equalsIgnoreCase("checker") || input.equalsIgnoreCase("chase"))  {
                        drawingPane.setStyle("-fx-background-color: transparent;");
                        gc.clearRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());
                        CheckerGridDefault(gc);
                        Utility.showInfoAlert("Success", "Background Changed", "Checkerboard grid restored");
                    } else {
                        // STRICT - Must validate color, no defaults allowed
                        javafx.scene.paint.Color testColor = javafx.scene.paint.Color.web(input);
                        if (testColor == null) {
                            throw new IllegalArgumentException("Invalid color: " + input);
                        }
                        String backgroundColor = input.toLowerCase();
                        drawingPane.setStyle("-fx-background-color: " + backgroundColor + ";");
                        Utility.showInfoAlert("Success", "Background Color Changed", "Background color set to: " + input);
                    }
                } catch (IllegalArgumentException ex) {
                    Utility.showErrorAlert("Error", "Invalid Canvas Background Color", 
                        "Canvas background requires a valid CSS color.\n\n" +
                        "Valid options:\n" +
                        "• Color names: red, blue, green, lightblue, etc.\n" +
                        "• Hex codes: #FF0000, #00FF00, etc.\n" +
                        "• Special: 'checkerboard' for default grid\n\n" +
                        "Error: " + ex.getMessage());
                } catch (Exception ex) {
                    Utility.showErrorAlert("Error", "Unexpected Error", "Failed to set background: " + ex.getMessage());
                }
            }
        });

        outLineColorItem.setOnAction(e -> {
            javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog("white");
            dialog.setTitle("Set Outline Color");
            dialog.setHeaderText("Enter Outline Color");
            dialog.setContentText("Color name (e.g., lightblue, lightgray). Leave empty for default (black):");
            java.util.Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String input = result.get().trim();
                
                // LENIENT - Empty or invalid uses default
                if (input.isEmpty()) {
                    UserstrokeColor = "black";
                    updatePropertyPanel.run();
                    Utility.showInfoAlert("Outline Color", "Reset to Default", 
                        "Outline color reset to default (black).\n\nThis default will be applied to all new shapes.");
                    return;
                }
                
                try {
                    // Try to validate the color
                    javafx.scene.paint.Color.web(input);
                    UserstrokeColor = input.toLowerCase();
                    updatePropertyPanel.run();
                    Utility.showInfoAlert("Success", "Outline Color Changed", "Outline color set to: " + input);
                } catch (IllegalArgumentException ex) {
                    // LENIENT - Invalid color uses default and notifies user
                    UserstrokeColor = "black";
                    updatePropertyPanel.run();
                    Utility.showInfoAlert("Using Default Color", "Invalid Color - Default Applied", 
                        "'" + input + "' is not a valid color name.\n\n" +
                        "Outline color has been set to default (black).\n" +
                        "This will be applied to all new shapes.\n\n" +
                        "Valid examples: red, blue, green, lightblue, #FF0000");
                }
            }

        });
        
        filledItem.setOnAction(e -> {
            boolean newState = !isFilled;
            isFilled = newState;
            updatePropertyPanel.run();
            Utility.showInfoAlert("Fill Setting", "Fill Toggled", 
                "Shapes will now be drawn as " + (newState ? "filled" : "hollow"));
        });
        
        SetWidthItem.setOnAction(e -> { // Draw grid lines
            javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog("1.0");
            dialog.setTitle("Set Line Width");
            dialog.setHeaderText("Enter Line Width for New Shapes");
            dialog.setContentText("Line width (e.g., 1.0, 2.5):");
            java.util.Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                try {
                    String input = result.get().trim();
                    if (input.isEmpty()) {
                        Utility.showErrorAlert("Error", "Invalid Input", "Line width cannot be empty");
                        return;
                    }
                    double lineWidth = Double.parseDouble(input);
                    
                    // Comprehensive validation
                    if (Double.isNaN(lineWidth)) {
                        Utility.showErrorAlert("Error", "Invalid Line Width", "Line width must be a valid number");
                        return;
                    }
                    if (!Double.isFinite(lineWidth)) {
                        Utility.showErrorAlert("Error", "Invalid Line Width", "Line width cannot be infinite");
                        return;
                    }
                    if (lineWidth <= 0) {
                        Utility.showErrorAlert("Error", "Invalid Line Width", "Line width must be greater than 0");
                        return;
                    }
                    if (lineWidth > 100) {
                        Utility.showErrorAlert("Warning", "Large Line Width", "Line width of " + lineWidth + " is very large. Are you sure?");
                    }
                    
                    gridLine = lineWidth;
                    updatePropertyPanel.run();
                    Utility.showInfoAlert("Success", "Line Width Changed", "Line width set to: " + lineWidth);
                } catch (NumberFormatException ex) {
                    Utility.showErrorAlert("Error", "Invalid Line Width", "Please enter a valid number (e.g., 1.0, 2.5)");
                }
            }
        });

        GradientItem.setOnAction(e -> {
            // Toggle gradient on/off
            boolean newState = !ColorPicker.isUseGradient();
            ColorPicker.setUseGradient(newState);
            updatePropertyPanel.run();
            Utility.showInfoAlert("Gradient Setting", "Gradient Toggled", 
                "Gradient fill has been " + (newState ? "enabled" : "disabled"));
        });
        
        GradientColorsItem.setOnAction(e -> {
            javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog("white");
            dialog.setTitle("Set Gradient Colors");
            dialog.setHeaderText("Set Start Color");
            dialog.setContentText("Enter start color (e.g., red, blue, green). Leave empty for default (white):");
            java.util.Optional<String> startResult = dialog.showAndWait();
            if (startResult.isPresent()) {
                String startColor = startResult.get().trim();
                boolean startValid = true;
                boolean startWasEmpty = false;
                
                // LENIENT - Empty uses default
                if (startColor.isEmpty()) {
                    startColor = "white";
                    startWasEmpty = true;
                } else {
                    // Try to validate
                    try {
                        javafx.scene.paint.Color.web(startColor);
                    } catch (IllegalArgumentException ex) {
                        startColor = "white";
                        startValid = false;
                    }
                }
                
                dialog.getEditor().setText("black");
                dialog.setHeaderText("Set End Color");
                dialog.setContentText("Enter end color (e.g., red, blue, green). Leave empty for default (black):");
                java.util.Optional<String> endResult = dialog.showAndWait();
                if (endResult.isPresent()) {
                    String endColor = endResult.get().trim();
                    boolean endValid = true;
                    boolean endWasEmpty = false;
                    
                    // LENIENT - Empty uses default
                    if (endColor.isEmpty()) {
                        endColor = "black";
                        endWasEmpty = true;
                    } else {
                        // Try to validate
                        try {
                            javafx.scene.paint.Color.web(endColor);
                        } catch (IllegalArgumentException ex) {
                            endColor = "black";
                            endValid = false;
                        }
                    }
                    
                    // Update global variables and ColorPicker
                    UserStartgradientColor = startColor.toLowerCase();
                    UserEndgradientColor = endColor.toLowerCase();
                    ColorPicker.setGradientColors(UserStartgradientColor, UserEndgradientColor);
                    updatePropertyPanel.run();
                    
                    // Build notification message
                    String message = "Gradient colors set:\n" + startColor + " → " + endColor;
                    String title = "Gradient Colors Updated";
                    
                    if (!startValid || !endValid || startWasEmpty || endWasEmpty) {
                        message += "\n\n";
                        if (startWasEmpty) {
                            message += "• Start color empty - using default (white)\n";
                        } else if (!startValid) {
                            message += "• Invalid start color - using default (white)\n";
                        }
                        if (endWasEmpty) {
                            message += "• End color empty - using default (black)\n";
                        } else if (!endValid) {
                            message += "• Invalid end color - using default (black)\n";
                        }
                        message += "\nDefaults will be applied to new shapes with gradient enabled.";
                        title = "Gradient Colors - Defaults Applied";
                    } else {
                        message += "\n\nThese colors will be applied to new shapes when gradient is enabled.";
                    }
                    
                    Utility.showInfoAlert(title, "Gradient Configuration", message);
                }
            }
        });
        
        GradientDirectionItem.setOnAction(e -> {
            javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog("HORIZONTAL");
            dialog.setTitle("Set Gradient Direction");
            dialog.setHeaderText("Choose Gradient Direction");
            dialog.setContentText("Enter direction (HORIZONTAL, VERTICAL, or DIAGONAL):");
            java.util.Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String direction = result.get().toUpperCase();
                if (direction.equals("HORIZONTAL") || direction.equals("VERTICAL") || direction.equals("DIAGONAL")) {
                    ColorPicker.setGradientDirection(direction);
                    Utility.showInfoAlert("Gradient Direction", "Direction Updated", 
                        "Gradient direction set to: " + direction);
                } else {
                    Utility.showErrorAlert("Error", "Invalid Direction", 
                        "Please enter HORIZONTAL, VERTICAL, or DIAGONAL");
                }
            }
        });
        
        // Help Menu handlers
        howToUseItem.setOnAction(e -> {
            String instructions = 
                "=== How to Use Shape Management GUI ===\n\n" +
                "DRAWING SHAPES:\n" +
                "1. Click a shape button (Draw Line, Circle, etc.)\n" +
                "2. Click on canvas to place points:\n" +
                "   - Line: 2 clicks (start, end)\n" +
                "   - Circle/Rectangle/Square: 2 clicks (define bounding box)\n" +
                "   - Triangle: 3 clicks (three vertices)\n" +
                "3. Shape is automatically created when all points are placed\n\n" +
                "MENU OPTIONS:\n" +
                "• File → New Drawing: Clear all shapes\n" +
                "• File → Exit: Close application\n" +
                "• Edit → Undo: Remove last shape\n" +
                "• Edit → Redo: Restore last removed shape\n" +
                "• Edit → Clear All: Remove all shapes\n" +
                "• Options → Set Background Color: Change canvas color\n" +
                "• Options → Enable Gradient: Toggle gradient fill for shapes\n\n" +
                "TIPS:\n" +
                "- Coordinate preview shows current mouse position\n" +
                "- Cancel button stops current drawing operation\n" +
                "- All shapes default to black color";
            
            Utility.showInfoAlert("Instructions", "How to Use", instructions);
        });
        
        // Create scene and stage
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Shape Management GUI - By Tekhour Khov");
        stage.setScene(scene);
        stage.setMaximized(true);  // Start maximized
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
