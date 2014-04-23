package it.univr.dama.view;

import it.univr.dama.model.Board;
import it.univr.dama.controller.Choose;

public class Main {
	
	public static void main(String[] args) {
		Board board = new Board();
		boolean turn = true;
		
		Choose choose = new Choose(turn, board);
		int[][] allowedMoves = choose.allowedMoves();
		
		Viewer viewer = new Viewer(board, allowedMoves, turn);
		viewer.showBoard();
					
	}
}