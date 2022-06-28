//package default;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class Ex1 {

	String algorithmName;
	Boolean isPrintOpen;
	String bordSize;
	char [][] firstState , goalState;


	public Ex1() throws IOException {


		File file = new File("input.txt");

		BufferedReader inFile = new BufferedReader(new FileReader(file));

		algorithmName = inFile.readLine();

		if(inFile.readLine().equals("no open")) {
			isPrintOpen = false;
		}else {
			isPrintOpen = true;
		}

		bordSize = inFile.readLine();

		if(bordSize.equals("small")) {
			firstState = new char [3][3];
			goalState = new char [3][3];

			for(int i = 0; i < 3; i++) {
				String line = inFile.readLine();
				for (int j = 0; j < 3; j++) {
					char cellColor = line.charAt(j*2);
					firstState[i][j] = cellColor;
				}
			}

			inFile.readLine();

			for(int i = 0; i < 3; i++) {
				String line = inFile.readLine();
				line.replace(",", "");
				for (int j = 0; j < 3; j++) {
					char cellColor = line.charAt(j*2);
					goalState[i][j] = cellColor;
				}
			}
		}else {
			firstState = new char [5][5];
			goalState = new char [5][5];

			for(int i = 0; i < 5; i++) {
				String line = inFile.readLine();
				line.replace(",", "");
				for (int j = 0; j < 5; j++) {
					char cellColor = line.charAt(j*2);
					firstState[i][j] = cellColor;
				}
			}
			
			inFile.readLine();
			
			for(int i = 0; i < 5; i++) {
				String line = inFile.readLine();
				line.replace(",", "");
				for (int j = 0; j < 5; j++) {
					char cellColor = line.charAt(j*2);
					goalState[i][j] = cellColor;
				}
			}
		}
		
		if(isPrintOpen)
			printer();	
		
		PrintStream out = new PrintStream(new FileOutputStream("output.txt"));//out stream for results


		switch(algorithmName) {

		//---------------------------------------------BFS Algorithm-------------------------------------//
		case "BFS" :  //rum BFS Algorithm	
			BFS bfs = new BFS(firstState, goalState, isPrintOpen);
			System.out.println("path: " + bfs.getPath());
			System.out.println("num: " + bfs.getNumOfNodes());
			System.out.println("cost: " + bfs.getCost());
			System.out.println("time: " + bfs.getTime());
			
			out.append(bfs.getPath() + "\n");
			out.append("Num: " + bfs.getNumOfNodes() + "\n");
			if(bfs.getPath() == "no path")
				out.append("Cost: inf\n");
			else
				out.append("Cost: " + bfs.getCost() + "\n");
			out.append(bfs.getTime() + " seconds");

			break;
			
		//---------------------------------------------DFID Algorithm-------------------------------------//	
		case "DFID" :
			DFID dfid= new DFID(firstState, goalState, isPrintOpen);
			System.out.println("path: " + dfid.getPath());
			System.out.println("num: " + dfid.getNumOfNodes());
			System.out.println("cost: " + dfid.getCost());
			System.out.println("time: " + dfid.getTime());
			
			out.append(dfid.getPath() + "\n");
			out.append("Num: " + dfid.getNumOfNodes() + "\n");
			if(dfid.getPath() == "no path")
				out.append("Cost: inf\n");
			else
				out.append("Cost: " + dfid.getCost() + "\n");			out.append(dfid.getTime() + " seconds");
			break;
			
			//---------------------------------------------A* Algorithm-------------------------------------//	
		case "A*" :
			Astar Astar= new Astar(firstState, goalState, isPrintOpen);
			System.out.println("path: " + Astar.getPath());
			System.out.println("num: " + Astar.getNumOfNodes());
			System.out.println("cost: " + Astar.getCost());
			System.out.println("time: " + Astar.getTime());
			
			out.append(Astar.getPath() + "\n");
			out.append("Num: " + Astar.getNumOfNodes() + "\n");
			if(Astar.getPath() == "no path")
				out.append("Cost: inf\n");
			else
				out.append("Cost: " + Astar.getCost() + "\n");			out.append(Astar.getTime() + " seconds");
			break;
			
		//---------------------------------------------IDA* Algorithm-------------------------------------//	 	
		case "IDA*" :
			IDAstar idastar= new IDAstar(firstState, goalState, isPrintOpen);
			System.out.println("path: " + idastar.getPath());
			System.out.println("num: " + idastar.getNumOfNodes());
			System.out.println("cost: " + idastar.getCost());
			System.out.println("time: " + idastar.getTime());
			
			out.append(idastar.getPath() + "\n");
			out.append("Num: " + idastar.getNumOfNodes() + "\n");
			if(idastar.getPath() == "no path")
				out.append("Cost: inf\n");
			else
				out.append("Cost: " + idastar.getCost() + "\n");			out.append(idastar.getTime() + " seconds");
			break;
			
		//---------------------------------------------DFBnB Algorithm-------------------------------------//	
		case "DFBnB" :
			DFBnB dfbnb= new DFBnB(firstState, goalState, isPrintOpen);
			System.out.println("path: " + dfbnb.getPath());
			System.out.println("num: " + dfbnb.getNumOfNodes());
			System.out.println("cost: " + dfbnb.getCost());
			System.out.println("time: " + dfbnb.getTime());
			
			out.append(dfbnb.getPath() + "\n");
			out.append("Num: " + dfbnb.getNumOfNodes() + "\n");
			if(dfbnb.getPath() == "no path")
				out.append("Cost: inf\n");
			else
				out.append("Cost: " + dfbnb.getCost() + "\n");			out.append(dfbnb.getTime() + " seconds");
			break;
			
		default :
			System.out.println("Error input algorithm.");
		}
		
		inFile.close();

		


	}

	public void printer() {
		System.out.println("First State:");
		for(int i = 0 ; i < firstState.length ; i++) {
			for(int j = 0 ; j < firstState.length ; j++) {
				System.out.print(firstState[i][j] + ",");
			}
			System.out.println();
		}

		System.out.println("Goal State:");
		for(int i = 0 ; i < goalState.length ; i++) {
			for(int j = 0 ; j < goalState.length ; j++) {
				System.out.print(goalState[i][j] + ",");
			}
			System.out.println();
		}
	}



	public static void main(String[] args) throws IOException {

		Ex1 game = new Ex1();
		//		File file = new File("E:\\Codes\\eclipse-workship\\betraonBaeot\\src\\betraonBaeot\\input.txt");
		//
		//		System.out.println(file);

	}

}
