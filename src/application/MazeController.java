package application;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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

public class MazeController {
		
		// FXML nodes (visual elements)
		@FXML
		private Button exit, clear;
	    
	    @FXML
	    private Canvas mazeCanvas;
	    
	    @FXML
	    private Label shortestPathLabel;
	    
	    /* maze is a mazeSolver used to generate and solve mazes
	     * mazeVisualizer is used for updating the frames of the canvas.
	     * shortestPath is used to store the shortest path from the origin to a given point, which is chosen by the user
	     */
		MazeSolver maze;
		double[][] mazeVisualizer;
		int shortestPath;
		
		
		static double UNIT_SIZE = 10; 														// UNIT_SIZE = (CANVAS_SIZE / tiles)
		static double CANVAS_SIZE; 															// Length/Width of canvas
		static double lineOffset = 0.5;														// Offset of lines to avoid visual glitches
		static final Color COLOR_WALL = Color.BLACK;  										// Color of wall tiles
		static final Color COLOR_EMPTY = Color.rgb(230, 230, 230);							// Color of empty tiles
		
		public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();	// User's screen size
		static int SCREEN_WIDTH = (int) screenSize.getWidth();								// User's screen width
		static int SCREEN_HEIGHT = (int) screenSize.getHeight();							// User's screen height
		AnimationTimer timer;																// Timer for updating the screen
	
	
	    @FXML
	    void initialize()
	    {
	    	System.out.println("Initialized maze controller.");
	    	
	    	CANVAS_SIZE = mazeCanvas.getHeight();
	  	  	GraphicsContext gc = mazeCanvas.getGraphicsContext2D();
	  	  

	  	  	// Mouse click events
	    	  mazeCanvas.setOnMouseClicked( new EventHandler<MouseEvent>() {
	          	@Override
	          	public void handle(MouseEvent event)
	          	{
	          		
	          		/* User picks the height/width of maze using the default 50x50 grid by clicking somewhere on the 50x50 grid. 
	          		 * If the user chooses a grid that is smaller than 50x50, the UNIT_SIZE is adjusted such that...
	          		 * 
	          		 * 					(Unit size) * (Number of tiles per side) = (Canvas Size)
	          		 * 
	          		 * This way, the entire canvas will be completely filled with tiles.
	          		 */
	          		double x = event.getSceneX();
	          		double y = event.getSceneY();
	          		System.out.println(x + ", " + y);
	          		
	          		
	          		if (maze == null) {
	          			int length = getAdjustedLength(x, y);
	          			if (length % 2 == 0)
	          				lineOffset = 0.5;
	          			else
	          				lineOffset = 0;
	          			
	          			
	          	    	System.out.println("Unit size: " + UNIT_SIZE);
	          			makeMaze(length);
	          			
	          		}
	          		
	          		/* If the user clicks anywhere on the canvas during the animation of the maze, the animation
	          		 * is skipped by clearing all the sequences of the maze except the last one.
	          		 */
	          		else if (maze != null && !maze.sequencesAreEmpty())
	          		{
	          			maze.skipSequences();
	          		}
	          		
	          		/* If the animation of maze generation is finished, the user can choose a point anywhere on the grid which will
	          		 * reveal the shortest path from the origin to that chosen point.
	          		 */
	          		else
	          		{
	          	    	int rows = (int) ((y-70)/UNIT_SIZE);
	          	    	int cols = (int) ((x-50)/UNIT_SIZE);
	          			
	          	    	try {
	          	    		shortestPath = maze.shortestPath(rows, cols);
	          	    		shortestPathLabel.setText("Shortest Path: " + shortestPath + " tiles");
	          	    	} catch (IllegalMazeException e)
	          	    	{
	          	    		System.out.println("Invalid cell");
	          	    	}
	          		}
	          	}
	          });
	    	  
	    	  // Draw a preview of the selected maze if the user moves the cursor when a maze has not been built yet
	    	  mazeCanvas.setOnMouseMoved(new EventHandler<MouseEvent>() {
	    		  
	    		  @Override
	    		  public void handle(MouseEvent event)
	    		  {
	          		double x = event.getSceneX();
	          		double y = event.getSceneY();
	          		
	          		if (maze == null)
	          		{
	          			int length = getAdjustedLength(x, y);
		    			
	          			drawPreview(gc, length);
	          			drawLines(gc);
	          		}
	    	  }
	    	  });
	    	  
	    	  // Draw a blank canvas if the user moves the cursor outside the canvas
	    	  mazeCanvas.setOnMouseExited(new EventHandler<MouseEvent>() {
	    		  
	    		  @Override
	    		  public void handle(MouseEvent event)
	    		  {
	    			  System.out.println("Mouse outside window.");
	    			  gc.setFill(Color.GRAY);
	    			  gc.fillRect(0, 0, mazeCanvas.getWidth(), mazeCanvas.getHeight());
	    			  drawLines(gc);
	 
	    		  }
	    		  
	    	  });
	      	
	    	  drawLines(gc);
	      	
	    }
	    
	    // Returns the adjusted length of rows/cols of the maze and updates the UNIT_SIZE so to completely fill the 500x500 canvas
	    // Uses the inputted x and y coordinates of the mouse event to determine 
	    private int getAdjustedLength(double x, double y)
	    {
		    	int rows = (int) ((y-50)/UNIT_SIZE);
		    	int cols = (int) ((x-50)/UNIT_SIZE);
		    	int length;
		    	
		    	if (rows < cols)
		    		length = cols;
		    	else
		    		length = rows;
	
		    	
		    	return length;
	    }
		
	    // Create the maze and start the animation timer
	    private void makeMaze(int length)
	    {
	    	
	    	System.out.println("length: " + length);
	      	timer = new MyTimer();
	      	
	      	this.maze = new MazeSolver(length, length);
	      	this.maze.createMaze();
	      	
	      	
	      	System.out.println("Line offset: " + this.lineOffset);
	      	UNIT_SIZE = (CANVAS_SIZE/  length);
	      	
	      	timer.start();
	    }
	    
	    // Draw a preview of the grid size selected by the user
	    private void drawPreview(GraphicsContext gc, int length)
	    {
			gc.setFill(Color.GRAY);
			gc.fillRect(0, 0, mazeCanvas.getWidth(), mazeCanvas.getHeight());
	
	    	for (int r = 0; r < length; r++) 
	    	{
	    		for (int c = 0; c < length; c++)
	    		{
	    			gc.setFill(Color.WHITE);
	    			gc.fillRect(r * UNIT_SIZE, c * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
	    		}
	    	}
	    	
	    	gc.setStroke(COLOR_WALL);
	    	gc.setLineWidth(3);
			gc.strokeLine(length*UNIT_SIZE + lineOffset, 0 + lineOffset, length*UNIT_SIZE, length*UNIT_SIZE);
			gc.strokeLine(0 + lineOffset, length*UNIT_SIZE + lineOffset, length*UNIT_SIZE, length*UNIT_SIZE);

	    }
	
	    // Timer to update the animation on a frame-by-frame basis
	    private class MyTimer extends AnimationTimer{
	    	
	    	GraphicsContext gc = mazeCanvas.getGraphicsContext2D();
	    	
			@Override
			public void handle(long arg0) {
				update(gc);
				
			}
			
			private void update(GraphicsContext gc) {
				
				// make a white canvas
				gc.setFill(Color.WHITE);
				gc.fillRect(0, 0, mazeCanvas.getWidth(), mazeCanvas.getHeight());
				
				// origin tile
				gc.setFill(Color.rgb(235, 10, 150));
				gc.fillRect(0, 0, UNIT_SIZE, UNIT_SIZE);
				
				// If there is a new sequence, get the sequence and store it into the mazeVisualizer matrix
				if (!maze.sequencesAreEmpty())
					mazeVisualizer = maze.getMatrixSequence();
	
				// Draw each tile in mazeVisualizer
					for (int  r = 0; r < mazeVisualizer.length; r++)
					{
						for (int c = 0; c < mazeVisualizer[0].length; c++)
						{
							gc.fillRect(c * UNIT_SIZE, r * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
							double current = mazeVisualizer[r][c];
							
							// Values 0-1 range from white to black
							if (current == 0)
							{
								// Draw
								gc.setFill(COLOR_EMPTY);
								gc.fillRect(c * UNIT_SIZE, r * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
							}

							// 
							if (current >= 0 && current <= 1)
							{
								gc.setFill(Color.rgb((int) (255 - (current * 255)), (int) (255 - (current * 255)), (int) (255 - (current * 255))));
								gc.fillRect(c * UNIT_SIZE, r * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
							}
							else if (current == 2)
							{
								// Draw solution
								gc.setFill(Color.rgb(100,200,255));
								gc.fillRect(c * UNIT_SIZE, r * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
							}
						}
					}
					
					drawLines(gc);
			}
	    	
	    }
	    
	    // Draw grid lines
	    private void drawLines(GraphicsContext gc)
	    {
			for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++)
			{
				gc.setStroke(COLOR_WALL);
				gc.setLineWidth(1);
				gc.strokeLine(i*UNIT_SIZE + lineOffset, 0 + lineOffset, i*UNIT_SIZE, SCREEN_HEIGHT);
				gc.strokeLine(0 + lineOffset, i*UNIT_SIZE + lineOffset, SCREEN_HEIGHT, i*UNIT_SIZE);
			}
	    }
	    
	    // Resets this scene
		private void reset()
		{
			if (timer != null)
				timer.stop();
	    	maze = null;
	    	mazeVisualizer = null;
	    	UNIT_SIZE = 10;
	    	lineOffset = 0.5;
	    	shortestPath = 0;
	    	shortestPathLabel.setText("");
		}

		// When the clear button is pressed, clear the maze
		@FXML
		void clear()
		{
			reset();
			GraphicsContext gc = mazeCanvas.getGraphicsContext2D();
			gc.clearRect(0, 0, mazeCanvas.getWidth(), mazeCanvas.getHeight());
		}
		
		// When the exit button is pressed, exit to the main menu
	    @FXML
	    void exitMaze() throws IOException
	    {
	    	System.out.println("Exiting.");
	    	
	    	// Reset canvas
	    	reset();
	    	
	    	// Exit to the main menu
	    	Stage stage = (Stage) this.exit.getScene().getWindow();
	    	FXMLLoader loader = new FXMLLoader(this.getClass().getResource("Menu.fxml"));
	    	loader.setController(new MenuController());
	    	AnchorPane root = (AnchorPane) loader.load();
	    	
	    	Scene scene = new Scene(root, 600, 600);
	    	scene.getStylesheets().add(this.getClass().getResource("menu.css").toExternalForm());
	    	
	    	stage.setScene(scene);
	    	stage.show();
	    	
	    }
}

