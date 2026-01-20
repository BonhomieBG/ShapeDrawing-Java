// File name: MyShape.java
// Description: This part of the code responsible for implement colorable interface
// Challenges: None
// 
// Time Spent: About 30 seconds
//
// Revision History
// Date: By: Action
// 09/14/2025 Created MyShape class
// 09/19/2025 Implemented Colorable interface
// 12/02/2025 Implement JavaFX Shape conversion method

import java.io.Serializable;

public abstract class MyShapeWithException implements Colorable, Serializable {
    private static final long serialVersionUID = 1L;
    private String Fillcolor;
    private String strokeColor;
    private final String defaulFillColor = "transparent";
    private final String defaulStrokeColor = "black";
    private boolean stroke = true;
    protected double gridLineWidth;

    public MyShapeWithException() {
        setFillColor(defaulFillColor);
        setStrokeColor(defaulStrokeColor);
    }

    public MyShapeWithException(String color) throws IllegalArgumentException {
        this();
        if (color!=null) {
            setFillColor(color.toLowerCase().trim());
            setStrokeColor(color.toLowerCase().trim());
        }
    }

    public MyShapeWithException(String Fillcolor, String strokeColor) throws IllegalArgumentException {
        this();
        if (Fillcolor!=null) {
            setFillColor(Fillcolor.toLowerCase().trim());
            setStrokeColor(strokeColor.toLowerCase().trim());
        }
    }

    public abstract String getName();

    public String getFillColor(){
        return (Fillcolor == null || Fillcolor.isEmpty())? "black": Fillcolor;
    }

    public String getStrokeColor(){
        return strokeColor;
    }

    public void setFillColor(String color){
        // If null or empty, use default
        if (color == null || color.trim().isEmpty()) {
            this.Fillcolor = defaulFillColor;
            return;
        }
        
        String trimmedColor = color.trim().toLowerCase();
        
        // Check if color contains only letters
        if (!trimmedColor.matches("[a-zA-Z]+")) {
            // Use default for invalid format
            this.Fillcolor = defaulFillColor;
            return;
        }
        
        // Validate that JavaFX can parse this color
        try {
            javafx.scene.paint.Color.web(trimmedColor);
            this.Fillcolor = trimmedColor;
        } catch (IllegalArgumentException e) {
            // Use default for invalid color name
            this.Fillcolor = defaulFillColor;
        }
    }

    public javafx.scene.paint.Paint setStrokeColor(String color){
        // If null or empty, use default
        if (color == null || color.trim().isEmpty()) {
            strokeColor = defaulStrokeColor;
            return javafx.scene.paint.Color.web(defaulStrokeColor);
        }
        
        String trimmedColor = color.trim().toLowerCase();
        
        try {
            javafx.scene.paint.Color testColor = javafx.scene.paint.Color.web(trimmedColor);
            strokeColor = trimmedColor;
            return testColor;
        } catch (IllegalArgumentException e) {
            // Use default for invalid color
            strokeColor = defaulStrokeColor;
            return javafx.scene.paint.Color.web(defaulStrokeColor);
        }
    }

    public javafx.scene.paint.Paint AutoSetStroke(){
        try {
            return javafx.scene.paint.Color.web(getStrokeColor());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid color name: '" + getStrokeColor() + "'. Please use a valid CSS color name.");
        }
    }

    public void setStrokeStatus(boolean stroke){
        if (stroke == false){
            this.stroke = false;
        }
        this.stroke = true;
    }

    public boolean getStrokeStatus(){
        return stroke;
    }

    public boolean isFilled(){
        if (getFillColor().isBlank()) return false;
        else return true;
    }

    public boolean isStroke(){
        if (getStrokeColor().isBlank()) return false;
        else return true;
    }
    
    public double getGridLineWidth(){
        return gridLineWidth;
    }

    public void setGridLineWidth(double width){
        this.gridLineWidth = width;
    }

    public String getDefaultFillColor(){
        return defaulFillColor;
    }

    public String getDefaultStrokeColor(){
        return defaulStrokeColor;
    }

    public abstract javafx.scene.shape.Shape toFXShape(); // Convert to JavaFX Shape

    public String toString(){
        return getName()+ " [Color= " + getFillColor() + "]";
    } 
}