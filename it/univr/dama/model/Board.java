package it.univr.dama.model;

public class Board {

	private static final int WHITE_SQUARE = -1;		//cella bianca
	private static final int BLACK_SQUARE = 0;		//cella nera
	private static final int WHITE_MAN = 1;			//pedina bianca
	private static final int BLACK_MAN = 2;			//pedina nera
	private static final int WHITE_KING = 3;		//dama bianca
	private static final int BLACK_KING = 4;		//dama nera
	
	private final int[][] board;
	
	public Board(){
		
		board = new int[8][8];
		
		for(int i=0; i<8; i++)
			for(int j=0; j<8; j++)
				if((i+j)%2 > 0)
					board[i][j] = WHITE_SQUARE;
				else if(i < 3)
					board[i][j] = BLACK_MAN;
				else if(i > 4)
					board[i][j] = WHITE_MAN;
				else
					board[i][j] = BLACK_SQUARE;	
	}
	
	public int getState(int x, int y){
		return board[x][y];
	}
	
	public void setState(int x, int y, int state){
		board[x][y] = state;
	}
	
}