import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class DFBnB {
	char [][] firstState , goalState;
	Board myBoard;
	boolean isPrintOpen;
	Stack<Board> stack = new Stack<Board>(); // Stack to hold the Node we want to check


	String path; // path to goal state from the start state
	int numOfNodes; // Number of Nodes
	int cost; // cost to get the final state
	float time; // time to run BFS algorithm in seconds

	// constructor to run ball game over DFBnB algorithm
	public DFBnB (char [][] firstState ,char [][] goalState, Boolean isPrintOpen) {

		this.numOfNodes = 1;
		this.cost = 0;
		this.isPrintOpen = isPrintOpen;
		this.path = "no path";
		this.time = 0;

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

		this.myBoard = new Board(this.firstState, this.goalState);

		if(checkDone(this.myBoard.getMyState())) 
			this.path = "";

		else
			this.runDfbnb();

	}

	// DFBnB runner , its run like we learned in the class
	private void runDfbnb() {
		Board result = null;
		int threshold = myBoard.getHuristicCost()*2 ;
		System.out.println(threshold);

		stack.add(myBoard);
		while(!stack.isEmpty() ) {
			if(isPrintOpen)
				printNodeList();

			Board node = stack.pop();
//			System.out.println((node.cost+node.heuristicCost) + " , " + node.cost + " + " + node.heuristicCost);

			if(node.getOut() == "out") {
				//				System.out.println("deleted");
				continue;
			}

			else {
				node.setOut("out");
				stack.add(node);

				Queue<Board> sortedChildrens = sortChildrenList(node);
				for(Board child : sortedChildrens) {
					int childCost = child.getCost() + child.getHuristicCost();
					Board containsChildInStack = checkContains(child, stack);
					if(childCost > threshold) {
						break;
					}
					if((containsChildInStack != null) && (containsChildInStack.getOut() == "out")) {
						continue;
					}
					if((containsChildInStack != null) && (containsChildInStack.getOut() != "out")) {
						if( (containsChildInStack.getCost() + containsChildInStack.getHuristicCost()) <= childCost)
							continue;
						else
							stack.remove(containsChildInStack);
					}
					if(checkDone(child.getMyState())){
						threshold = childCost;
						result = child;
						break;
					}
					stack.add(child);
				}
			}
		}

		if (result != null) {
			this.path = result.getPath();
			this.cost = result.getCost();
		}

	}

	// this function check if a given board can be found in giving Stack
	public Board checkContains(Board board, Stack<Board> stack) {
		// running over the Stack and checking the ID of board equal to each node, if it found we return this node
		for(Board node : stack) {
			if(node.toString().equals(board.toString()))
				return node;
		}
		//if there is no node in the Stack equal to board ID then this board can't found in this Stack, so it return NULL
		return null;
	}

	// this function build all the states that can be got from father board by moving one ball which be sorted according to their f values (increasing order).
	private Queue<Board> sortChildrenList(Board father){

		ArrayList<Board> childrens = createChildrens(father);

		Queue<Board> sortedQueue = new LinkedList<Board>();

		while(!childrens.isEmpty()) {
			Board node = getNextNode(childrens);
			sortedQueue.add(node);
			childrens.remove(node);
		}

		return sortedQueue;
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


		Board ans = new Board(newState, newPath, newCost, direct, this.firstState);
		this.numOfNodes++;
		return ans;
	}

	// this function return the next Node that need to be enter to the queue, which has the minimal cost to get.
	public Board getNextNode(ArrayList<Board> list) {
		int minCost = Integer.MAX_VALUE;
		Board minCostNode = null ;
		//running over the open list and find the board which has the minimal cost
		for(Board node : list) {
			int helperCost = node.getCost() + node.getHuristicCost();
			if(helperCost < minCost) {
				minCost = helperCost;
				minCostNode = node;
			}
		}

		return minCostNode;
	}


	// checking if got the final state
	private Boolean checkDone(char [][] state ) { 
		// running over goal state matrix and state matrix should have the same chars in each place
		for (int i = 0; i < goalState.length; i++) {
			for (int j = 0; j < goalState.length; j++) {
				// if we found different char at state and goalStae matrix we return false
				if (this.goalState[i][j] != state[i][j])
					return false;
			}
		}
		return true;
	}

	// function to print the open list Nodes
	private void printNodeList() {
		//running over the list and printing every Node ID that openList hold
		for (Board node : stack) {
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
