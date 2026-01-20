// File name: Utility.java
// Description: Handle all Utility functions for the graphical user interface to display correctly
// Challenges: #
// 
// Time Spent: #
//
// Revision History
// Date: By: Action
// 12/9/2025 Created the Utility class
// 
import javafx.scene.control.ButtonType;

import java.util.Optional;
import javafx.scene.control.Alert;

public class Utility {
    // Custom general buttons
    public static ButtonType yesButtonType = new ButtonType("Yes");
    public static ButtonType noButtonType = new ButtonType("No");
    public static ButtonType okButtonType = new ButtonType("OK");
    public static ButtonType cancelButtonType = new ButtonType("Cancel");
    public static ButtonType applyButtonType = new ButtonType("Apply");

    public static Optional<ButtonType> showConfirmationAlert(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, content, yesButtonType, noButtonType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        return alert.showAndWait();
    }

    public static Optional<ButtonType> showWarningAlert(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }

    public static Optional<ButtonType> showErrorAlert(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }

    public static Optional<ButtonType> showInfoAlert(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }

    public static Optional<ButtonType> showConfirmationAlert(String conformation){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(conformation);
        return alert.showAndWait();
    }

    public static Optional<ButtonType> showWarningAlert(String warning){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(warning);
        return alert.showAndWait();
    }

    public static Optional<ButtonType> showErrorAlert(String error){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(error);
        return alert.showAndWait();
    }

    public static Optional<ButtonType> showInfoAlert(String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(content);
        return alert.showAndWait();
    }

}