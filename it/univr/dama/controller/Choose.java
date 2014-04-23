package it.univr.dama.controller;

import it.univr.dama.model.Board;

public class Choose {
	
	private final boolean turn;
	private final Board board;
	
	public Choose(boolean turn, Board board){
		this.turn = turn;
		this.board = board;
	}
	
	//controllo di eventuale uscita dal bordo di una posizione
	private int check(int c){
		if(c-1<0)			//esce dal bordo a sx o in alto in base a se c=j o c=i
			return 1;
		else if(c+1>7)		//esce dal bordo a dx o in basso in base a se c=j o c=i
			return 2;
		return 0;			//può andare ovunque
	}
	
	//controllo di eventuale uscita dal bordo di due posizioni
	private int dCheck(int c){
		if(c-2<0)			//esce dal bordo a sx o in alto in base a se c=j o c=i
			return 1;
		else if(c+2>7)		//esce dal bordo a dx o in basso in base a se c=j o c=i
			return 2;
		return 0;			//può mangiare ovunque
	}
	
	//funzione che ritorna la matrice con i valori pesati delle mosse concesse
	public int[][] allowedMoves(){
		int[][] ris = new int[8][8];
		int dVerCheck;
		int verCheck;
		int dHorCheck;
		int horCheck;
		
		for(int i=0; i < 8; i++)
			for (int j=0; j < 8; j++){
				
				dVerCheck = dCheck(j);
				verCheck = check(j);
				dHorCheck = dCheck(i);
				horCheck = check(i);
				
				//CASO DELLA PEDINA
				if((turn==true && board.getState(i,j)==1) || (turn==false && board.getState(i,j)==2)){
					
					ris[i][j] = manCase(i, j, dVerCheck, verCheck, dHorCheck, horCheck);
				}
		
				//CASO DELLA DAMA
				else if((turn==true && board.getState(i,j)==3) || (turn==false && board.getState(i,j)==4)){
					
					ris[i][j] = kingCase(i, j, dVerCheck, verCheck, dHorCheck, horCheck);
				}
			}
		
		return grantedMoves(ris);
	}
	
	//###############################################################################################################################################################
	//funzione che gestisce il CASO DELLA PEDINA
	private int manCase(int i, int j, int dVerCheck, int verCheck, int dHorCheck, int horCheck){
		
		//caso della mangiata
		
			//tocca al nero, mangia ovunque, non raggiunge bordo, dx o sx libere
			if(turn==false && dVerCheck==0 && (dHorCheck==0 || dHorCheck==1) && (board.getState(i+1, j-1)==1 && board.getState(i+2, j-2)==0 || board.getState(i+1, j+1)==1 && board.getState(i+2, j+2)==0)
			//tocca al nero, mangia solo dx, non raggiunge bordo, dx libera  
			|| turn==false && dVerCheck==1 && (dHorCheck==0 || dHorCheck==1) && board.getState(i+1, j+1)==1 && board.getState(i+2, j+2)==0
			//tocca al nero, mangia solo sx, non raggiunge bordo, sx libera  
			|| turn==false && dVerCheck==2 && (dHorCheck==0 || dHorCheck==1) && board.getState(i+1, j-1)==1 && board.getState(i+2, j-2)==0
			//tocca al bianco, mangia ovunque, non raggiunge bordo, dx o sx libere
			|| turn==true && dVerCheck==0 && (dHorCheck==0 || dHorCheck==2) && (board.getState(i-1, j-1)==2 && board.getState(i-2, j-2)==0 || board.getState(i-1, j+1)==2 && board.getState(i-2, j+2)==0)
			//tocca al bianco, mangia solo dx, non raggiunge bordo, dx libera  
			|| turn==true && dVerCheck==1 && (dHorCheck==0 || dHorCheck==2) && board.getState(i-1, j+1)==2 && board.getState(i-2, j+2)==0
			//tocca al bianco, mangia solo sx, non raggiunge bordo, sx libera  
			|| turn==true && dVerCheck==2 && (dHorCheck==0 || dHorCheck==2) && board.getState(i-1, j-1)==2 && board.getState(i-2, j-2)==0 
			)
				return 2;
		
		//caso della mossa semplice
			
			//tocca al nero, va ovunque, non raggiunge bordo, dx o sx libere
			else if(turn==false && verCheck==0 && (board.getState(i+1, j-1)==0 || board.getState(i+1, j+1)==0)
			//tocca al nero, va solo dx, non raggiunge bordo, dx libera  
			|| turn==false && verCheck==1 && board.getState(i+1, j+1)==0
			//tocca al nero, va solo sx, non raggiunge bordo, sx libera  
			|| turn==false && verCheck==2 && board.getState(i+1, j-1)==0
			//tocca al bianco, va ovunque, non raggiunge bordo, dx o sx libere  
			|| turn==true && verCheck==0 && (board.getState(i-1, j-1)==0 || board.getState(i-1, j+1)==0)
			//tocca al bianco, va solo dx, non raggiunge bordo, dx libera 
			|| turn==true && verCheck==1 && board.getState(i-1, j+1)==0
			//tocca al bianco, va solo sx, non raggiunge bordo, sx libera
			|| turn==true && verCheck==2 && board.getState(i-1, j-1)==0
			)
				return 1;

		//caso bloccato
		else
			return 0;
	}
	
	//###############################################################################################################################################################
	//funzione che gestisce il CASO DELLA DAMA
	private int kingCase(int i, int j, int dVerCheck, int verCheck, int dHorCheck, int horCheck){
		
		//caso della mangiata
		
			//tocca al nero, mangia ovunque, non raggiunge bordo, dx o sx e su o e giù liberi
			if(turn==false && dVerCheck==0 && dHorCheck==0 && ( board.getState(i+1, j-1)==1 && board.getState(i+2, j-2)==0 ||	//pedina bianca basso sx, vuoto
																board.getState(i+1, j-1)==3 && board.getState(i+2, j-2)==0 ||	//dama bianca basso sx, vuoto 
																board.getState(i+1, j+1)==1 && board.getState(i+2, j+2)==0 ||	//pedina bianca basso dx, vuoto
																board.getState(i+1, j+1)==3 && board.getState(i+2, j+2)==0 ||	//dama bianca basso dx, vuoto
																board.getState(i-1, j-1)==1 && board.getState(i-2, j-2)==0 ||	//pedina bianca alto sx, vuoto
																board.getState(i-1, j-1)==3 && board.getState(i-2, j-2)==0 ||	//dama bianca alto sx, vuoto
																board.getState(i-1, j+1)==1 && board.getState(i-2, j+2)==0 ||	//pedina bianca alto dx, vuoto
																board.getState(i-1, j+1)==3 && board.getState(i-2, j+2)==0)		//dama bianca alto dx, vuoto
			//tocca al nero, mangia ovunque, raggiungerebbe bordo in su, dx o sx e giù libera  
			|| turn==false && dVerCheck==0 && dHorCheck==1 && ( board.getState(i+1, j-1)==1 && board.getState(i+2, j-2)==0 ||	//pedina bianca basso sx, vuoto
																board.getState(i+1, j-1)==3 && board.getState(i+2, j-2)==0 ||	//dama bianca basso sx, vuoto 
																board.getState(i+1, j+1)==1 && board.getState(i+2, j+2)==0 ||	//pedina bianca basso dx, vuoto
																board.getState(i+1, j+1)==3 && board.getState(i+2, j+2)==0)		//dama bianca basso dx, vuoto
			//tocca al nero, mangia ovunque, raggiungerebbe bordo in giù, dx o sx e su libera  
			|| turn==false && dVerCheck==0 && dHorCheck==2 && ( board.getState(i-1, j-1)==1 && board.getState(i-2, j-2)==0 ||	//pedina bianca alto sx, vuoto
																board.getState(i-1, j-1)==3 && board.getState(i-2, j-2)==0 ||	//dama bianca alto sx, vuoto
																board.getState(i-1, j+1)==1 && board.getState(i-2, j+2)==0 ||	//pedina bianca alto dx, vuoto
																board.getState(i-1, j+1)==3 && board.getState(i-2, j+2)==0)		//dama bianca alto dx, vuoto
			//tocca al nero, mangia solo dx, non raggiunge bordo, dx e su o giù liberi  
			|| turn==false && dVerCheck==1 && dHorCheck==0 && ( board.getState(i+1, j+1)==1 && board.getState(i+2, j+2)==0 ||	//pedina bianca basso dx, vuoto
																board.getState(i+1, j+1)==3 && board.getState(i+2, j+2)==0 ||	//dama bianca basso dx, vuoto
																board.getState(i-1, j+1)==1 && board.getState(i-2, j+2)==0 ||	//pedina bianca alto dx, vuoto
																board.getState(i-1, j+1)==3 && board.getState(i-2, j+2)==0)		//dama bianca alto dx, vuoto
			//tocca al nero, mangia solo dx, raggiungerebbe bordo, dx e giù libera  
			|| turn==false && dVerCheck==1 && dHorCheck==1 && ( board.getState(i+1, j+1)==1 && board.getState(i+2, j+2)==0 ||	//pedina bianca basso dx, vuoto
																board.getState(i+1, j+1)==3 && board.getState(i+2, j+2)==0)		//dama bianca basso dx, vuoto
			//tocca al nero, mangia solo dx, raggiungerebbe bordo, dx e su libera  
			|| turn==false && dVerCheck==1 && dHorCheck==2 && ( board.getState(i-1, j+1)==1 && board.getState(i-2, j+2)==0 ||	//pedina bianca alto dx, vuoto
																board.getState(i-1, j+1)==3 && board.getState(i-2, j+2)==0)		//dama bianca alto dx, vuoto
			//tocca al nero, mangia solo sx, non raggiunge bordo, sx e su o giù liberi  
			|| turn==false && dVerCheck==2 && dHorCheck==0 && ( board.getState(i+1, j-1)==1 && board.getState(i+2, j-2)==0 ||	//pedina bianca basso sx, vuoto
																board.getState(i+1, j-1)==3 && board.getState(i+2, j-2)==0 ||	//dama bianca basso sx, vuoto
																board.getState(i-1, j-1)==1 && board.getState(i-2, j-2)==0 ||	//pedina bianca alto sx, vuoto
																board.getState(i-1, j-1)==3 && board.getState(i-2, j-2)==0)		//dama bianca alto sx, vuoto
			//tocca al nero, mangia solo sx, raggiungerebbe bordo, sx e giù libera  
			|| turn==false && dVerCheck==2 && dHorCheck==1 && ( board.getState(i+1, j-1)==1 && board.getState(i+2, j-2)==0 ||	//pedina bianca basso sx, vuoto
																board.getState(i+1, j-1)==3 && board.getState(i+2, j-2)==0)		//dama bianca basso sx, vuoto
			//tocca al nero, mangia solo sx, raggiungerebbe bordo, sx e su libera  
			|| turn==false && dVerCheck==2 && dHorCheck==2 && ( board.getState(i-1, j-1)==1 && board.getState(i-2, j-2)==0 ||	//pedina bianca alto sx, vuoto
																board.getState(i-1, j-1)==3 && board.getState(i-2, j-2)==0)		//dama bianca alto sx, vuoto
			  
			//tocca al bianco, mangia ovunque, non raggiunge bordo, dx o sx e su o giù liberi													
			|| turn==true && dVerCheck==0 && dHorCheck==0 && (  board.getState(i+1, j-1)==2 && board.getState(i+2, j-2)==0 ||	//pedina nera basso sx, vuoto
																board.getState(i+1, j-1)==4 && board.getState(i+2, j-2)==0 ||	//dama nera basso sx, vuoto 
																board.getState(i+1, j+1)==2 && board.getState(i+2, j+2)==0 ||	//pedina nera basso dx, vuoto
																board.getState(i+1, j+1)==4 && board.getState(i+2, j+2)==0 ||	//dama nera basso dx, vuoto
																board.getState(i-1, j-1)==2 && board.getState(i-2, j-2)==0 ||	//pedina nera alto sx, vuoto
																board.getState(i-1, j-1)==4 && board.getState(i-2, j-2)==0 ||	//dama nera alto sx, vuoto
																board.getState(i-1, j+1)==2 && board.getState(i-2, j+2)==0 ||	//pedina nera alto dx, vuoto
																board.getState(i-1, j+1)==4 && board.getState(i-2, j+2)==0)		//dama nera alto dx, vuoto
			//tocca al bianco, mangia ovunque, raggiungerebbe bordo in su, dx o sx e giù libera  
			|| turn==true && dVerCheck==0 && dHorCheck==1 && (  board.getState(i+1, j-1)==2 && board.getState(i+2, j-2)==0 ||	//pedina nera basso sx, vuoto
																board.getState(i+1, j-1)==4 && board.getState(i+2, j-2)==0 ||	//dama nera basso sx, vuoto 
																board.getState(i+1, j+1)==2 && board.getState(i+2, j+2)==0 ||	//pedina nera basso dx, vuoto
																board.getState(i+1, j+1)==4 && board.getState(i+2, j+2)==0)		//dama nera basso dx, vuoto
			//tocca al bianco, mangia ovunque, raggiungerebbe bordo in giù, dx o sx e su libera  
			|| turn==true && dVerCheck==0 && dHorCheck==2 && (  board.getState(i-1, j-1)==2 && board.getState(i-2, j-2)==0 ||	//pedina nera alto sx, vuoto
																board.getState(i-1, j-1)==4 && board.getState(i-2, j-2)==0 ||	//dama nera alto sx, vuoto
																board.getState(i-1, j+1)==2 && board.getState(i-2, j+2)==0 ||	//pedina nera alto dx, vuoto
																board.getState(i-1, j+1)==4 && board.getState(i-2, j+2)==0)		//dama nera alto dx, vuoto
			//tocca al bianco, mangia solo dx, non raggiunge bordo, dx e su o giù liberi  
			|| turn==true && dVerCheck==1 && dHorCheck==0 && (  board.getState(i+1, j+1)==2 && board.getState(i+2, j+2)==0 ||	//pedina nera basso dx, vuoto
																board.getState(i+1, j+1)==4 && board.getState(i+2, j+2)==0 ||	//dama nera basso dx, vuoto
																board.getState(i-1, j+1)==2 && board.getState(i-2, j+2)==0 ||	//pedina nera alto dx, vuoto
																board.getState(i-1, j+1)==4 && board.getState(i-2, j+2)==0)		//dama nera alto dx, vuoto
			//tocca al bianco, mangia solo dx, raggiungerebbe bordo, dx e giù libera  
			|| turn==true && dVerCheck==1 && dHorCheck==1 && (  board.getState(i+1, j+1)==2 && board.getState(i+2, j+2)==0 ||	//pedina nera basso dx, vuoto
																board.getState(i+1, j+1)==4 && board.getState(i+2, j+2)==0)		//dama nera basso dx, vuoto
			//tocca al bianco, mangia solo dx, raggiungerebbe bordo, dx e su libera  
			|| turn==true && dVerCheck==1 && dHorCheck==2 && (  board.getState(i-1, j+1)==2 && board.getState(i-2, j+2)==0 ||	//pedina nera alto dx, vuoto
																board.getState(i-1, j+1)==4 && board.getState(i-2, j+2)==0)		//dama nera alto dx, vuoto
			//tocca al bianco, mangia solo sx, non raggiunge bordo, sx e su o giù liberi  
			|| turn==true && dVerCheck==2 && dHorCheck==0 && (  board.getState(i+1, j-1)==2 && board.getState(i+2, j-2)==0 ||	//pedina nera basso sx, vuoto
																board.getState(i+1, j-1)==4 && board.getState(i+2, j-2)==0 ||	//dama nera basso sx, vuoto
																board.getState(i-1, j-1)==2 && board.getState(i-2, j-2)==0 ||	//pedina nera alto sx, vuoto
																board.getState(i-1, j-1)==4 && board.getState(i-2, j-2)==0)		//dama nera alto sx, vuoto
			//tocca al bianco, mangia solo sx, raggiungerebbe bordo, sx e giù libera  
			|| turn==true && dVerCheck==2 && dHorCheck==1 && (  board.getState(i+1, j-1)==2 && board.getState(i+2, j-2)==0 ||	//pedina nera basso sx, vuoto
																board.getState(i+1, j-1)==4 && board.getState(i+2, j-2)==0)		//dama nera basso sx, vuoto
			//tocca al bianco, mangia solo sx, raggiungerebbe bordo, sx e su libera 
			|| turn==true && dVerCheck==2 && dHorCheck==2 && (  board.getState(i-1, j-1)==2 && board.getState(i-2, j-2)==0 ||	//pedina nera alto sx, vuoto
																board.getState(i-1, j-1)==4 && board.getState(i-2, j-2)==0)		//dama nera alto sx, vuoto
			)
				return 2;
	
		//caso della mossa semplice
			
			//tocca al nero o bianco, va ovunque, non raggiunge bordo, dx o sx e su o giù liberi
			else if(verCheck==0 && horCheck==0 && (board.getState(i+1, j-1)==0 || board.getState(i+1, j+1)==0 || board.getState(i-1, j-1)==0 || board.getState(i-1, j+1)==0)
			//tocca al nero o bianco, va ovunque, raggiungerebbe bordo in su, dx o sx e giù libera  
			|| verCheck==0 && horCheck==1 && (board.getState(i+1, j-1)==0 || board.getState(i+1, j+1)==0)
			//tocca al nero o bianco, va ovunque, raggiungerebbe bordo in giù, dx o sx e su libera  
			|| verCheck==0 && horCheck==2 && (board.getState(i-1, j-1)==0 || board.getState(i-1, j+1)==0)
			//tocca al nero o bianco, va solo dx, non raggiunge bordo, dx e su o giù liberi  
			|| verCheck==1 && horCheck==0 && (board.getState(i+1, j+1)==0 || board.getState(i-1, j+1)==0)
			//tocca al nero o bianco, va solo dx, raggiungerebbe bordo, dx e giù libera (ANGOLO ALTO A SX)  
			|| verCheck==1 && horCheck>0 && (board.getState(i+1, j+1)==0)
			//tocca al nero o bianco, va solo sx, non raggiunge bordo, sx e su o giù liberi  
			|| verCheck==2 && horCheck==0 && (board.getState(i+1, j-1)==0 || board.getState(i-1, j-1)==0)
			//tocca al nero o bianco, va solo sx, raggiungerebbe bordo, sx e su libera (ANGOLO BASSO DX)  
			|| verCheck==2 && horCheck>0 && (board.getState(i-1, j-1)==0)
			)
				return 1;
		
		//caso bloccato
		else
			return 0;
	}
	
	//###############################################################################################################################################################
	//funzione che lascia nella matrice risultato SOLO i valori delle mosse obbligatorie,
	//e mette a 0 tutti gli altri
	private int[][] grantedMoves(int[][] ris){
		
		//scansione della matrice per vedere il peso di massima priorità
		int max=0;
		for(int i=0; i<8; i++)
			for(int j=0; j<8; j++)
				if(ris[i][j]>max)
					max=ris[i][j];
		
		//modifica della della matrice lasciando solo >0 le celle delle mosse possibili
		if(max == 2)
			for(int i=0; i<8; i++)
				for(int j=0; j<8; j++)
					if(ris[i][j]==1)
						ris[i][j]=0;
		
		return ris;
	}
	
}
