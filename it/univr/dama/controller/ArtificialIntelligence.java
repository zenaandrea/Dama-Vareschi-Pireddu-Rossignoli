package it.univr.dama.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import it.univr.dama.model.Board;

public class ArtificialIntelligence {

	private Board board;
	private int[][] allowedMoves;
	private boolean turn;
	private boolean aiMove;
	private javax.swing.Timer timer;
	private ActionListener listener;
	
	public ArtificialIntelligence(Board board, int[][] allowedMoves, boolean turn, boolean aiMove){
		this.board = board;
		this.allowedMoves = allowedMoves;
		this.turn = turn;
		this.aiMove = aiMove;
		
		//ascoltatore del timer
		listener = new ActionListener() {
			public void actionPerformed(ActionEvent e){
				
				effectiveChoiceAi();	//funzione per l'effettiva scelta dell'IA
				timer.stop();			//viene bloccato il timer
			}
		};
		
		this.timer = new javax.swing.Timer(500, listener);
	}
	
	private void effectiveChoiceAi(){
		
		int[] coord = new int[2];
		
		if(aiMove == true)
			coord = decidePiece();
		else if(aiMove == false)
			coord = decideDestination();
		
		Move move = new Move(coord[0], coord[1], board, allowedMoves, turn);
		try {
			move.rePrint();
		}
		catch (IllegalChoiceException e) {
			System.out.println("Scelta non valida");
		}
	}
	
	public void choiceAi() throws IllegalChoiceException{
		
		timer.start(); 		//viene fatto partire un timer e chiamato il suo ascoltatore
		
	}

	private int check(int c){
		if(c-1<0)			//esce dal bordo a sx o in alto in base a se c=j o c=i
			return 1;
		else if(c+1>7)		//esce dal bordo a dx o in basso in base a se c=j o c=i
			return 2;
		return 0;			//può andare ovunque
	}
	
	private int dCheck(int c){
		if(c-2<0)			//esce dal bordo a sx o in alto in base a se c=j o c=i
			return 1;
		else if(c+2>7)		//esce dal bordo a dx o in basso in base a se c=j o c=i
			return 2;
		return 0;			//può mangiare ovunque
	}
	
	//**************************************************************************************************
	//FUNZIONE PER LA DECISIONE DEL PEZZO DA MUOVERE
	private int[] decidePiece(){
		
		int[][] safeMoves = new int[8][8];
		boolean can = false;
		int count = 0;
		int numPiece;
		int[] coord = new int[2];
		
		for(int a=0; a<8; a++)
			for(int b=0; b<8; b++)
				safeMoves[a][b] = 0;						//azzeramento della matrice delle mosse safe
		
		for(int i=0; i<8; i++)
			for(int j=0; j<8; j++)
				if(allowedMoves[i][j] > 0){
					
					coord[0] = i;							//tengo salvate le coordinate dell'ultima mossa possibile
					coord[1] = j;							//(nel caso tutte le mosse siano unsafe)
					
					//se la pedina può diventare dama, la mossa assume maggiore priorità
					if(board.getState(i, j)==2 && allowedMoves[i][j]==1 && i==6
					  && (check(j)!=1 && board.getState(7,j-1)==0 || check(j)!=2 && board.getState(7,j+1)==0))
						return coord;
					
					can = simulate(i, j);					//simula per vedere se il pezzo può fare almeno una mossa safe
					if(can == true){						//se la mossa è safe:
						safeMoves[i][j] = 1;
						count++;								//incremento il contatore delle mosse safe		
					}
					
					//se non ci sono mosse safe e il pezzo si trova già in una situazione unsafe, scappa
					if(count==0 && isSafe(i, j, board.getState(i, j)) == false)
						return coord;
				}
		
		//se sono state trovate mosse safe
		if(count > 0){
			
			//genero un numero casuale tra le possibili mosse safe
			Random r = new Random();
			numPiece = r.nextInt(count)+1;
			
			//doppio ciclo che seleziona le coordinate del pezzo in ordine al numero casuale
			count = 0;
			for(int x=0; x<8; x++)
				for(int y=0; y<8; y++)
					if(safeMoves[x][y] == 1){		
						count++;					//contatore riprende a contare le mosse safe
						if(count == numPiece){		//quando il suo valore raggiunge quello casuale
							coord[0] = x;			//salvo le coordinate
							coord[1] = y;
						}
					}
		}
		
		return coord;	
	}

	//funzione per la simulazione della DECISIONE DEL PEZZO DA MUOVERE
	private boolean simulate(int i, int j){
		
		//spostamento pedina
		if(allowedMoves[i][j]==1 && board.getState(i,j)==2){
			//non esce dal bordo sinistro e la cella in basso a sx è vuota
			if(check(j)!=1 && board.getState(i+1, j-1)==0)
				if(isSafe(i+1, j-1, board.getState(i, j)) == true)
					return true;
			//non esce dal bordo destro e la cella in basso a dx è vuota
			if(check(j)!=2 && board.getState(i+1, j+1)==0)
				if(isSafe(i+1, j+1, board.getState(i, j)) == true)
					return true;
			return false;
		}
		//spostamento dama
		else if(allowedMoves[i][j]==1 && board.getState(i,j)==4){
			//non esce da sopra e dal bordo sinistro e la cella in alto a sx è vuota
			if(check(i)!=1 && check(j)!=1 && board.getState(i-1, j-1)==0)
				if(isSafe(i-1, j-1, board.getState(i, j)) == true)
					return true;
			//non esce da sotto e dal bordo sinistro e la cella in basso a sx è vuota
			if(check(i)!=2 && check(j)!=1 && board.getState(i+1, j-1)==0)
				if(isSafe(i+1, j-1, board.getState(i, j)) == true)
					return true;
			//non esce da sopra e dal bordo destro e la cella in alto a dx è vuota
			if(check(i)!=1 && check(j)!=2 && board.getState(i-1, j+1)==0)
				if(isSafe(i-1, j+1, board.getState(i, j)) == true)
					return true;
			//non esce da sotto e dal bordo destro e la cella in basso a dx è vuota
			if(check(i)!=2 && check(j)!=2 && board.getState(i+1, j+1)==0)
				if(isSafe(i+1, j+1, board.getState(i, j)) == true)
					return true;
			return false;
		}
		//mangiata pedina e dama
		else if(allowedMoves[i][j]==2 && (board.getState(i,j)==2 || board.getState(i,j)==4)){
			return true;
		}
		
		return false;

	}
	
	//funzione che controlla se lo spostamento in una certa cella è safe
	boolean isSafe(int i, int j, int pieceState){
		//non esce da sopra e dal bordo sinistro e nella cella in alto a sx c'è una minaccia
		if(check(i)!=1 && check(j)!=1 && (pieceState==2 && board.getState(i-1, j-1)==1 || board.getState(i-1, j-1)==3))
			return false;
		//non esce da sotto e dal bordo sinistro e nella cella in basso a sx c'è una minaccia
		if(check(i)!=2 && check(j)!=1 && (pieceState==2 && board.getState(i+1, j-1)==1 || board.getState(i+1, j-1)==3))
			return false;
		//non esce da sopra e dal bordo destro e nella cella in alto a dx c'è una minaccia
		if(check(i)!=1 && check(j)!=2 && (pieceState==2 && board.getState(i-1, j+1)==1 || board.getState(i-1, j+1)==3))
			return false;
		//non esce da sotto e dal bordo destro e nella cella in basso a dx c'è una minaccia
		if(check(i)!=2 && check(j)!=2 && (pieceState==2 && board.getState(i+1, j+1)==1 || board.getState(i+1, j+1)==3))
			return false;
		return true;
	}
	
	//**************************************************************************************************
	//FUNZIONE PER LA DECISIONE DELLA DESTINAZIONE
		private int[] decideDestination(){
			
			int[][] possibleDestinations = new int[8][8];
			int count = 0;
			int numDest;
			int[] coord = new int[2];
			
			for(int a=0; a<8; a++)
				for(int b=0; b<8; b++)
					possibleDestinations[a][b] = 0;			//azzeramento della matrice delle possibili destinazioni
			
			for(int i=0; i<8; i++)
				for(int j=0; j<8; j++)
					if(board.getState(i, j)==-2){
						//decisione di spostare il pezzo (pedina o dama) in basso verso destra
						if(dCheck(j)!=1 && board.getState(i, j-2)==-2 && i-1>=0 && (board.getState(i-1, j-1)==2 || board.getState(i-1, j-1)==4)){
							if(isSafe(i, j, board.getState(i-1, j-1)) == true){
								possibleDestinations[i][j] = 1;
								count++;
							}
						}
						//decisione di spostare il pezzo (pedina o dama) in basso verso sinistra
						if(dCheck(j)!=2 && board.getState(i, j+2)==-2 && i-1>=0 && (board.getState(i-1, j+1)==2 || board.getState(i-1, j+1)==4)){
							if(isSafe(i, j, board.getState(i-1, j+1)) == true){
								possibleDestinations[i][j] = 1;
								count++;
							}
						}
						//decisione di spostare la dama in alto verso destra
						if(dCheck(j)!=1 && board.getState(i, j-2)==-2 && i+1<8 && board.getState(i+1, j-1)==4){
							if(isSafe(i, j, 4) == true){
								possibleDestinations[i][j] = 1;
								count++;
							}
						}
						//decisione di spostare la dama in alto verso sinistra
						if(dCheck(j)!=2 && board.getState(i, j+2)==-2 && i+1<8 && board.getState(i+1, j+1)==4){
							if(isSafe(i, j, 4) == true){
								possibleDestinations[i][j] = 1;
								count++;
							}
						}
						//se entrambe le mosse sono unsafe, si tiene l'ultima
						coord[0] = i;
						coord[1] = j;
					}
			
			//se sono state trovate destinazioni possibili
			if(count > 0){
				
				//genero un numero casuale tra le possibili mosse safe
				Random r = new Random();
				numDest = r.nextInt(count)+1;
				
				//doppio ciclo che seleziona le coordinate della destinazione in ordine al numero casuale
				count = 0;
				for(int x=0; x<8; x++)
					for(int y=0; y<8; y++)
						if(possibleDestinations[x][y] == 1){		
							count++;					//contatore riprende a contare le possibili destinazioni
							if(count == numDest){		//quando il suo valore raggiunge quello casuale
								coord[0] = x;			//salvo le coordinate
								coord[1] = y;
							}
						}
			}
			
			return coord;
			
		}

}	

