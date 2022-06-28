import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Astar {

	char [][] firstState , goalState;
	Board myBoard;
	boolean isPrintOpen;
	Set<Board> closedList = new HashSet<Board>();
	Set<Board> openList = new HashSet<Board>();

	String path; // path to goal state from the start state
	int numOfNodes; // Number of Nodes
	int cost; // cost to get the final state
	float time; // time to run BFS algorithm in seconds



	// constructor to run ball game over A* algorithm
	public Astar (char [][] firstState ,char [][] goalState, Boolean isPrintOpen) {

		this.numOfNodes = 1;
		this.cost = 0;
		this.isPrintOpen = isPrintOpen;

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


		this.runAstar();

	}


	// A* runner , its run like we learned in the class
	private void runAstar() {

		long startTime = System.currentTimeMillis();

		openList.add(myBoard);

		while(!openList.isEmpty()) {

			if(this.isPrintOpen)
				this.printNodeList();

			Board node = getNextNode();
			openList.remove(node);
			closedList.add(node);

			if(checkDone(node.getMyState())) {
				this.path = node.getPath();
				this.cost = node.getCost();
				break;
			}


			ArrayList<Board> childrens = createChildrens(node);
			for(Board child : childrens) {
				Board containsChildInOpenList = checkContains(child, openList);
				Board containsChildInClosedList = checkContains(child, openList);

				if((containsChildInClosedList == null) && (containsChildInOpenList == null))
					openList.add(child);
				else if( (containsChildInOpenList != null) && 
						((child.getCost() + child.getHuristicCost() ) < (containsChildInOpenList.getCost() + containsChildInOpenList.getHuristicCost()))) {
					openList.remove(containsChildInOpenList);
					openList.add(child);
				}	
			}
		}


		long endTime = System.currentTimeMillis();
		this.time = (endTime - startTime)/1000F;

	}

	// this function return the next board that need to be checked, which has the minimal cost to get.
	public Board getNextNode() {
		int minCost = Integer.MAX_VALUE;
		Board minCostNode = null ;
		//running over the open list and find the board which has the minimal cost
		for(Board node : openList) {
			int helper = node.getCost() + node.getHuristicCost();
			if(helper < minCost) {
				minCost = helper;
				minCostNode = node;
			}
		}

		return minCostNode;
	}

	// this function check if a given board can be found in giving hashSet
	public Board checkContains(Board board, Set<Board> hashSet) {
		// running over the hashSet and checking the ID of board equal to each node, if it found we return this node
		for(Board node : hashSet) {
			if(node.toString().equals(board.toString()))
				return node;
		}
		//if there is no node in the hashSet equal to board ID then this board can't found in this hashSet, so it return NULL
		return null;
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
						if(!((checkContains(newChild, openList) != null) || (checkContains(newChild, closedList) != null)))
							childrens.add(newChild);
					}
				//check down:
				if(father.LastMove != "up")
					if( i < helper.length-1 && helper[i][j] != '_' && helper[i+1][j] == '_' ) {
						Board newChild = buildNew(father, i, j, "down");
						if(!((checkContains(newChild, openList) != null) || (checkContains(newChild, closedList) != null)))
							childrens.add(newChild);
					}
				// check right:
				if(father.LastMove != "left")
					if( j < helper.length-1 && helper[i][j] != '_' && helper[i][j+1] == '_' ) {
						Board newChild = buildNew(father, i, j, "right");
						if(!((checkContains(newChild, openList) != null) || (checkContains(newChild, closedList) != null)))
							childrens.add(newChild);
					}
				//check left:
				if(father.LastMove != "right")
					if( j > 0 && helper[i][j] != '_' && helper[i][j-1] == '_' ) {
						Board newChild = buildNew(father, i, j, "left");
						if(!((checkContains(newChild, openList) != null) || (checkContains(newChild, closedList) != null)))
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


		Board ans = new Board(newState, newPath, newCost, direct, this.goalState);
		this.numOfNodes++;
		return ans;
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
		for (Board node : openList) {
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
