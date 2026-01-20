// File name: ColorPicker.java
// Description: Handle gradient settings for shapes
// 
// Time Spent: #
// Challenges: JavaFX is completely new and need to understand how it is work. Need time to learn about each class that it has.
//
// Revision History
// Date: By: Action
// 12/13/2025 Tekhour Khov Created the ColorPicker class
// This class manages gradient settings only (solid color handled by MyShapeWithException)

import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;

public class ColorPicker{
    // Global gradient settings
    private static Runnable updateCallback; // Callback to update UI when settings change
    private static boolean useGradient = false;
    private static String gradientStartColor = "white";
    private static String gradientEndColor = "black";
    private static final String GDefaultStarts = "white";
    private static final String GDefaultEnds = "black";
    private static String gradientDirection = "HORIZONTAL"; // HORIZONTAL, VERTICAL, DIAGONAL
    
    public static void setUpdateCallback(Runnable callback) {
        updateCallback = callback;
    }


    public static boolean isUseGradient() {
        return useGradient;
    }
    
    public static void setUseGradient(boolean gradient) {
        useGradient = gradient;
    }
    
    public static void setGradientColors(String startColor, String endColor) {
        // Use defaults if colors are null or empty
        if (startColor == null || startColor.trim().isEmpty()) {
            gradientStartColor = GDefaultStarts;
        } else {
            try {
                // Validate color
                Color.web(startColor);
                gradientStartColor = startColor.trim().toLowerCase();
            } catch (IllegalArgumentException e) {
                // Use default for invalid color
                gradientStartColor = GDefaultStarts;
            }
        }
        
        if (endColor == null || endColor.trim().isEmpty()) {
            gradientEndColor = GDefaultEnds;
        } else {
            try {
                // Validate color
                Color.web(endColor);
                gradientEndColor = endColor.trim().toLowerCase();
            } catch (IllegalArgumentException e) {
                // Use default for invalid color
                gradientEndColor = GDefaultEnds;
            }
        }
        
        if (updateCallback != null) updateCallback.run();
    }
    
    public static void setGradientDirection(String direction) {
        gradientDirection = direction;
    }

    public static String getGradientDirection() {
        return gradientDirection;
    }

    public static String getGradientStartColor() {
        return gradientStartColor;
    }
    
    public static String getGradientEndColor() {
        return gradientEndColor;
    }

    public static void resetGradientColors() {
        gradientStartColor = GDefaultStarts;
        gradientEndColor = GDefaultEnds;
        if (updateCallback != null) updateCallback.run();
    }

    // Create gradient for JavaFX shapes
    public static LinearGradient createGradient(String color, String color2) {
        // Use defaults if colors are null or invalid
        String startColor = (color == null || color.trim().isEmpty()) ? GDefaultStarts : color;
        String endColor = (color2 == null || color2.trim().isEmpty()) ? GDefaultEnds : color2;
        
        try {
            Stop[] stops = new Stop[] {
                new Stop(0, Color.web(startColor)),
                new Stop(1, Color.web(endColor))
            };
            
            switch (gradientDirection) {
                case "HORIZONTAL":
                    return new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
                case "VERTICAL":
                    return new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
                case "DIAGONAL":
                    return new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
                default:
                    return new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
            }
        } catch (IllegalArgumentException e) {
            // If colors are invalid, use defaults
            Stop[] stops = new Stop[] {
                new Stop(0, Color.web(GDefaultStarts)),
                new Stop(1, Color.web(GDefaultEnds))
            };
            return new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
        }
    }

    public static LinearGradient createGradient() {
        // Use defaults if current colors are null or invalid
        String startColor = (gradientStartColor == null || gradientStartColor.trim().isEmpty()) ? GDefaultStarts : gradientStartColor;
        String endColor = (gradientEndColor == null || gradientEndColor.trim().isEmpty()) ? GDefaultEnds : gradientEndColor;
        
        try {
            Stop[] stops = new Stop[] {
                new Stop(0, Color.web(startColor)),
                new Stop(1, Color.web(endColor))
            };
            
            switch (gradientDirection) {
                case "HORIZONTAL":
                    return new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
                case "VERTICAL":
                    return new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
                case "DIAGONAL":
                    return new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
                default:
                    return new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
            }
        } catch (IllegalArgumentException e) {
            // If colors are invalid, use defaults
            Stop[] stops = new Stop[] {
                new Stop(0, Color.web(GDefaultStarts)),
                new Stop(1, Color.web(GDefaultEnds))
            };
            return new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
        }
    }

    public static void resetGradientSettings() {
        useGradient = false;
        gradientStartColor = "white";
        gradientEndColor = "black";
        gradientDirection = "HORIZONTAL";
    }
}