package application;
import java.util.HashSet;
import java.util.Objects;
import java.util.Stack;

public class Coordinate {
	private int r;
	private int c;
	private boolean visited;
	private Coordinate prev;
	private Coordinate next;
	
	public Coordinate(int r, int c, Coordinate prev)
	{
		try {
			if (r < 0 || c < 0)
				throw new IllegalCoordinateException("r is negative: " + (r < 0) + " | c is negative: " + (c < 0));
		} catch (IllegalCoordinateException e)
		{
			e.printStackTrace();
		}
		this.r = r;
		this.c = c;
		this.visited = false;
		this.prev = prev;
		
	}
	
	public Coordinate getPrev()
	{
		return this.prev;
	}
	
	public int getRow() {
		return r;
	}
	public int getCol() {
		return c;
	}
	
	public boolean isVisited() {
		return visited;
	}
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	public boolean equals(Object o)
	{
		Coordinate that = (Coordinate) o;
		if (this.hashCode() == that.hashCode())
			return true;
		return false;
	}
	
	public int hashCode()
	{
		// Use hash method from Objects class
		return Objects.hash(this.r, this.c);
	}
	
//	public static void main(String[] args)
//	{
//		Coordinate co = new Coordinate(2,0);
//		Coordinate co2 = new Coordinate(0,2);
//		System.out.println("Coordinates equal each other: " + co.equals(co2) + " | Co: " + co.hashCode() + ", Co2: " + co2.hashCode());
//		
//		HashSet<Coordinate> test = new HashSet<>();
//		test.add(new Coordinate(0,0));
//		test.add(new Coordinate(1,0));
//		
//		Stack<Coordinate> test2 = new Stack<>();
//		test2.addAll(test);
//		System.out.println(test2.size());
//	}
}
