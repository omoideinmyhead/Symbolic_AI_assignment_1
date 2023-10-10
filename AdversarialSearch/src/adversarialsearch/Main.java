package adversarialsearch;

import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		State table = new State();
		try {
			table.read("data/board.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(table.toString());
		Game g=new Game();

		g.test();
			
	}
}
