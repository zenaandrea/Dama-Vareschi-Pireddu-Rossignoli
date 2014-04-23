package it.univr.dama.view;

import java.awt.BorderLayout;
import java.awt.Window;

import it.univr.dama.model.Board;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Viewer {

	private static Board board;
	private static int[][] allowedMoves;
	private static boolean turn;
	private static final JFrame wBoard = new WindowBoard();
	private static final JFrame wWinner = new WindowWinner();
	
	public Viewer(Board board, int[][] allowedMoves, boolean turn) {
		
		this.board = board;
		this.allowedMoves = allowedMoves;
		this.turn = turn;
		
	}
	
	//funzione che mostra la finestra della damiera
	public  void showBoard(){
		
		wBoard.getContentPane().removeAll();
		wBoard.setLayout(new java.awt.GridLayout(8, 8));
		
		wWinner.getContentPane().removeAll();
		wWinner.setLayout(new java.awt.BorderLayout());
		
		for(int i=0; i<8; i++)
			for (int j=0; j<8; j++)
				setSquare(i, j);
									
		wBoard.setVisible(true);
		
		showWinner();
		
	}
	
	//funzione che setta la cella della damiera in base al suo stato
	private static void setSquare(int i, int j){
		
		if (board.getState(i, j) == -1)
			wBoard.add(new Square("img/cellaBianca.jpg", i, j, board, allowedMoves, turn));
		else if(board.getState(i, j) == 0)
			wBoard.add(new Square("img/cellaNera.jpg", i, j, board, allowedMoves, turn));
		else if(board.getState(i, j) == -2)
			wBoard.add(new Square("img/cellaEvidenziata.jpg", i, j, board, allowedMoves, turn));
		else if(board.getState(i, j) == 1)
			if(allowedMoves[i][j]==0)
				wBoard.add(new Square("img/pedinaBianca.jpg", i, j, board, allowedMoves, turn));
			else
				wBoard.add(new Square("img/pedinaBianca_evid.jpg", i, j, board, allowedMoves, turn));
		else if(board.getState(i, j) == 2)
			if(allowedMoves[i][j]==0)
				wBoard.add(new Square("img/pedinaNera.jpg", i, j, board, allowedMoves, turn));
			else
				wBoard.add(new Square("img/pedinaNera_evid.jpg", i, j, board, allowedMoves, turn));
		else if(board.getState(i, j) == 3)
			if(allowedMoves[i][j]==0)
				wBoard.add(new Square("img/damaBianca.jpg", i, j, board, allowedMoves, turn));
			else
				wBoard.add(new Square("img/damaBianca_evid.jpg", i, j, board, allowedMoves, turn));
		else if(board.getState(i, j) == 4)
			if(allowedMoves[i][j]==0)
				wBoard.add(new Square("img/damaNera.jpg", i, j, board, allowedMoves, turn));
			else
				wBoard.add(new Square("img/damaNera_evid.jpg", i, j, board, allowedMoves, turn));
	}
	
	//funzione che mostra la finestra che decreta il vncitore a fine partita
	private static void showWinner(){
		JLabel label = new JLabel("");
		
		int indexMove=0;
		for(int a=0; a<8 && indexMove==0; a++)
			for(int b=0; b<8 && indexMove==0; b++)
				if(allowedMoves[a][b]>indexMove)
					indexMove=allowedMoves[a][b];
		
		if(indexMove==0){
			if(turn==true)
				label = new JLabel(new ImageIcon("img/vince_nero.jpg"));
			else if(turn==false)
				label = new JLabel(new ImageIcon("img/vince_bianco.jpg"));
			wWinner.add(label, BorderLayout.CENTER);
			wWinner.setVisible(true);
			wWinner.setBounds(650, 0, 625, 312);
		}
	}

}
