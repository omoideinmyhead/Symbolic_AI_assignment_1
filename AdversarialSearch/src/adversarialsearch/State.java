package adversarialsearch;

import java.io.*;
import java.util.*;

public class State {
	// create class attributes
	char[][] board; // the board as a 2D character array
	int[] agentX; // the x−coordinates of the agents
	int[] agentY; // the y−coordinates of the agents
	int[] score; // the amount of food eaten by each agent
	int turn; // who’s turn it is , agent 0 or agent 1
	int food; // the total amount of food still available
	Vector<String> moves;

	// create a class constructor for the State class
	public State() {
		this.board = null;
		this.agentX = new int[2];
		this.agentY = new int[2];
		this.score = new int[2];
		this.turn = 0;
		this.food = 0;
		this.moves = new Vector<String>();
	}

	public void read(String file) throws IOException {
		Scanner readfile = new Scanner(new File(file));
		String firstLine = readfile.nextLine();
		String[] segments = firstLine.split(" ");
		int columns = Integer.parseInt(segments[0]);
		int rows = Integer.parseInt(segments[1]);
		this.board = new char[rows][columns];
		int j = 0;
		while (j < rows) {
			int i = 0;
			String line = readfile.nextLine();
			while (i < columns) {
				char currentChar = line.charAt(i);
				if (currentChar == 'A') {
					agentX[0] = i;
					agentY[0] = j;
					board[j][i] = ' ';
				} else if (currentChar == 'B') {
					agentX[1] = i;
					agentY[1] = j;
					board[j][i] = ' ';
				} else {
					board[j][i] = currentChar;
				}
				;
				if (currentChar == '*') {
					food += 1;
				}
				;
				i++;
			}
			j++;
		}
	}

	public String toString() {
		String printable = "board:\n";
		for (char[] C : board) {
			for (char c : C) {
				printable += c;
			}
			printable += "\n";
		}
		printable += String.format("agentX: (%d,%d)\n", agentX[0], agentX[1]);
		printable += String.format("agentY: (%d,%d)\n", agentY[0], agentY[1]);
		printable += String.format("score: (%d,%d)\n", score[0], score[1]);
		printable += String.format("turn: %d\n", turn);
		printable += String.format("food: %d\n", food);
		return printable;
	}

	public State copy() {
		State state2 = new State();
		state2.board = new char[board.length][];
		for (int i = 0; i < board.length; i++) {
			state2.board[i] = board[i].clone();
		}
		state2.agentX = agentX.clone();
		state2.agentY = agentY.clone();
		state2.food = food;
		state2.score = score.clone();
		state2.turn = turn;
		return state2;
	}

	public Vector<String> legalMoves(int agent) {
		int[] Loc = { agentY[agent], agentX[agent] };
		Vector<String> moves = new Vector<String>();
		if (board[Loc[0]][Loc[1]] == '*') {
			moves.add("eat");
		}
		;
		if (board[Loc[0]][Loc[1] + 1] == ' ' || board[Loc[0]][Loc[1] + 1] == '*') {
			moves.add("right");
		}
		;
		if (board[Loc[0] + 1][Loc[1]] == ' ' || board[Loc[0] + 1][Loc[1]] == '*') {
			moves.add("down");
		}
		;
		if (board[Loc[0] - 1][Loc[1]] == ' ' || board[Loc[0] - 1][Loc[1]] == '*') {
			moves.add("up");
		}
		;
		if (board[Loc[0]][Loc[1] - 1] == ' ' || board[Loc[0]][Loc[1] - 1] == '*') {
			moves.add("left");
		}
		;
		if (board[Loc[0]][Loc[1]] == ' ') {
			moves.add("block");
		}
		;

		return moves;

	}

	public Vector<String> legalMoves() {
		return legalMoves(turn);
	}

	public void execute(String action) {
		switch (action) {
		case "eat":
			score[turn]++;
			board[agentY[turn]][agentX[turn]] = ' ';
			food--;
			break;
		case "up":
			agentY[turn]--;
			break;
		case "down":
			agentY[turn]++;
			break;
		case "left":
			agentX[turn]--;
			break;
		case "right":
			agentX[turn]++;
			break;
		case "block":
			board[agentY[turn]][agentX[turn]] = '#';
			break;
		}
		if (turn == 1) {
			turn = 0;
		} else {
			turn = 1;
		}
		;
	}

	public boolean isLeaf() {
		if (legalMoves().isEmpty()) {
			return true;
		} else if (food == 0) {
			return true;
		}
		return false;
	}

	public double value(int agent) {
		int otherAgent = ((agent == 0) ? 1 : 0);
		if (food == 0) {
			if (score[agent] > score[otherAgent]) {
				return 1;
			} else if (score[agent] < score[otherAgent]) {
				return -1;
			}
		}
		if (turn == otherAgent && legalMoves(otherAgent).isEmpty()) {
			return 1;
		}
		if (turn == agent && legalMoves(agent).isEmpty()) {
			return -1;
		}
		return 0;
	}
}
