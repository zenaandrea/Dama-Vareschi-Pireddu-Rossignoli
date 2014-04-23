package it.univr.dama.view;

import it.univr.dama.controller.IllegalChoiceException;
import it.univr.dama.controller.Move;
import it.univr.dama.model.Board;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Square extends JButton {
	
	private Move move;
	private int x;
	private int y;
	private Board board;
	private int[][] allowedMoves;
	private boolean turn;
	
	public Square(String path, int i, int j, Board board, int[][] allowedMoves, boolean turn){
		
		setIcon(new javax.swing.ImageIcon(path));
		
		x = i;
		y = j;
		
		this.board = board;
		this.allowedMoves = allowedMoves;
		this.turn = turn;
		
		setListener();
	}
	
	private void setListener(){	
		
		addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				move = new Move(x, y, board, allowedMoves, turn);
				try {
					move.rePrint();
				}
				catch (IllegalChoiceException e1) {
					System.out.println("Scelta non valida");
				}
			}
			
		});
	}
}