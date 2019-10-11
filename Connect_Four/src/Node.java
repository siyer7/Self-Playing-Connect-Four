import java.util.ArrayList;
import java.util.List;

public class Node
{// creates a state, which is stored as an instance of this class
	private double Utility;
	private int[][] State;
	private Expand expand = new Expand();
	private int BestMove;
	private int maxdepth;
	
	public Node(int[][] State)
	{
		this.State= State;
	}
	
	public int[][] GetState()
	{
		return State;
	}
	
	public double GetUtility()
	{
		return Utility;
	}
	
	public void SetUtility(double Utility)
	{
		this.Utility = Utility;
	}
	public int GetBestMove()
	{
		return BestMove;
	}
	
	public void SetBestMove(int move)
	{
		this.BestMove = move;
	}
	public List<Node> Expand(boolean Player)
	{//expands frontier
		return expand.MakeChildren(State, Player);
	}
	public void SetDepth(int depth)
	{
		this.maxdepth= depth;
	}
	public int GetDepth()
	{
		return maxdepth;
	}
}

class Expand
{
	public ArrayList<Node> MakeChildren(int[][] State, boolean Player) 
	{//generates successors by putting an element in each empty row
		ArrayList<Node> frontier= new ArrayList<Node>();
		for(int j=0; j < State[0].length; j++) 
		{
			if(Problem.Action(State,j))
			{//tests for applicability, i.e., whether the particular column has an empty element
				Node node = new Node(AddChild(State,j,Player));
				node.SetBestMove(j);
				frontier.add(node);//adds a state to a frontier
			}
		}
		return frontier;
	}
	public int[][] AddChild(int[][] State, int j, boolean Player) 
	{
		int[][] ChildState = new int[State.length][State[0].length];
		for(int i=0; i<State.length; i++)
			for(int k=0; k<State[0].length; k++)
				ChildState[i][k]= State[i][k];
		
		for(int i = (State.length-1); i >= 0; i--) 
		{
			if(ChildState[i][j]==0) 
			{
				if(Player == false)
					ChildState[i][j]=1;
				else
					ChildState[i][j]=2;
				break;
			}
		}
		return ChildState;
	}
}