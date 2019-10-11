public class Problem {
	/**after state has been created, it deals with actions (applicability) and results of actions
	 	purely representational class. actual logic is performed by class check*/
	
	private static int K_game;
	private static int Utility;
	private static int[][] InitialState;
	private static Check check = new Check();
	
	public Problem(int Case)
	{//creates initial state based on user choice
		switch (Case) 
		{
		case 1:
			K_game = 3;
			InitialState = new int[3][3];
			break;
			
		case 2:
			K_game = 3;
			InitialState = new int[3][5];
			break;
			
		case 3:
			K_game = 4;
			InitialState = new int[6][7];
			break;
		}
	}
	public static int[][] InitialState()
	{
		return InitialState;
	}
	public static int Utility()
	{
		return Utility;
	}
	public static double CutOffUtility(int[][] State)
	{//returns utility based on heuristic
		return check.CutOff(State);
	}
	
	public static boolean Action(int[][] State,int j) 
	{//returns whether an action is applicable or not
		if(check.TestAction(State, j))
			return true;
		return false;
	}

	public static Node Result(int[][] GridState, int Column, int Color)
	{//updates state to give result
		return check.PutaMark(GridState, Column, Color);
	}
	
	public static boolean TerminalState(int[][] State) 
	{//returns whether a state is a terminal state or not
		if(check.IsWinner(State,K_game))
		{//checks if there is a winner
			if (Check.WinnerPlayer == 1)
				Utility = -1;
			else
				Utility = 1;
			
			return true;
		}
		else if(check.IsFull(State))
		{
			Utility = 0;
			return true;
		}
		
		return false;
	}
		
	public static boolean Draw(int[][] State)
	{//if state is full, but there is no winner, i.e. there is a draw
		if(check.IsWinner(State, Problem.K_game))
			return false;
		else if(check.IsFull(State))
			return true;
		return false;
	}
}

class Check
{/**this class does the logical reasoning behind applicability of an action; determining whether is terminal or not
	returns terminal utility or cutoff utility (based on heuristic)*/
	static int WinnerPlayer;
	
	public double CutOff(int[][] State)
	{/**
		Here is our heuristic.
		We identify all empty spaces on the board, and then count how many neighboring Xs or Os can take advantage of it.
		While considering a blank, if there are 2 neighboring Xs, then the state gets 2 utility points. If 3
		neighboring 0s, then 3 utility points
		get deducted.
		The total utility is then halved, to be more conservative with the heuristic.
	 */
		double Max = 0, Min = 0; 
		//max stores utility for Xs, min holds utility for Os
		for(int i = 0; i < State.length; i++)
		{
			for(int j = 0; j < State[0].length; j++)
			{
				if(i != 0)
				{//checks cell above
					if(State[i-1][j] == 1)
						Min -=1; 
					else if(State[i-1][j] == 2)
						Max +=1;
				}
				else if(i != (State.length-1))
				{//checks cell below
					if(State[i+1][j] == 1)
						Min -=1;
					else if(State[i+1][j] == 2)
						Max +=1;
				}
				else if(j != 0)
				{//checks cell to the left
					if(State[i][j-1] == 1)
						Min -=1;
					else if(State[i][j-1] == 2)
						Max +=1;
				}
				else if(j != (State[0].length-1))
				{//checks cell to the right
					if(State[i][j+1] == 1)
						Min -=1;
					else if(State[i][j+1] == 2)
						Max +=1;
				}
				else if(i != 0 && j != 0)
				{//checks cell left upward
					if(State[i-1][j-1] == 1)
						Min -=1;
					else if(State[i-1][j-1] == 2)
						Max +=1;
				}
				else if(i != (State.length-1) && j != (State[0].length-1))
				{//checks cell right downward
					if(State[i+1][j+1] == 1)
						Min -=1;
					else if(State[i+1][j+1] == 2)
						Max +=1;
				}
				else if(i != 0 && j != (State[0].length-1))
				{//checks cell right upward
					if(State[i-1][j+1] == 1)
						Min -=1;
					else if(State[i-1][j+1] == 2)
						Max +=1;
				}
				else if(i != State.length-1 && j != 0)
				{//checks cell left downward
					if(State[i+1][j-1] == 1)
						Min -=1;
					else if(State[i+1][j-1] == 2)
						Max +=1;
				}
			}
		}
		double average = ((Max+Min)/2);
		return average;
	}
	
	public Node PutaMark(int[][] State, int Column, int Color)
	{//logically applies the action to result in a new state
		for(int i = (State.length-1); i >= 0; i--) 
		{
			if(State[i][Column]==0) 
			{
				State[i][Column] = Color;
				break;
			}	
		}
		return new Node(State);
	}
	public boolean TestAction(int[][] State, int j) 
	{//logically tests if given action is applicable, by checking if chosen column is not empty
		if(State[0][j]==0)
			return true;
		return false;
	}
	
	public Boolean IsFull(int[][] State) 
	{		
		for(int j=0; j<State[0].length; j++) 
		{
			if(State[0][j]==0)
				return false;
		}
		return true;
	}
	public boolean IsWinner(int[][] State, int k_game)
	{//checks for k elements in a row
		int i,j;
		
		for(i=0; i<State.length; i++) 
		{//horizontals
			for(j=0; j<=State[0].length-k_game; j++) 
			{
				if(State[i][j] == State[i][j+1] &&
				   State[i][j] == State[i][j+2] &&
				   State[i][j] == State[i][j+k_game-1] &&
				   State[i][j]!=0) 
				{
					if(State[i][j] == 1)
						WinnerPlayer = 1;
					else 
						WinnerPlayer = 2;
					return true;	
				}
			}
		}
		
		for(i=0; i<=State.length-k_game; i++) 
		{//veticals
			for(j=0; j<State[0].length; j++) 
			{
				if(State[i][j] == State[i+1][j] &&
				   State[i][j] == State[i+2][j] &&
				   State[i][j] == State[i+k_game-1][j] &&
				   State[i][j]!=0)
				{
					if(State[i][j] == 1)
						WinnerPlayer = 1;
					else 
						WinnerPlayer = 2;
					return true;
				}
			}
		}
		
		for(i=0; i<=State.length-k_game; i++) 
		{//right downward diagonals
			for(j=0; j<=State[0].length-k_game; j++) 
			{
				if(State[i][j] == State[i+1][j+1] &&
				   State[i][j] == State[i+2][j+2] &&
				   State[i][j] == State[i+k_game-1][j+k_game-1] &&
				   State[i][j]!=0)
				{
					if(State[i][j] == 1)
						WinnerPlayer = 1;
					else 
						WinnerPlayer = 2;
					return true;
				}
			}
		}
		
		for(i=State.length-1; i>=k_game-1; i--) 
		{//up rightward diagonals
			for(j=0; j<=State[0].length-k_game; j++) 
			{
				if(State[i][j] == State[i-1][j+1] &&
				   State[i][j] == State[i-2][j+2] &&
				   State[i][j] == State[i-(k_game-1)][j+k_game-1] &&
				   State[i][j]!=0)
				{
					if(State[i][j] == 1)
						WinnerPlayer = 1;
					else 
						WinnerPlayer = 2;
					return true;
				}
			}
		}
		
		return false;
	}
}