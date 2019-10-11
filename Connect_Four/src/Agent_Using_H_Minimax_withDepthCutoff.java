import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

public class Agent_Using_H_Minimax_withDepthCutoff
{	
	private Node RandomBestMove = new Node(Problem.InitialState());
	
	private ArrayList<Node> Frontiers = new ArrayList<>();
	
	private ArrayList<Node> BestMoves = new ArrayList<>();
	
	private long RedTotalTime;
	
	private int States;
	
	private double BestMoveUtility;
	
	public long GetRedTotalTime()
	{
		return RedTotalTime;
	}
	public Node PlayGame(Node node, boolean Player, int HDepth)
	{
		long start = System.currentTimeMillis();
		System.out.println("I'm thinking...");
		
		BestMoveUtility = Minimax(node, Player, HDepth);
		Depth = 0;
		SelectBestMove();
		Frontiers.clear();
		BestMoves.clear();
		
		NumberFormat formatter = new DecimalFormat("#0.00000");
		long end = System.currentTimeMillis();
		RedTotalTime += (end - start) / 1000d;
		
		System.out.println("	visited "+States+" states\r\n" + 
				"	best move: Red@" + RandomBestMove.GetBestMove()+", value: "+ BestMoveUtility +"\r\n" + 
				"Elapsed time: " + formatter.format((end - start) / 1000d) + " secs\r\n" + 
				"Red@" + RandomBestMove.GetBestMove()+"\n");
		States = 0;
		return RandomBestMove;
	}
	private int Depth;
	
	public double Minimax(Node node, boolean Player, int HDepth)
	{
		if(Problem.TerminalState(node.GetState()))
			return Problem.Utility();
		else if (Depth == HDepth)//base case also involves cutoff state as specified by the user's depth
			return Problem.CutOffUtility(node.GetState());
		
		if (Player)
		{
			node.SetUtility(Integer.MIN_VALUE);
			for(Node n : node.Expand(Player))
			{	
				States+=1; Depth+=1;
				n.SetUtility(Minimax(n, false, HDepth));
//				n.SetDepth(Math.max(n.GetDepth(), Depth));
				node.SetUtility(Math.max(n.GetUtility(), node.GetUtility()));
				Depth-=1;
			}
			return node.GetUtility();
		}
		else
		{
			node.SetUtility(Integer.MAX_VALUE);
			for(Node n : node.Expand(Player))
			{	
				States+=1;Depth+=1;
				n.SetUtility(Minimax(n, true, HDepth));
//				n.SetDepth(Math.max(n.GetDepth(), Depth));
				node.SetUtility(Math.min(n.GetUtility(), node.GetUtility()));
				if (Depth == 1)
					Frontiers.add(n);
				Depth-=1;
			}
			return node.GetUtility();
		}
		
	}
	public void SelectBestMove()
	{
		RandomBestMove.SetUtility(Integer.MAX_VALUE);
		for(Node n : Frontiers)
		{
			if(n.GetUtility() == BestMoveUtility)
				BestMoves.add(n);
		}
		RandomBestMove = BestMoves.get(randomNumberInRange(0, (BestMoves.size()-1)));
	}
	public int randomNumberInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
	
}