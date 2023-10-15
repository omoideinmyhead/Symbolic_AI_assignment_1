package adversarialsearch;

import java.io.IOException;
import java.util.Vector;

public class Game {
	State b;
	int countMinimax;
	int countAlfabeta;

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
		// base case, leaf or maxDepth just returns itself
		if (s.isLeaf() || depth == maxDepth) {
			countMinimax++;
			return s;
		}
		// general case finds the best value possible (max or min) and returns the state
		// max/min -> keep track of max and min scores;
		// returnState -> remembers the state to be returned;
		State returnState = new State();
		double max = -2;
		double min = 2;
		// loop goes through all legal moves and finds the best move considering if the player is forAgent or not
		for (String move : s.legalMoves()) {
			countMinimax++;
			// copies the state to execute the move, necessary because we have to remember the original state for the next moves
			State nextState = s.copy();
			nextState.execute(move);
			// we recursively find the result state and save its value in resultValue to optimize the rest of the code (don't have to evaluate it 3 times)
			State searchResult = minimax(nextState, forAgent, maxDepth, depth + 1);
			double resultValue = searchResult.value(forAgent);
			// if the turn belongs to the forAgent we want the state of max value
			if (forAgent == s.turn) {
				if (resultValue > max) {
					returnState = searchResult;
					max = resultValue;
				}
			// else the turn belongs to his opponent and they want the state of min value
			} else {
				if (resultValue < min) {
					returnState = searchResult;
					min = resultValue;
				}
			}
		}
		// after the loop we return the state of max or min value
		return returnState;
	}

	public State alfabeta(State s, int forAgent, int maxDepth, int depth, double alfa, double beta) {
		// base case, leaf or maxDepth just returns itself
		if (s.isLeaf() || depth == maxDepth) {
			countAlfabeta ++;
			return s;
		}
		// general case finds the best value possible (max or min) and returns the state
		// max/min -> keep track of max and min scores;
		// returnState -> remembers the state to be returned;
		State returnState = new State();
		double max = -2;
		double min = 2;
		// loop goes through all legal moves and finds the best move considering if the player is forAgent or not
		for (String move : s.legalMoves()) {
			countAlfabeta ++;
			// copies the state to execute the move, necessary because we have to remember the original state for the next moves
			State nextState = s.copy();
			nextState.execute(move);
			// we recursively find the result state and save its value in resultValue to optimize the rest of the code (don't have to evaluate it 3 times)
			State searchResult = alfabeta(nextState, forAgent, maxDepth, depth + 1, alfa, beta);
			double resultValue = searchResult.value(forAgent);
			// if the turn belongs to the forAgent we want the state of max value
			if (forAgent == s.turn) {
				// if the value is greater than or equal beta this subtree will not be relevant in actual play and we can already return the result
				if (resultValue >= beta) {
					return searchResult;
				}
				if (resultValue > max) {
					returnState = searchResult;
					max = resultValue;
					alfa = max;
				}
			} else {
				// if the value is lower than or equal alfa this subtree will not be relevant in actual play and we can already return the result
				if (resultValue <= alfa) {
					return searchResult;
				}
				if (resultValue < min) {
					returnState = searchResult;
					min = resultValue;
					beta = min;
				}
			}
		}
		// System.out.println(returnState);
		return returnState;
	}

	public void test() {
		for (int i=7;i<14;i+=2 ) {
			countMinimax = 0;
			minimax(b, b.turn, i, 0);
			countAlfabeta = 0;
			alfabeta(b, b.turn, i, 0, -2, 2);
			System.out.println(String.format("turn %d: minimax = %d runs; alfabeta = %d runs", i, countMinimax, countAlfabeta));
		}
		System.out.println(alfabeta(b, b.turn, 11, 0, -2, 2));

//		while (!b.isLeaf()){
//			System.out.println(b.toString());
//			System.out.println("Legal moves for agent with turn:"+b.legalMoves());
//			b.execute(b.legalMoves().get((int)(Math.random()*b.legalMoves().size())));
//		}
	}

}
