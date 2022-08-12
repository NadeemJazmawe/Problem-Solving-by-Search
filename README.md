# Problem Solving by Search

Path Finding project for "Problem Solving by Search" Course which implementing different search algorithms such as:
* BFS
* DFID
* A-Star(A*)
* IDA-Star(IDA*)
* DFBnB

There is no application to GUI , the mean idea is to solve the game using the algorithms, so i hasn't focused on the GUI.

## Overview
The objective is to solve The "Fix me" game(description of the game is below), by calculating and returning the path to solve the game.
Some algorithms return the minimum cost to solve, and some return the minimum steps to solve.


## "Fix me" Game

The game is about Board with Balls in different colors on it, There is two size of Borad:
1. Small Borad (3x3 cells), which have 6 Balls on it :
	* 2 Red Balls
	* 2 Blue balls
	* 2 Green balls
2. Big Board (5x5 cells), which have 16 Balls on it :
	* 4 Red Balls
	* 4 Blue balls
	* 4 Green balls
	* 4 Yellow balls 

The Balls are placed on the Board in some given starting order, and the goal is to find the cheapest number of actions from the situation to the final state, which is also given.

### The Actions
Each Ball can be moved to a cell next to it horizontally or vertically, but not diagonally. In addition, the Ball can be moved to a cell Only on the condition that it is empty (there is no ball in it at the moment). The cost to move the Balls is:

| Ball color  | Cost to move |
| ------------- | ------------- |
| Yellow  | 1  |
| Red | 1 |
| Blue | 2  |
| Green  | 10  |


For example, if the small board is in this state:


We can move one blue Ball to the right, then up, and one green Ball up to get to this position:


The cost of the described route will be 14=10+2+2.



### Realization
#### Input
The program will read all its input from a single file - txt.input. 

The first line in the file will determine which algorithm to use: BFS, DFID*, A*, IDA, or DFBnB. 


The second line will determine whether to print the list open to the screen at each stage of running the search algorithm (open with) or not (open no).

The third line will determine in which variation this is: a big Board (big) or a small board (small). 

After that, the initial arrangement of the Board by rows will appear, when there is commas between cells.
A cell containing a red Ball will be marked as R, a blue Ball will be marked as B, a green Ball will be marked as G and Yellow will be marked as Y. 
Empty cells will be marked as "_".

After that a line will appear that says "state Goal:" and after that the final arrangement of the board that needs to be reached will appear(in the same format as the initial arrangement). 

Assuming that the input file is correct.



#### Output
If open no is written in the input file, all output will be written to the txt.output file. In such a case, nothing should be printed on the screen. 

In the first line of the file, will write the series of actions that founded by the algorithm. 

In the second line, will write "Num:" and then the number of vertices that were created. Vertices that did not enter the open list also will be counted, and if a vertex has been created many times it will be counted each time it has created. 

In the third line, will write "Cost:" and then the cost of the founded solution.

In the fourth line, will write the time it took for the algorithm to find the solution (in seconds).

The actions will be marked by the position of the Balls that moved, its color, and to which cell it has moved. This is how the match is numbered, like that number the appropriate ones in the matrix. The upper left cell is 1,1 and the lower right cell is 3,3 (in the small game) or 5.5 (in the big game).

A colon must be written between each characteristic of an action, and the actions will be separated by a dual dash.

For example, The route described earlier will be writted as:
(2,2):B:(2,3)--(2,3):B:(1,3)--(3,2):G:(2,2)

If "open with" is written in the output file, the output will be written to the output file exactly as before, except that in addition there will printed to the screen the contents of the list-open in each iteration of the algorithm (before each output from the list-open.)
With a clearly visible how the board will be visible in each of the states found in list-open.


#### Highlights
* Open & Closed list has implement with hash-Table.

* BFS and A* has implemented with a closed list.

* IDA* and DFBnB has implemented with a stack and without list-closed with loop-avoidance, i.e. checking whether the vertex that developed is on the branch we are working on or already in the stack.

* DFID has been implemented recursively, without Closed-list also with loop-avoidance.

* If there is no path has been found, will be writed: 
	"path no" in the first line of the output file.
	In the second line will be write "Num:" and then the number of vertices that were created. 
	In the third line will be write "Cost: inf"
	In the fourth line will be write the time it took for the algorithm (in seconds).

* In DFID, the first iteration is when i=1 , because it is clear that the initial state is not the final state.

* Although our aim is to find the cheapest route, BFS and DFID will not necessarily find the cheapest route but the shortest route (with the fewest sliding operations). Still in the output file the cost must be returned of the route they found (and not the number of steps of the route).







