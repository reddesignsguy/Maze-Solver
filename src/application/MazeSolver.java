package application;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.List;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class MazeSolver {

	// 						* matrixSequences contains the sequences of the maze for maze visualization
	// 						* A sequence is a "snapshot" of every change in the maze
	LinkedList<double[][]> matrixSequences = new LinkedList<double[][]>();
	
	/* mazeVisualizer 
	 * 
	 */
	double[][] mazeVisualizer;
	
	// 	A maze of ints:
	// 		* 0 represents an empty cell
	// 		* 1 represents a wall
	// 		* 2 represents a cell included in the shortestPath to a given exit
	int[][] maze;


	public MazeSolver() {
		this.maze = new int[50][50];
		this.mazeVisualizer = new double[50][50];
		this.createEmptyMaze();
	}
	
	public MazeSolver(int rows, int cols)
	{
		// Rows and columns must be <= 100
		try {
			if (rows > 100 || cols > 100)
				throw new IllegalMazeException("Maze length or width is greater than 100");
		} 
		catch (IllegalMazeException e)
		{
			e.printStackTrace();
		}
		
		this.maze = new int[rows][cols];
		this.mazeVisualizer = new double[rows][cols];
		this.createEmptyMaze();
	}


	// Creates matrix full of walls (int = 1)
	private void createEmptyMaze()
	{
		for (int r = 0; r < maze.length; r++)
			for (int c = 0; c< maze[r].length; c++)
				maze[r][c] = 1;
		
		this.mazeVisualizer = toDoubleMatrix(maze);
		// First sequence to be visualized
		System.out.println(this.matrixSequences.add(copyMatrix(mazeVisualizer)));
		
		maze[0][0] = 0;
	}
	
	// Converts an integer array to a double array
	private double[][] toDoubleMatrix(int[][] matrix)
	{
		double[][] mazeDouble = new double[matrix.length][matrix[0].length];
		for (int r = 0; r < matrix.length; r++) 
			for (int c = 0; c< matrix[r].length; c++) 
				mazeDouble[r][c] = matrix[r][c];
		
		return mazeDouble;
	}
	
	// Creates a maze of paths that are one cell thick
	public void createMaze()
	{
		System.out.println("Starting maze building... ");
		recurse(0, 0);
		while (this.interpolateAndAddSequence())
			this.interpolateAndAddSequence();
		
	}
	
	// Recurses up, down, right, and left from the current cell such that the maze paths are one cell thick
	private void recurse(int r, int c)
	{
		// Mark current cell as undergoing animation in mazeVisualizer
		// as cells undergoing animation are values < 1
		mazeVisualizer[r][c] = 0.9;

		// Add current sequence to reflect change
		interpolateAndAddSequence();
		
		// Get a random list of directions
		// 1 = up, 2 = down, 3 = right, 4 = left
		int[] directions = getRandomDirections();
		
		// Check if we can recurse up, down, left, and right
		for (int i = 0; i < directions.length; i++)
		{
				
			switch(directions[i])
			{
				case 1: // UP
					if (r-2 < 0)
						continue;

					// If the two cells above haven't been visited, mark those two cells
					// as visited and recurse on the second cell
					 
					// Since each recurse travels two steps, every cluster of wall cells and visited
					// cells will never be greater than one cell thick

					if (maze[r-1][c] != 0 && maze[r-2][c] != 0)
					{
						// Mark one cell above as visited in maze
						maze[r-1][c] = 0;
						
						// Mark one cell above as undergoing animation in mazeVisualizer
						mazeVisualizer[r-1][c] = 0.99;
						
						// Add current sequence to reflect change in the one cell above
						interpolateAndAddSequence();
						
						// Mark the second cell above as visited in maze
						maze[r-2][c] = 0;
						
						// Recurse on the second cell above
						recurse(r-2, c);
					}
					break;
				
				case 2: // DOWN
					if (r+2 >= maze.length)
						continue;

					if (maze[r+1][c] != 0 && maze[r+2][c] != 0)
					{
						maze[r+1][c] = 0;
						mazeVisualizer[r+1][c] = 0.99;
						interpolateAndAddSequence();
						
						maze[r+2][c] = 0;
						recurse(r+2, c);
					}
					break;
				
				case 3: // RIGHT
					if (c+2 >= maze.length)
						continue;
					
					if (maze[r][c+1] != 0 && maze[r][c+2] != 0)
					{
						maze[r][c+1] = 0;
						mazeVisualizer[r][c+1] = 0.99;
						interpolateAndAddSequence();
						
						maze[r][c+2] = 0;
						recurse(r, c+2);
					}
					break;
				
				case 4: // LEFT
					if (c-2 < 0)
						continue;
					
					if (maze[r][c-1] != 0 && maze[r][c-2] != 0)
					{
						maze[r][c-1] = 0;
						mazeVisualizer[r][c-1] = 0.99;
						interpolateAndAddSequence();
						
						maze[r][c-2] = 0;
						recurse(r, c-2);
					}
					break;
			}
		}
		
	}
	

	// Get a random order of directions
	public int[] getRandomDirections()
	{
		// Create an empty primitve array for four directions
		int[] randomDirections = new int[4];
		
		// Create a new arraylist of ints and shuffle
		// Each number represent each type of direction
		ArrayList<Integer> tempDirections = new ArrayList<Integer>();
		tempDirections.add(1);
		tempDirections.add(2);
		tempDirections.add(3);
		tempDirections.add(4);
		Collections.shuffle(tempDirections);
		
		// Transfer arraylist to primitive array
		for (int i = 0; i < tempDirections.size(); i++)
			randomDirections[i] = tempDirections.get(i);
			
		return randomDirections;
	}
	
	
	private void printMatrix(double[][] matrix)
	{
		for (int r = 0; r < 4; r++)
		{
			for (int c = 0; c< 4; c++)
			{
				System.out.print(matrix[r][c] + ", ");
			}
			System.out.println("");
		}
	}
	
	public int[][] getMaze()
	{
		return this.maze;
	}
	
	
	public double[][] getMatrixSequence()
	{
		// 
		if (!this.matrixSequences.isEmpty())
			return this.matrixSequences.poll();
		return null;
	}
	
	public boolean sequencesAreEmpty()
	{
		return this.matrixSequences.isEmpty();
	}

	// The two methods return a copy of a given matrix
	private int[][] copyMatrix(int[][] thatMaze)
	{
		int[][] maze = new int[thatMaze.length][thatMaze[0].length];
		for (int r = 0; r < thatMaze.length; r++)
		{
			for (int c = 0; c< thatMaze[r].length; c++)
			{
				maze[r][c] = thatMaze[r][c];
			}
		}
		
		return maze;
	}
	
	private double[][] copyMatrix(double[][] thatMaze)
	{
		double[][] maze = new double[thatMaze.length][thatMaze[0].length];
		for (int r = 0; r < thatMaze.length; r++)
		{
			for (int c = 0; c< thatMaze[r].length; c++)
			{
				maze[r][c] = thatMaze[r][c];
			}
		}
		
		return maze;
	}


	// Adds a sequence with the highlighted shortest path
	private void addSolutionSequence(Coordinate tail)
	{
		// solutionMatrix will be returned
		int[][] solutionMatrix = copyMatrix(this.maze);
		
		// Get position (r,c) from coordinate nodes starting from tail back to head
		// Set each position to be 2
		Coordinate coordinate = tail;
		while (coordinate != null)
		{
			System.out.println("Added");
			solutionMatrix[coordinate.getRow()][coordinate.getCol()] = 2;
			coordinate = coordinate.getPrev();
		}
		
		// Send the solution sequence to the sequences queue
		this.matrixSequences.add(toDoubleMatrix(solutionMatrix));
		
	}
	
	/*
	 * Returns the shortest path from the entrance to a given exit using Depth First Search (DFS)
	 * Tracks the cells of the shortest path using Coordinate nodes, which are used to find the cells
	 * in mazeVisualizer to set to double value = 3
	 */
	public int shortestPath(int rExit, int cExit) throws IllegalMazeException
	{
		// Retrieve a copy of the maze
		int[][] solutionMaze = copyMatrix(this.maze);
		
		// Create the head Coordinate, which will be related by subsequent Coordinate all the way to the tail. Each 
		// coordinate is a node that is related to a previous node
		Coordinate head = new Coordinate(0,0, null);
		int shortestPath = recurseShortestPath(rExit, cExit, 0, 0, 0, head, solutionMaze);
		
		
		// Check if maze is unsolvable (shortest path is 10001)
			if (shortestPath == 10001)
				throw new IllegalMazeException("Maze is unsolvable");
		
		
		return shortestPath;
	}
	
	/* 
	 *  interpolateAndAddSequence() continues interpolation of cells that are currently undergoing animation
	 *  1) If a cell in mazeVisualizer is less than 1, the cell is considered to be undergoing
	 *  animation. (as 1 represents a wall and 0 represents an empty cell)
	 *  
	 *  2) The method returns false if there are no cells currently undergoing animation
	 */
	
	private boolean interpolateAndAddSequence()
	{
		
//		System.out.println("Interpolating -----------------------------------");
		
		boolean changeInMatrix = false;
		for (int r = 0; r < mazeVisualizer.length; r++)
		{
			for (int c = 0; c < mazeVisualizer[0].length; c++)
			{
				// If there is a cell undergoing animation, there has been a change in the matrix
				if ((mazeVisualizer[r][c] < 1 && mazeVisualizer[r][c] != 0) || mazeVisualizer[r][c] == 3)
					changeInMatrix = true;
				
				// 
				if (mazeVisualizer[r][c] == 0.99) {
					mazeVisualizer[r][c] = 0.9;
				}
				else if (mazeVisualizer[r][c] < 1 && mazeVisualizer[r][c]> 0)
					mazeVisualizer[r][c] = Math.round((mazeVisualizer[r][c]- 0.1) * 100.0) / 100.0;
				}
			}
		
		// If there was no cell undergoing animation after checking the entire matrix, then
		// return false
		if (changeInMatrix == false)
			return false;

		this.matrixSequences.add(copyMatrix(this.mazeVisualizer));

		return true;
	}
	
	
	// Return the shortest path from the origin to a given point on the maze
	public int recurseShortestPath(int rExit, int cExit, int r, int c, int steps, Coordinate prev,int[][] solutionMaze)
	{

		// System.out.println("--------------------");
		// System.out.println("Target: " + rExit + ", " + cExit + " | Current: " + r + ", " + c);		
		
		// End recurse if current cell is out of bounds
		if (r == -1 || r == solutionMaze.length || c == -1 || c == solutionMaze[0].length) {
		//	System.out.println("Out of bounds");
			return 10001;
		}
			
		// End recurse if the cell is a wall (int = 1)
		if (solutionMaze[r][c] == 1) {
			// System.out.println("Hit wall");
			return 10001;
		}
		
		// End recurse if the cell has been stepped on before (int = 2)
		if (solutionMaze[r][c] == 2) {
			// System.out.println("Been to this tile before");
			return 10001;
		}

		// Cell is an empty cell, construct new Coordinate related to the previous node
		Coordinate current = new Coordinate(r, c, prev);

		// End recurse and upload the sequence of the solution if tile is the end point;
		if (rExit == r && cExit == c) {
			// System.out.println("Reached the end!");
			addSolutionSequence(current);
			return steps;
		}
		
		// Mark as visited if exit hasn't been reached yet
		solutionMaze[r][c] = 2;

		
		// Return the smallest number of steps retrieved by each recursive call
		return Integer.min( 
				Integer.min(recurseShortestPath(rExit, cExit, r-1, c, steps + 1, current, solutionMaze), recurseShortestPath(rExit, cExit, r+1, c, steps + 1, current, solutionMaze)),
				Integer.min(recurseShortestPath(rExit, cExit, r, c-1, steps + 1, current, solutionMaze), recurseShortestPath(rExit, cExit, r, c+1, steps + 1, current, solutionMaze))
				);
		
	}

	// Clears all sequences except the most recently added sequence, which is the fully drawn maze
	public void skipSequences()
	{
		double[][] lastSequence = this.matrixSequences.getLast();
		this.matrixSequences.clear();
		this.matrixSequences.add(lastSequence);
	}
									            
}
