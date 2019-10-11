import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;

public class Game 
{
	//concerned with game play; alternating turns; printing states, stating instructions and declaring winner.
	public static void main(String[] args)
	{
		Scanner userInput = new Scanner(System.in);
		
		System.out.print("Connect-Four by Daniel He\n" +
				"Choose your game:\n" +
				"1. Tiny 3x3x3 Connect-Three\n" +
				"2. Wider 3x5x3 Connect-Three\n" +
				"3. Standard 6x7x4 Connect-Four\n" +
				"Your Choice? ");
		int InitialState = userInput.nextInt();
		while(InitialState < 1 || InitialState > 3) 
		{//ensures correct input
			System.out.print("Incorrect Input! Please try again... \nYour Choice? ");
			InitialState  = userInput.nextInt();
		}
		//creates problem with board specifications
		new Problem(InitialState);
		
		System.out.print("Choose your opponent:\n" + 
				"1. An agent that plays randomly\n" + 
				"2. An agent that uses MINIMAX\n" + 
				"3. An agent that uses MINIMAX with alpha-beta pruning\n" + 
				"4. An agent that uses H-MINIMAX with a fixed depth cutoff\n" +
				"Your Choice? ");
		int Opponent= userInput.nextInt();
		while(Opponent  < 1 || Opponent> 4)
		{//ensures correct input
			System.out.print("Incorrect Input! Please try again... \nYour Choice? ");
			Opponent= userInput.nextInt();
		}
		int HDepth= 0;
		if(Opponent==4)
		{
			System.out.println("Depth limit?");
			HDepth = userInput.nextInt();
		}
		
		System.out.print("You are Player (Yellow)\nYour Opponent is Player (Red)\n"
				+ "Do you want Player RED (1) or YELLOW (2) to go first? ");
		int Player = userInput.nextInt();
		while(Player < 1 || Player > 2) 
		{//ensures correct input
			System.out.print("Incorrect Input! Please try again... \n"
					+ "You are Player Yellow!!!\nYour Opponent is Player Red\n" 
					+"Do you want player RED (1) or YELLOW (2) to go first?");
			Player = userInput.nextInt();
		}
		//creates initial state: an empty board with given dimensions
		Node node = new Node(Problem.InitialState());
		Game.PrintTransitionModel(Problem.InitialState());
		
		Agent_Playing_Random RandomAgent= null;
		Agent_Using_Minimax MinimaxAgent= null;
		Agent_Using_Minimax_withAlpha_Beta_Pruning MinimaxAgentABPrune= null;
		Agent_Using_H_Minimax_withDepthCutoff HMinimax= null;
		
		switch (Opponent) 
		{
		case 1:
			RandomAgent= new Agent_Playing_Random();
			break;
		case 2:
			MinimaxAgent= new Agent_Using_Minimax();
			break;
		case 3:
			MinimaxAgentABPrune= new Agent_Using_Minimax_withAlpha_Beta_Pruning();
		case 4:
			HMinimax= new Agent_Using_H_Minimax_withDepthCutoff(); 
		}
		
		Agent_Using_Brain BrainAgent = new Agent_Using_Brain();
		while((Problem.TerminalState(node.GetState()) != true)) 
		{//while a turn can be played, i.e. no winner/draw
			if(Player%2 == 0)
			{//user's turn
				System.out.print("Next to move: YELLOW\n\nYour move [column 0-"+(Problem.InitialState()[0].length-1)+"]? ");
				node = BrainAgent.PlayGame(node, Player);
				Player=1;//alternates turns
			}
			else
			{//program's turn
				System.out.print("Next to move: RED\n\n");
				switch(Opponent)
				{
				case 1:
					node = RandomAgent.PlayGame(node);
					break;
				case 2:
					node = MinimaxAgent.PlayGame(node, false);
					break;
				case 3:
					node = MinimaxAgentABPrune.PlayGame(node, false);
					break;
				case 4:
					node= HMinimax.PlayGame(node, false, HDepth);
				}
				Player = 2;
			}
			PrintTransitionModel(node.GetState());

		}
		
		if(Problem.Draw(node.GetState()) != true) 
		{
			if(Player == 1)
				System.out.println("\nWinner: YELLOW");
			else
				System.out.println("\nWinner: RED");
		}
		else
			System.out.println("\nNo Winner: Draw");
		
		NumberFormat formatter = new DecimalFormat("#0.00000");
		switch(Opponent) {
			case 1:
				System.out.println("Total time:\r\n" + 
						"	RED: "+ formatter.format(RandomAgent.GetRedTotalTime()) +" secs\r\n" + 
						"	YELLOW: "+formatter.format(BrainAgent.GetYellowTotalTime())+" secs");
				break;
			case 2:
				System.out.println("Total time:\r\n" + 
						"	RED: "+ formatter.format(MinimaxAgent.GetRedTotalTime()) +" secs\r\n" + 
						"	YELLOW: "+formatter.format(BrainAgent.GetYellowTotalTime())+" secs");
				break;
			case 3:
				System.out.println("Total time:\r\n" + 
						"	RED: "+ formatter.format(MinimaxAgentABPrune.GetRedTotalTime()) +" secs\r\n" + 
						"	YELLOW: "+formatter.format(BrainAgent.GetYellowTotalTime())+" secs");
				break;
			case 4:
			System.out.println("Total time:\r\n" + 
					"	RED: "+ formatter.format(HMinimax.GetRedTotalTime()) +" secs\r\n" + 
					"	YELLOW: "+formatter.format(BrainAgent.GetYellowTotalTime())+" secs");
		}
		userInput.close();
	}
	public static void PrintTransitionModel(int[][] State) 
	{
		System.out.print("     ");
		for(int i = 0; i < State[0].length; i++)
			System.out.print("    "+i);
		
		System.out.println();
		for (int i = 0; i < State.length; i++) 
		{
			System.out.print("\n    "+i);
			for(int j = 0 ; j < State[i].length; j++) 
			{
				if(State[i][j]==0)
					System.out.print("     ");
				else if(State[i][j]==1)
					System.out.print("    X");
				else
					System.out.print("    O");
				
			}
			System.out.print("    "+i+"\n");
			
		}
		System.out.println();
		
		System.out.print("     ");
		for(int i = 0; i < State[0].length; i++)
			System.out.print("    "+i);
		
		System.out.println();
	}
}