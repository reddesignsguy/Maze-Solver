
MaveSolver is a maze generation and solver app! 

Features:
- Able to generate a square maze of sizes 2x2 to 50x50
- Able to highlight the shortest path from the start to any point you choose
- Able to see the maze being created


Tools used:
- Java
- JavaFX module: Responsible for  user interface and graphics

Implementation:
* To implement the bulk of the maze generation and maze solving, I implemented Depth-First-Search (DFS) algorithm 
that was modified to work with a 2D array rather than a traditional graph.

* Implementing the animations was the harder bit. I compiled each change in the maze as it was being generated.
The visual maze generation that you see being played out is actually a compilation of the changes already done on the maze; In other words,
the complete maze is already generated before the animation even starts.


Purpose:
* I worked on this project because I thought the concept of maze generation was awesome and wanted to understand
how  it exactly worked. I actually saw a YouTube video who had

* This project was before I took my algorithms course, therefore, I can definitely say that I could make many changes
to my approach for this project including.. 
  * The use of a queue for the sake of conserving the stack
  * A better method of showcasing animation
  * Using a different platform altogether; A website would be funner and more easily accessible to users.
