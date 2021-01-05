package application;
	

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	static  int SCREEN_WIDTH;
	static  int SCREEN_HEIGHT;
	static final int UNIT_SIZE = 10;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT/UNIT_SIZE);
	static final int DELAY = 1;
	
	// Initialize the main menu
	@Override
	public void start(Stage primaryStage) {
		try {
			// Load a font
			Font.loadFont(getClass().getResourceAsStream("coolvetica.ttf"), 14);
			
			// Make a new FXMLLoader and fetch the main menu FXML file
	    	FXMLLoader loader = new FXMLLoader(Main.class.getResource("Menu.fxml"));
	    	
	    	// Set the loader's controller to be a new instance of MenuController
	    	loader.setController(new MenuController());
	    	
	    	// Load the FXML and cast it to type AnchorPane
	    	AnchorPane root =  (AnchorPane) loader.load();
	    	
	    	// Create a new scene of size 600 x 600 resolution
			Scene scene = new Scene(root,600,600);
			
			// Assign a CSS stylesheet to the scene
			scene.getStylesheets().add(getClass().getResource("menu.css").toExternalForm());
			
			// Make a new window (stage) and have the scene be inside the window
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Maze Solver");
			
			// Open the window
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
