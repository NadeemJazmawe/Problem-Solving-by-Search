
public class Board {

	char [][] myState; // current state	
	String path; // path for this state from the start state
	int cost; // the cost to get this state
	String boardId; // hold the ID of the board which is the string of the board
	String LastMove ="";  // hold the last move to get this board;

	int heuristicCost = 0; // Heuristic cost to get the final state, for A* IDA* DFBnB
	
	String out = ""; // hold the out variable, for IDA* DFBnB


	// default constructor
	public Board(char [][] state){
		this.path = "";
		this.cost = 0;
		this.myState = new char [state.length][state.length];

		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++) {
				this.myState[i][j] = state[i][j];
			}
		}
		buildBoardId();

	}


	// constructor with path, cost and last move
	public Board(char [][] state, String path ,int cost, String LastMove){
		this.path = path;
		this.cost = cost;
		this.LastMove = LastMove;
		this.myState = new char [state.length][state.length];

		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++) {
				this.myState[i][j] = state[i][j];
			}
		}
		buildBoardId();
	}

	
	// constructor that build Heuristic, for A* IDA* DFBnB
	public Board(char [][] state, char [][] finalState){ 
		this.path = "";
		this.cost = 0;
		this.myState = new char [state.length][state.length];


		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++) {
				this.myState[i][j] = state[i][j];
			}
		}
		buildBoardId();
		getHuristicCost(finalState);

	}

	
	//constructor that build Heuristic with path, cost and move, for A* IDA* DFBnB
	public Board(char [][] state, String path ,int cost, String LastMove, char [][] finalState){
		this.path = path;
		this.cost = cost;
		this.LastMove = LastMove;
		this.myState = new char [state.length][state.length];

		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++) {
				this.myState[i][j] = state[i][j];
			}
		}
		buildBoardId();
		getHuristicCost(finalState);

	}

	
	// this function build the Board ID which is the string of the board
	private void buildBoardId() {
		String ans = "[";
		for (int i = 0; i < this.myState.length; i++) {
			ans += '{';
			for (int j = 0; j < this.myState.length; j++) {

				ans += this.myState[i][j] + ",";
			}
			ans += "},";
		}
		ans = ans.substring(0, ans.length()-1);
		ans += "]";

		boardId = ans;
	}

	
	// this function build the Heuristic cost to get the final state, for A* IDA* DFBnB
	private void getHuristicCost(char [][] finalState) {

		// counting number of balls, every color has variable
		int numR = 0;
		int numB = 0;
		int numG = 0;
		int numY = 0;

		for(int i=0 ; i<finalState.length; i++) {
			for(int j=0 ; j<finalState.length; j++) {
				if(finalState[i][j] == 'R')
					numR++;
				else if(finalState[i][j] == 'B')
					numB++;
				else if(finalState[i][j] == 'G')
					numG++;
				else if(finalState[i][j] == 'Y')
					numY++;
			}
		}
		
		
		//making holder for the location of the balls, every color has variable
		int [][] holdR = new int [numR][2];
		int [][] holdB = new int [numB][2];
		int [][] holdG = new int [numG][2];
		int [][] holdY = new int [numY][2];

		//making runner over the balls location holder, every color has variable
		int indexR = 0;
		int indexB = 0;
		int indexG = 0;
		int indexY = 0;

		//insert places of the balls, running over the state and every time we found ball we insert it's place in location holder array
		for(int i=0 ; i<finalState.length; i++) {
			for(int j=0 ; j<finalState.length; j++) {
				if(finalState[i][j] == 'R') {
					holdR[indexR][0] = i;
					holdR[indexR][1] = j;
					indexR++;
				}
				else if(finalState[i][j] == 'B'){
					holdB[indexB][0] = i;
					holdB[indexB][1] = j;

					indexB++;
				}
				else if(finalState[i][j] == 'G'){
					holdG[indexG][0] = i;
					holdG[indexG][1] = j;
					indexG++;
				}
				else if(finalState[i][j] == 'Y'){
					holdY[indexY][0] = i;
					holdY[indexY][1] = j;
					indexY++;
				}
			}
		}

		// reset the runners
		indexR = 0;
		indexB = 0;
		indexG = 0;
		indexY = 0;

		
		// running over state matrix if we found a ball we calculate how many steps it need to get for her place * cost o move
		for(int i=0 ; i<myState.length; i++) {
			for(int j=0 ; j<myState.length; j++) {
				if(myState[i][j] == 'R') {
					heuristicCost += Math.abs(i-holdR[indexR][0]);
					heuristicCost += Math.abs(j-holdR[indexR][1]);
					indexR++;
				}
				else if(myState[i][j] == 'B') {
					heuristicCost += 2*Math.abs(i-holdB[indexB][0]);
					heuristicCost += 2*Math.abs(j-holdB[indexB][1]);
					indexB++;
				}
				else if(myState[i][j] == 'G') {
					heuristicCost += 10*Math.abs(i-holdG[indexG][0]);
					heuristicCost += 10*Math.abs(j-holdG[indexG][1]);
					indexG++;
				}
				else if(myState[i][j] == 'Y') {
					heuristicCost += Math.abs(i-holdY[indexY][0]);
					heuristicCost += Math.abs(j-holdY[indexY][1]);
					indexY++;
				}
			}
		}

	}

	
	// getter and setters :-
	public int getHuristicCost() {
		return heuristicCost;
	}

	public char[][] getMyState() {
		return myState;
	}

	public String getPath() {
		return path;
	}

	public int getCost() {
		return cost;
	}

	public String toString() {
		return boardId;
	}

	public void setOut(String out) {
		this.out = out;
	}

	public String getOut() {
		return out;
	}


}
