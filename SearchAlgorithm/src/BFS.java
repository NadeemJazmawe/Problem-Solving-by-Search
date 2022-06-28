
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class BFS {

	char [][] firstState , goalState;
	Board myBoard;
	boolean isPrintOpen;
	Set<String> closedList = new HashSet<String>();
	Set<String> openList = new HashSet<String>();

	String path; // path to goal state from the start state
	int numOfNodes; // Number of Nodes
	int cost; // cost to get the final state
	float time; // time to run BFS algorithm in seconds
	Queue<Board> queue = new LinkedList<Board>(); // queue to hold the Node we want to check


	// constructor to run ball game over BFS algorithm
	public BFS (char [][] firstState ,char [][] goalState, Boolean isPrintOpen) {

		this.numOfNodes = 1;
		this.cost = 0;
		this.isPrintOpen = isPrintOpen;
		this.path = "no path";

		this.firstState = new char [firstState.length][firstState.length];
		this.goalState = new char [firstState.length][firstState.length];

		for (int i = 0; i < firstState.length; i++) {
			for (int j = 0; j < firstState.length; j++) {
				this.firstState[i][j] = firstState[i][j];
			}
		}

		for (int i = 0; i < goalState.length; i++) {
			for (int j = 0; j < goalState.length; j++) {
				this.goalState[i][j] = goalState[i][j];
			}
		}

		this.myBoard = new Board(this.firstState);


		this.runBFS();

	}


	// BFS runner , its run like we learned in the class
	private void runBFS() {

		long startTime = System.currentTimeMillis();


		if(!checkDone(this.myBoard.getMyState())) {

			this.queue.add(this.myBoard);
			this.openList.add(this.myBoard.toString());
			Boolean run = true;

			while((!this.queue.isEmpty()) && run) {

				Board node = this.queue.poll();
				ArrayList<Board> childrens = createChildrens(node);
				if(this.isPrintOpen)
					this.printOpenList();

				for (Board child : childrens) {
					this.numOfNodes++;
					if( checkDone(child.getMyState())) {
						this.path = child.getPath();
						this.cost = child.getCost();
						run = false;
						break;
					}
					else {
						this.openList.add(child.toString());
						this.queue.add(child);
					}
				}

				this.closedList.add(node.toString());
				this.openList.remove(node.toString());
			}
		}
		else {
			this.path = "";
		}

		long endTime = System.currentTimeMillis();
		this.time = (endTime - startTime)/1000F;

	}

	
	// this function build all the states that can be got from father board by moving one ball
	private ArrayList<Board> createChildrens(Board father) {

		char [][] helper = father.getMyState();

		ArrayList<Board> childrens = new ArrayList<Board>();
		for(int i = 0; i < helper.length; i++) {
			for (int j = 0; j < helper.length; j++) {

				
				// check up :
				if(father.LastMove != "down")
					if(i > 0 && helper[i][j] != '_' && helper[i-1][j] == '_') {
						Board newChild = buildNew(father, i, j, "up");
						if(!(openList.contains(newChild.toString()) || closedList.contains(newChild.toString())))
							childrens.add(newChild);
					}
				
				
				//check down:
				if(father.LastMove != "up")
					if( i < helper.length-1 && helper[i][j] != '_' && helper[i+1][j] == '_' ) {
						Board newChild = buildNew(father, i, j, "down");
						if(!(openList.contains(newChild.toString()) || closedList.contains(newChild.toString())))
							childrens.add(newChild);
					}
				
				
				
				//check left:
				if(father.LastMove != "right")
					if( j > 0 && helper[i][j] != '_' && helper[i][j-1] == '_' ) {
						Board newChild = buildNew(father, i, j, "left");
						if(!(openList.contains(newChild.toString()) || closedList.contains(newChild.toString())))
							childrens.add(newChild);
					}
				
				// check right:
				if(father.LastMove != "left")
					if( j < helper.length-1 && helper[i][j] != '_' && helper[i][j+1] == '_' ) {
						Board newChild = buildNew(father, i, j, "right");
						if(!(openList.contains(newChild.toString()) || closedList.contains(newChild.toString())))
							childrens.add(newChild);
					}
				
				
				
				
			}
		}

		return childrens;
	}

	// this function build new board state from over board state
	private Board buildNew(Board state, int i, int j, String direct) {
		char[][] newState = new char [state.getMyState().length][state.getMyState().length];
		for (int k = 0; k < newState.length; k++) {
			for (int k2 = 0; k2 < newState.length; k2++) {
				newState[k][k2] = state.getMyState()[k][k2];
			}
		}

		String newPath = state.getPath();

		switch(direct) {
		case "up":
			newState[i-1][j] = newState[i][j] ;
			newState[i][j]  = '_' ;
			newPath += "--(" + (i+1) + "," + (j+1) + ")" + ":" + newState[i-1][j] + ":(" + (i) + "," + (j+1) + ")";
			break;

		case "down":
			newState[i+1][j] = newState[i][j] ;
			newState[i][j]  = '_' ;
			newPath += "--(" + (i+1) + "," + (j+1) + ")" + ":" + newState[i+1][j] + ":(" + (i+2) + "," + (j+1) + ")";
			break;

		case "right":
			newState[i][j+1] = newState[i][j] ;
			newState[i][j]  = '_' ;
			newPath += "--(" + (i+1) + "," + (j+1) + "):" + newState[i][j+1] + ":(" + (i+1) + "," + (j+2) + ")";
			break;

		case "left":
			newState[i][j-1] = newState[i][j] ;
			newState[i][j]  = '_' ;
			newPath += "--(" + (i+1) + "," + (j+1) + ")" + ":" + newState[i][j-1] + ":(" + (i+1) + "," + (j) + ")";
			break;

		default:
			System.out.println("no direct");
		}

		// customize the path string
		if(newPath.charAt(0) == '-')
			newPath = newPath.substring(2);

		// get the new cost 
		int newCost = state.getCost();
		switch(state.getMyState()[i][j]) {
		case 'R':
			newCost += 1;
			break;

		case 'Y':
			newCost += 1;
			break;

		case 'B':
			newCost += 2;
			break;

		case 'G':
			newCost += 10;
			break;

		}


		Board ans = new Board(newState, newPath, newCost, direct);
		return ans;
	}

	
	// checking if got the final state
	private Boolean checkDone(char [][] state ) {
		
		// running over goal state matrix and state matrix should have the same chars in each place
		for (int i = 0; i < goalState.length; i++) {
			for (int j = 0; j < goalState.length; j++) {
				// if we found different char at state and goalStae matrix we reurnt false
				if (this.goalState[i][j] != state[i][j])
					return false;
			}
		}
		
		// if there is no different chars it mean we got the goal
		return true;
	}

	
	// function to print the open list Nodes
	public void printOpenList() {
		//running over the list and printing every Node ID that openList hold
		for(String s : this.openList) {
			System.out.print(s + ", ");
		}
		System.out.println();
	}


	// getter and setters :-
	
	public String getPath() {
		return path;
	}

	public int getNumOfNodes() {
		return numOfNodes;
	}

	public int getCost() {
		return cost;
	}

	public float getTime() {
		return time;
	}


}
