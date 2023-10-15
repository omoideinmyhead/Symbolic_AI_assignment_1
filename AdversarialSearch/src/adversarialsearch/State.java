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
	
	//constructs a State object via a file containing a board
	public void read(String file) throws IOException {
		//reads first line to figure out the number of rows and columns
		Scanner readfile = new Scanner(new File(file));
		String firstLine = readfile.nextLine();
		String[] segments = firstLine.split(" ");
		int columns = Integer.parseInt(segments[0]);
		int rows = Integer.parseInt(segments[1]);
		//initiate the board
		this.board = new char[rows][columns];
		int j = 0;
		//iterate through all lines of the .txt file filling the entries of board and adding additional info to the state
		while (j < rows) {
			int i = 0;
			String line = readfile.nextLine();
			while (i < columns) {
				char currentChar = line.charAt(i);
				//when the chars are A or B we update AgentX and AgentY and fill the board with a space character
				if (currentChar == 'A') {
					agentX[0] = i;
					agentY[0] = j;
					board[j][i] = ' ';
				} else if (currentChar == 'B') {
					agentX[1] = i;
					agentY[1] = j;
					board[j][i] = ' ';
				//in the other cases we just fill the board with the respective char in the .txt file
				} else {
					board[j][i] = currentChar;
				}
				;
				//we also use this opportunity to count the amount of food
				if (currentChar == '*') {
					food += 1;
				}
				;
				i++;
			}
			j++;
		}
	}

	//method that displays the state
	public String toString() {
		//we first iterate through the board chars adding them to a printable string
		String printable = "board:\n";
		for (char[] C : board) {
			for (char c : C) {
				printable += c;
			}
			printable += "\n";
		}
		//the we use string format method to display the other relevant info
		printable += String.format("agentX: (%d,%d)\n", agentX[0], agentX[1]);
		printable += String.format("agentY: (%d,%d)\n", agentY[0], agentY[1]);
		printable += String.format("score: (%d,%d)\n", score[0], score[1]);
		printable += String.format("turn: %d\n", turn);
		printable += String.format("food: %d\n", food);
		return printable;
	}

	//method designed to copy a State object by value
	public State copy() {
		State state2 = new State();
		//as board is an array of arrays we need to clone line by line
		state2.board = new char[board.length][];
		for (int i = 0; i < board.length; i++) {
			state2.board[i] = board[i].clone();
		}
		//for other arrays .clone() is enough
		state2.agentX = agentX.clone();
		state2.agentY = agentY.clone();
		state2.food = food;
		state2.score = score.clone();
		state2.turn = turn;
		return state2;
	}
	
	//method designed to return the legal moves
	public Vector<String> legalMoves(int agent) {
		//first saves the location of the current agent
		int[] Loc = { agentY[agent], agentX[agent] };
		Vector<String> moves = new Vector<String>();
		//when there is food at agent location we add eat to actions
		if (board[Loc[0]][Loc[1]] == '*') {
			moves.add("eat");
		}
		;
		//when there's food or empty space to the right we add right to actions
		if (board[Loc[0]][Loc[1] + 1] == ' ' || board[Loc[0]][Loc[1] + 1] == '*') {
			moves.add("right");
		}
		;
		//when there's food or empty space downwards we add down to actions
		if (board[Loc[0] + 1][Loc[1]] == ' ' || board[Loc[0] + 1][Loc[1]] == '*') {
			moves.add("down");
		}
		;
		//when there's food or empty space upwards we add down to actions
		if (board[Loc[0] - 1][Loc[1]] == ' ' || board[Loc[0] - 1][Loc[1]] == '*') {
			moves.add("up");
		}
		;
		//when there's food or empty space to the left we add left to actions
		if (board[Loc[0]][Loc[1] - 1] == ' ' || board[Loc[0]][Loc[1] - 1] == '*') {
			moves.add("left");
		}
		;
		//when there's an empty space at agent location we add block to actions
		if (board[Loc[0]][Loc[1]] == ' ') {
			moves.add("block");
		}
		;

		return moves;

	}

	//helper function that just return the legal moves for the agent currently with turn
	public Vector<String> legalMoves() {
		return legalMoves(turn);
	}

	//execute method responsible for executing an action
	public void execute(String action) {
		//simple switch block to handle all possible actions
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
		//changes the agent with turn
		turn = ((turn == 1) ? 0 : 1);
	}

	//method responsible for evaluating if a state is a leaf
	public boolean isLeaf() {
		//when there are no legal moves for the player with turn or when there is no more food the state is a leaf
		if (legalMoves().isEmpty()) {
			return true;
		} else if (food == 0) {
			return true;
		}
		//the state is not a leaf otherwise
		return false;
	}

	//method responsible for evaluating the local value of a state for a given agent
	public double value(int agent) {
		//first saves who the other agent is
		int otherAgent = ((agent == 0) ? 1 : 0);
		//in the case where the food is over if someone has a higher score they win
		if (food == 0) {
			if (score[agent] > score[otherAgent]) {
				return 1;
			} else if (score[agent] < score[otherAgent]) {
				return -1;
			}
		}
		//if someone has the turn and there are no legal moves they lose
		if (turn == otherAgent && legalMoves(otherAgent).isEmpty()) {
			return 1;
		}
		if (turn == agent && legalMoves(agent).isEmpty()) {
			return -1;
		}
		//all other states have a local value of 0
		return 0;
	}
}
