package it.univr.dama.view;

import javax.swing.JFrame;

public class WindowBoard extends JFrame {
	private static final int WIDTH = 600;
	private static final int HEIGHT = 600;
	
	public WindowBoard(){
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setResizable(false);
		this.setTitle("DAMA");

	}
	
}