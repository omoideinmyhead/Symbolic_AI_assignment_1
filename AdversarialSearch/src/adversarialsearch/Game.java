package adversarialsearch;

import java.io.IOException;
import java.util.Vector;

public class Game {
	State b;

	public Game() {
		b = new State();
		try {
			b.read("data/board.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public State minimax(State s, int forAgent, int maxDepth, int depth) {
		// base case, just returns itself
		if (s.isLeaf() || depth == maxDepth) {
			return s;
		}
		// general case finds the best value possible given the depth and return that
		// state
		// min -> min opponent score found in search; returnState -> remembers the state
		// to be returned; forAgent changes to other agent
		int min = 2;
		State returnState = new State();
		forAgent = ((forAgent == 0) ? 1 : 0);
//		System.out.println(forAgent);
		// loop goes through all legal moves and finds the minimum value that the
		// opponent can achieve finding the move that yields in min possible score for
		// opponent
		for (String move : s.legalMoves()) {
			State nextState = s.copy();
			nextState.execute(move);
			System.out.println(String.format("%d,%d", forAgent, depth));
//			System.out.println(move);
			System.out.println(nextState.toString());
			State searchResult = minimax(nextState, forAgent, maxDepth, depth + 1);
			if (searchResult.value(forAgent) < min) {
				returnState = searchResult;
			}
//			System.out.println(searchResult.value(forAgent));
//			System.out.println(searchResult + move);
//			System.out.println(depth);

		}
		return returnState;
	}

	public void test() {

		System.out.println(minimax(b, b.turn, 13, 0));

//		while (!b.isLeaf()){
//			System.out.println(b.toString());
//			System.out.println("Legal moves for agent with turn:"+b.legalMoves());
//			b.execute(b.legalMoves().get((int)(Math.random()*b.legalMoves().size())));
//		}
	}

}
