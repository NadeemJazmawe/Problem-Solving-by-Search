import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class DFID {
	char [][] firstState , goalState;
	Board myBoard;
	boolean isPrintOpen;

	String path; // path to goal state from the start state
	int numOfNodes; // Number of Nodes
	int cost; // cost to get the final state
	float time; // time to run BFS algorithm in seconds


	// constructor to run ball game over DFID algorithm
	public DFID (char [][] firstState ,char [][] goalState, Boolean isPrintOpen) {

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

		long startTime = System.currentTimeMillis();
		Board ans = this.runDFID();
		if(ans != null) {
			this.path = ans.getPath();
			this.cost = ans.getCost();
		}
			long endTime = System.currentTimeMillis();
			this.time = (endTime - startTime)/1000F;

	}


	// DFID runner , its run like we learned in the class(Recursive)
	private Board runDFID() {

		for (int i = 1; i < Integer.MAX_VALUE; i++) {
			Queue<Board> queue = new LinkedList<Board>();// queue to hold the Node we want to check
			Board path = Limited_DFS(myBoard, i, queue);
			if(path != null)
				return path;
		}
		return null;
	}

	// DFID runner helper , which run like DFS with = limited
	private Board Limited_DFS(Board state, int limit , Queue<Board> queue) {
		if(checkDone(state.getMyState()))
			return state;
		else if(limit == 0)
			return null;
		else {
			queue.add(state);
			Board result = null;
			ArrayList<Board> childrens = createChildrens(state);
			for (Board child : childrens) {
				if(queue.contains(child))
					continue;
				result = Limited_DFS(child, limit-1, queue);
				if(result != null)
					return result;
			}
			if(isPrintOpen)
				printNodeList(queue);
			queue.remove(state);
			return result;
		}

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
						childrens.add(newChild);
					}
				//check down:
				if(father.LastMove != "up")
					if( i < helper.length-1 && helper[i][j] != '_' && helper[i+1][j] == '_' ) {
						Board newChild = buildNew(father, i, j, "down");
						childrens.add(newChild);
					}
				// check right:
				if(father.LastMove != "left")
					if( j < helper.length-1 && helper[i][j] != '_' && helper[i][j+1] == '_' ) {
						Board newChild = buildNew(father, i, j, "right");
						childrens.add(newChild);
					}
				//check left:
				if(father.LastMove != "right")
					if( j > 0 && helper[i][j] != '_' && helper[i][j-1] == '_' ) {
						Board newChild = buildNew(father, i, j, "left");
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
		this.numOfNodes++;
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


	// function to print the Node list Nodes
	public void printNodeList(Queue<Board> queue) {
		//running over the queue and printing every Node ID.
		for(Board node : queue) {
			System.out.print(node.toString() + ", ");
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
