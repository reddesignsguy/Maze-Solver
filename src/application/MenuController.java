package application;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.fxml.*;

import java.awt.Dimension;
import java.awt.Toolkit;
//import java.awt.Color;
import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class MenuController {
	
    @FXML
    private Button start;
  

    
    // Play the maze solver
    @FXML
    void start(ActionEvent event) throws IOException {
    	
    	// Make a new loader for the maze's FXML file
    	FXMLLoader loader = new FXMLLoader(Main.class.getResource("MazePage.fxml"));
    	
    	// Set the controller of the loader to a new instance of MazeController
    	loader.setController(new MazeController());
    	
    	// Load the FXML and cast it to BorderPane
    	BorderPane root =  (BorderPane) loader.load();
    	
    	// Make a new scene of size 600 x 600
		Scene scene = new Scene(root, 600, 600);
		
		// Create a new window (stage)
		Stage stage = (Stage) start.getScene().getWindow();
		stage.setResizable(false);
		
		// Set the window's scene
		stage.setScene(scene);
    }    
    
    

}

