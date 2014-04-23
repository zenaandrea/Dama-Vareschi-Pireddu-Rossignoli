package it.univr.dama.controller;

import it.univr.dama.model.Board;
import it.univr.dama.view.Viewer;

public class Move {
	
	private int x;
	private int y;
	private Board board;
	private int[][] allowedMoves;
	private boolean turn;
	private boolean aiMove = false;		//booleano che serve per segnalare all'I.A. se deve scegliere un pezzo da muovere (true) o una destinazione (false)
	
	public Move(int x, int y, Board board, int[][] allowedMoves, boolean turn) {
		this.x = x;
		this.y = y;
		this.board = board;
		this.allowedMoves = allowedMoves;
		this.turn = turn;
	}
	
	//funzione che gestisce effettivamente le mosse
	public Board chosenMoves() throws IllegalChoiceException{
		
		boolean toDo = false;			//variabile booleana (true=spostamento ; false=mangiata)
		boolean becomeKing = false;
		
		//doppio ciclo per raggiungere le coordinate del pulsante cliccato
		for(int i=0; i<8; i++)
			for (int j=0; j<8; j++){
				
				//CASO DELLA SCELTA DEL PEZZO DA SPOSTARE (tra i possibili)
				if(i==x && j==y && board.getState(i, j)>0){
					
					toDo = recognitionPiece(i, j);		//funzione che riconosce la casistica del pezzo scelto e torna il tipo di mossa
					
					updateMatrix(toDo);					//funzione che aggiorna la matrice allowedMoves
					
				}
				
				//CASO DELLA SCELTA DELLA DESTINAZIONE (selezione cella evidenziata)
				else if(i==x && j==y && board.getState(i, j)==-2){
					
					//spostamento e mangiata pedina e dama BIANCA
					if(turn==true){
						becomeKing = moveWhitePiece(i, j); 			//funzione che decreta la mossa del pezzo bianco
														   			//e ritorna true solo se la pedina può diventare dama
					}
					
					//spostamento e mangiata pedina e dama NERA
					if(turn==false){
						becomeKing = moveBlackPiece(i, j); 			//funzione che decreta la mossa del pezzo nero
						   								   			//e ritorna true solo se la pedina può diventare dama
					}
					
					aiMove = true;			//imposto a true la variabile per far capire all'I.A. che deve fare una mossa
											//nel caso in cui abbia appena concluso il bianco o che debba fare una multipla mangiata
					
					managementEndTurn(i, j, becomeKing); 		//funzione che gestisce la fine del turno,
																//controllando la possibilità di doppia mangiata	
				}

			}
		
		return board;
	}
	
	//funzione che riconosce la casistica del pezzo scelto e torna il tipo di mossa
	private boolean recognitionPiece(int i, int j) throws IllegalChoiceException{
		
		boolean toDo = false;
		
		//spostamento pedina bianca
		if(i==x && j==y && board.getState(i, j)==1 && allowedMoves[i][j]==1){
			if(j-1>=0 && board.getState(i-1, j-1)==0)
				board.setState(i-1, j-1, -2);
			if(j+1<8 && board.getState(i-1, j+1)==0)
				board.setState(i-1, j+1, -2);
			toDo = true;
		}
		
		//mangiata pedina bianca
		else if(i==x && j==y && board.getState(i, j)==1 && allowedMoves[i][j]==2){
			if(j-2>=0 && board.getState(i-1, j-1)==2 && board.getState(i-2, j-2)==0)
				board.setState(i-2, j-2, -2);
			if(j+2<8 && board.getState(i-1, j+1)==2 && board.getState(i-2, j+2)==0)
				board.setState(i-2, j+2, -2);
			toDo = false;
		}
		
		//spostamento pedina nera
		else if(i==x && j==y && board.getState(i, j)==2 && allowedMoves[i][j]==1){
			if(j-1>=0 && board.getState(i+1, j-1)==0)
				board.setState(i+1, j-1, -2);
			if(j+1<8 && board.getState(i+1, j+1)==0)
				board.setState(i+1, j+1, -2);
			toDo = true;
		}
		
		//mangiata pedina nera
		else if(i==x && j==y && board.getState(i, j)==2 && allowedMoves[i][j]==2){
			if(j-2>=0 && board.getState(i+1, j-1)==1 && board.getState(i+2, j-2)==0)
				board.setState(i+2, j-2, -2);
			if(j+2<8 && board.getState(i+1, j+1)==1 && board.getState(i+2, j+2)==0)
				board.setState(i+2, j+2, -2);
			toDo = false;
		}
		
		//spostamento dama bianca e nera
		else if(i==x && j==y && (board.getState(i, j)==3 || board.getState(i, j)==4) && allowedMoves[i][j]==1){
			if(i-1>=0 && j-1>=0 && board.getState(i-1, j-1)==0)
				board.setState(i-1, j-1, -2);
			if(i-1>=0 && j+1<8 && board.getState(i-1, j+1)==0)
				board.setState(i-1, j+1, -2);
			if(i+1<8 && j-1>=0 && board.getState(i+1, j-1)==0)
				board.setState(i+1, j-1, -2);
			if(i+1<8 && j+1<8 && board.getState(i+1, j+1)==0)
				board.setState(i+1, j+1, -2);
			toDo = true;
		}
		
		//mangiata dama bianca
		else if(i==x && j==y && board.getState(i, j)==3 && allowedMoves[i][j]==2){
			if(i-2>=0 && j-2>=0 && (board.getState(i-1, j-1)==2 || board.getState(i-1, j-1)==4) && board.getState(i-2, j-2)==0)
				board.setState(i-2, j-2, -2);
			if(i-2>=0 && j+2<8 && (board.getState(i-1, j+1)==2 || board.getState(i-1, j+1)==4) && board.getState(i-2, j+2)==0)
				board.setState(i-2, j+2, -2);
			if(i+2<8 && j-2>=0 && (board.getState(i+1, j-1)==2 || board.getState(i+1, j-1)==4) && board.getState(i+2, j-2)==0)
				board.setState(i+2, j-2, -2);
			if(i+2<8 && j+2<8 && (board.getState(i+1, j+1)==2 || board.getState(i+1, j+1)==4) && board.getState(i+2, j+2)==0)
				board.setState(i+2, j+2, -2);
			toDo = false;
		}
		
		//mangiata dama nera
		else if(i==x && j==y && board.getState(i, j)==4 && allowedMoves[i][j]==2){
			if(i-2>=0 && j-2>=0 && (board.getState(i-1, j-1)==1 || board.getState(i-1, j-1)==3) && board.getState(i-2, j-2)==0)
				board.setState(i-2, j-2, -2);
			if(i-2>=0 && j+2<8 && (board.getState(i-1, j+1)==1 || board.getState(i-1, j+1)==3) && board.getState(i-2, j+2)==0)
				board.setState(i-2, j+2, -2);
			if(i+2<8 && j-2>=0 && (board.getState(i+1, j-1)==1 || board.getState(i+1, j-1)==3) && board.getState(i+2, j-2)==0)
				board.setState(i+2, j-2, -2);
			if(i+2<8 && j+2<8 && (board.getState(i+1, j+1)==1 || board.getState(i+1, j+1)==3) && board.getState(i+2, j+2)==0)
				board.setState(i+2, j+2, -2);
			toDo = false;
		}
		
		//eccezione se si clicca su un bottone non valido
		else{
			throw new IllegalChoiceException("Scelta non valida");
		}
		
		return toDo;
	}
	
	//funzione che aggiorna la matrice allowedMoves
	private void updateMatrix(boolean toDo){
		
		//doppio ciclo per la "tabula rasa" di allowedMoves
		for(int a=0; a<8; a++)
			for (int b=0; b<8; b++)
				if(allowedMoves[a][b]>0)
					allowedMoves[a][b]=0;
		
		//teniamo salvata la posizione della pedina da spostare in allowedMoves
		if(toDo)
			allowedMoves[x][y] = 1;	// = 1 se spostamento
		else
			allowedMoves[x][y] = 2;	// = 2 se mangiata
	}
	
	//____________________________________________________________________________________________________________________________________
	
	//funzione che decreta la mossa del pezzo bianco e ritorna true solo se la pedina può diventare dama
	private boolean moveWhitePiece(int i, int j){
		
		boolean becomeKing = false;
		
		//cella evidenziata sinistra in su per spostamento
		if(i+1<8 && j+2<8 && allowedMoves[i+1][j+1]==1){
			if(board.getState(i+1, j+1)==1){			//controllo se è una pedina
				board.setState(i, j, 1);				//nella cella cliccata viene messa la pedina da spostare
				if(board.getState(i, j+2)==-2)			//caso in cui ho altre celle evidenziate da rimettere vuote
					board.setState(i, j+2, 0);
			}
			else if(board.getState(i+1, j+1)==3){		//controllo se è una dama
				board.setState(i, j, 3);				//nella cella cliccata viene messa la dama da spostare
				if(board.getState(i, j+2)==-2)			//casi in cui ho altre celle evidenziate da rimettere vuote
					board.setState(i, j+2, 0);
				if(i+2<8 && board.getState(i+2, j)==-2)
					board.setState(i+2, j, 0);
				if(i+2<8 && board.getState(i+2, j+2)==-2)
					board.setState(i+2, j+2, 0);
			}
			board.setState(i+1, j+1, 0);				//svuoto la cella di partenza
		}
		//cella evidenziata destra in su per spostamento
		else if(i+1<8 && j-2>=0 && allowedMoves[i+1][j-1]==1){
			if(board.getState(i+1, j-1)==1){			//controllo se è una pedina
				board.setState(i, j, 1);				//nella cella cliccata viene messa la pedina da spostare
				if(board.getState(i, j-2)==-2)			//caso in cui ho altre celle evidenziate da rimettere vuote
					board.setState(i, j-2, 0);
			}
			else if(board.getState(i+1, j-1)==3){		//controllo se è una dama
				board.setState(i, j, 3);				//nella cella cliccata viene messa la dama da spostare
				if(board.getState(i, j-2)==-2)			//casi in cui ho altre celle evidenziate da rimettere vuote
					board.setState(i, j-2, 0);
				if(i+2<8 && board.getState(i+2, j)==-2)
					board.setState(i+2, j, 0);
				if(i+2<8 && board.getState(i+2, j-2)==-2)
					board.setState(i+2, j-2, 0);
			}
			board.setState(i+1, j-1, 0);				//svuoto la cella di partenza
		}
		//cella evidenziata sinistra in giu per spostamento
		else if(i-1>=0 && j+2<8 && allowedMoves[i-1][j+1]==1){
			board.setState(i, j, 3);					//nella cella cliccata viene messa la dama da spostare
			if(board.getState(i, j+2)==-2)				//casi in cui ho altre celle evidenziate da rimettere vuote
				board.setState(i, j+2, 0);
			if(i-2>=0 && board.getState(i-2, j)==-2)
				board.setState(i-2, j, 0);
			if(i-2>=0 && board.getState(i-2, j+2)==-2)
				board.setState(i-2, j+2, 0);
			board.setState(i-1, j+1, 0);				//svuoto la cella di partenza
		}
		//cella evidenziata destra in giu per spostamento
		else if(i-1>=0 && j-2>=0 && allowedMoves[i-1][j-1]==1){
			board.setState(i, j, 3);					//nella cella cliccata viene messa la dama da spostare
			if(board.getState(i, j-2)==-2)				//casi in cui ho altre celle evidenziate da rimettere vuote
				board.setState(i, j-2, 0);
			if(i-2>=0 && board.getState(i-2, j)==-2)
				board.setState(i-2, j, 0);
			if(i-2>=0 && board.getState(i-2, j-2)==-2)
				board.setState(i-2, j-2, 0);
			board.setState(i-1, j-1, 0);				//svuoto la cella di partenza
		}
		
		//cella evidenziata sinistra in su per mangiata
		else if(i+2<8 && j+4<8 && allowedMoves[i+2][j+2]==2 && (board.getState(i+1, j+1)==2 || board.getState(i+2, j+2)==3 && board.getState(i+1, j+1)==4)){
			if(board.getState(i+2, j+2)==1){			//controllo se è una pedina
				board.setState(i, j, 1);				//nella cella cliccata viene messa la pedina da spostare
				if(board.getState(i, j+4)==-2)			//caso in cui ho altre celle evidenziate da rimettere vuote
					board.setState(i, j+4, 0);
			}
			else if(board.getState(i+2, j+2)==3){		//controllo se è una dama
				board.setState(i, j, 3);				//nella cella cliccata viene messa la dama da spostare
				if(board.getState(i, j+4)==-2)			//casi in cui ho altre celle evidenziate da rimettere vuote
					board.setState(i, j+4, 0);
				if(i+4<8 && board.getState(i+4, j)==-2)
					board.setState(i+4, j, 0);
				if(i+4<8 && board.getState(i+4, j+4)==-2)
					board.setState(i+4, j+4, 0);
			}
			board.setState(i+2, j+2, 0);				//svuoto la cella di partenza
			board.setState(i+1, j+1, 0);				//svuoto la cella della pedina mangiata
		}
		//cella evidenziata destra in su per mangiata
		else if(i+2<8 && j-4>=0 && allowedMoves[i+2][j-2]==2 && (board.getState(i+1, j-1)==2 || board.getState(i+2, j-2)==3 && board.getState(i+1, j-1)==4)){
			if(board.getState(i+2, j-2)==1){			//controllo se è una pedina
				board.setState(i, j, 1);				//nella cella cliccata viene messa la pedina da spostare
				if(board.getState(i, j-4)==-2)			//caso in cui ho altre celle evidenziate da rimettere vuote
					board.setState(i, j-4, 0);
			}
			else if(board.getState(i+2, j-2)==3){		//controllo se è una dama
				board.setState(i, j, 3);				//nella cella cliccata viene messa la dama da spostare
				if(board.getState(i, j-4)==-2)			//casi in cui ho altre celle evidenziate da rimettere vuote
					board.setState(i, j-4, 0);
				if(i+4<8 && board.getState(i+4, j)==-2)
					board.setState(i+4, j, 0);
				if(i+4<8 && board.getState(i+4, j-4)==-2)
					board.setState(i+4, j-4, 0);
			}
			board.setState(i+2, j-2, 0);				//svuoto la cella di partenza
			board.setState(i+1, j-1, 0);				//svuoto la cella della pedina mangiata
		}
		//cella evidenziata sinistra in giu per mangiata
		else if(i-2>=0 && j+4<8 && allowedMoves[i-2][j+2]==2 && (board.getState(i-1, j+1)==2 || board.getState(i-1, j+1)==4)){
			board.setState(i, j, 3);					//nella cella cliccata viene messa la dama da spostare
			if(board.getState(i, j+4)==-2)				//casi in cui ho altre celle evidenziate da rimettere vuote
				board.setState(i, j+4, 0);
			if(i-4>=0 && board.getState(i-4, j)==-2)
				board.setState(i-4, j, 0);
			if(i-4>=0 && board.getState(i-4, j+4)==-2)
				board.setState(i-4, j+4, 0);
			board.setState(i-2, j+2, 0);				//svuoto la cella di partenza
			board.setState(i-1, j+1, 0);				//svuoto la cella della pedina mangiata
		}
		//cella evidenziata destra in giu per mangiata
		else if(i-2>=0 && j-4>=0 && allowedMoves[i-2][j-2]==2 && (board.getState(i-1, j-1)==2 || board.getState(i-1, j-1)==4)){
			board.setState(i, j, 3);					//nella cella cliccata viene messa la dama da spostare
			if(board.getState(i, j-4)==-2)				//casi in cui ho altre celle evidenziate da rimettere vuote
				board.setState(i, j-4, 0);
			if(i-4>=0 && board.getState(i-4, j)==-2)
				board.setState(i-4, j, 0);
			if(i-4>=0 && board.getState(i-4, j-4)==-2)
				board.setState(i-4, j-4, 0);
			board.setState(i-2, j-2, 0);				//svuoto la cella di partenza
			board.setState(i-1, j-1, 0);				//svuoto la cella della pedina mangiata
		}
		
		//cella evidenziata sinistra in su per spostamento, vicino bordo destro
		else if(i+1<8 && j+2>=8 && allowedMoves[i+1][j+1]==1){
			if(board.getState(i+1, j+1)==1)				//controllo se è una pedina
				board.setState(i, j, 1);				//nella cella cliccata viene messa la pedina da spostare
			else if(board.getState(i+1, j+1)==3){		//controllo se è una dama
				board.setState(i, j, 3);				//nella cella cliccata viene messa la dama da spostare
				if(i+2<8 && board.getState(i+2, j)==-2)			//caso in cui ho altre celle evidenziate da rimettere vuote
					board.setState(i+2, j, 0);
			}
			board.setState(i+1, j+1, 0);				//svuoto la cella di partenza
		}
		//cella evidenziata destra in su per spostamento, vicino bordo sinistro
		else if(i+1<8 && j-2<0 && allowedMoves[i+1][j-1]==1){
			if(board.getState(i+1, j-1)==1)				//controllo se è una pedina
				board.setState(i, j, 1);				//nella cella cliccata viene messa la pedina da spostare
			else if(board.getState(i+1, j-1)==3){		//controllo se è una dama
				board.setState(i, j, 3);				//nella cella cliccata viene messa la dama da spostare
				if(i+2<8 && board.getState(i+2, j)==-2)			//caso in cui ho altre celle evidenziate da rimettere vuote
					board.setState(i+2, j, 0);
			}
			board.setState(i+1, j-1, 0);				//svuoto la cella di partenza
		}
		//cella evidenziata sinistra in giu per spostamento, vicino bordo destro
		else if(i-1>=0 && j+2>=8 && board.getState(i-1, j+1)==3){
			board.setState(i, j, 3);					//nella cella cliccata viene messa la dama da spostare
			if(i-2>=0 && board.getState(i-2, j)==-2)	//caso in cui ho altre celle evidenziate da rimettere vuote
				board.setState(i-2, j, 0);
			board.setState(i-1, j+1, 0);				//svuoto la cella di partenza
		}
		//cella evidenziata destra in giu per spostamento, vicino bordo sinistro
		else if(i-1>=0 && j-2<0 && board.getState(i-1, j-1)==3){
			board.setState(i, j, 3);					//nella cella cliccata viene messa la dama da spostare
			if(i-2>=0 && board.getState(i-2, j)==-2)	//caso in cui ho altre celle evidenziate da rimettere vuote
				board.setState(i-2, j, 0);
			board.setState(i-1, j-1, 0);				//svuoto la cella di partenza
		}
		
		//cella evidenziata sinistra in su per mangiata, vicino bordo destro
		else if(i+2<8 && j+4>=8 && allowedMoves[i+2][j+2]==2 && (board.getState(i+1, j+1)==2 || board.getState(i+2, j+2)==3 && board.getState(i+1, j+1)==4)){
			if(board.getState(i+2, j+2)==1)				//controllo se è una pedina
				board.setState(i, j, 1);				//nella cella cliccata viene messa la pedina da spostare
			else if(board.getState(i+2, j+2)==3){		//controllo se è una dama
				board.setState(i, j, 3);				//nella cella cliccata viene messa la dama da spostare
				if(i+4<8 && board.getState(i+4, j)==-2)	//caso in cui ho altre celle evidenziate da rimettere vuote
					board.setState(i+4, j, 0);
			}
			board.setState(i+2, j+2, 0);				//svuoto la cella di partenza
			board.setState(i+1, j+1, 0);				//svuoto la cella della pedina mangiata
		}
		//cella evidenziata destra in su per mangiata, vicino bordo sinistro
		else if(i+2<8 && j-4<0 && allowedMoves[i+2][j-2]==2 && (board.getState(i+1, j-1)==2 || board.getState(i+2, j-2)==3 && board.getState(i+1, j-1)==4)){
			if(board.getState(i+2, j-2)==1)				//controllo se è una pedina
				board.setState(i, j, 1);				//nella cella cliccata viene messa la pedina da spostare
			else if(board.getState(i+2, j-2)==3){		//controllo se è una dama
				board.setState(i, j, 3);				//nella cella cliccata viene messa la dama da spostare
				if(i+4<8 && board.getState(i+4, j)==-2)	//caso in cui ho altre celle evidenziate da rimettere vuote
					board.setState(i+4, j, 0);
			}
			board.setState(i+2, j-2, 0);				//svuoto la cella di partenza
			board.setState(i+1, j-1, 0);				//svuoto la cella della pedina mangiata
		}
		//cella evidenziata sinistra in giu per mangiata, vicino bordo destro
		else if(i-2>=0 && j+4>=8 && allowedMoves[i-2][j+2]==2 && (board.getState(i-1, j+1)==2 || board.getState(i-1, j+1)==4)){
			board.setState(i, j, 3);					//nella cella cliccata viene messa la dama da spostare
			if(i-4>=0 && board.getState(i-4, j)==-2)	//caso in cui ho altre celle evidenziate da rimettere vuote
				board.setState(i-4, j, 0);
			board.setState(i-2, j+2, 0);				//svuoto la cella di partenza
			board.setState(i-1, j+1, 0);				//svuoto la cella della pedina mangiata
		}
		//cella evidenziata destra in giu per mangiata, vicino bordo sinistro
		else if(i-2>=0 && j-4<0 && allowedMoves[i-2][j-2]==2 && (board.getState(i-1, j-1)==2 || board.getState(i-1, j-1)==4)){
			board.setState(i, j, 3);					//nella cella cliccata viene messa la dama da spostare
			if(i-4>=0 && board.getState(i-4, j)==-2)	//caso in cui ho altre celle evidenziate da rimettere vuote
				board.setState(i-4, j, 0);
			board.setState(i-2, j-2, 0);				//svuoto la cella di partenza
			board.setState(i-1, j-1, 0);				//svuoto la cella della pedina mangiata
		}
		
		//diventa dama bianca
		if(board.getState(i,j)==1 && i==0)
		{
			board.setState(i, j, 3);
			becomeKing = true;
		}
		
		return becomeKing;
	}
	
	//funzione che decreta la mossa del pezzo nero e ritorna true solo se la pedina può diventare dama
	private boolean moveBlackPiece(int i, int j){
		
		boolean becomeKing = false;
		
		//cella evidenziata sinistra in giu per spostamento
		if(i-1>=0 && j+2<8 && allowedMoves[i-1][j+1]==1){
			if(board.getState(i-1, j+1)==2){			//controllo se è una pedina
				board.setState(i, j, 2);				//nella cella cliccata viene messa la pedina da spostare
				if(board.getState(i, j+2)==-2)			//caso in cui ho altre celle evidenziate da rimettere vuote
					board.setState(i, j+2, 0);
			}
			else if(board.getState(i-1, j+1)==4){		//controllo se è una dama
				board.setState(i, j, 4);				//nella cella cliccata viene messa la dama da spostare
				if(board.getState(i, j+2)==-2)			//casi in cui ho altre celle evidenziate da rimettere vuote
					board.setState(i, j+2, 0);
				if(i-2>=0 && board.getState(i-2, j)==-2)
					board.setState(i-2, j, 0);
				if(i-2>=0 && board.getState(i-2, j+2)==-2)
					board.setState(i-2, j+2, 0);
			}
			board.setState(i-1, j+1, 0);				//svuoto la cella di partenza
		}
		//cella evidenziata destra in giu per spostamento
		else if(i-1>=0 && j-2>=0 && allowedMoves[i-1][j-1]==1){
			if(board.getState(i-1, j-1)==2){			//controllo se è una pedina
				board.setState(i, j, 2);				//nella cella cliccata viene messa la pedina da spostare
				if(board.getState(i, j-2)==-2)			//caso in cui ho altre celle evidenziate da rimettere vuote
					board.setState(i, j-2, 0);
			}
			else if(board.getState(i-1, j-1)==4){
				board.setState(i, j, 4);				//nella cella cliccata viene messa la dama da spostare
				if(board.getState(i, j-2)==-2)			//casi in cui ho altre celle evidenziate da rimettere vuote
					board.setState(i, j-2, 0);
				if(i-2>=0 && board.getState(i-2, j)==-2)
					board.setState(i-2, j, 0);
				if(i-2>=0 && board.getState(i-2, j-2)==-2)
					board.setState(i-2, j-2, 0);
			}
			board.setState(i-1, j-1, 0);				//svuoto la cella di partenza
		}
		//cella evidenziata sinistra in su per spostamento	
		else if(i+1<8 && j+2<8 && allowedMoves[i+1][j+1]==1){
			board.setState(i, j, 4);					//nella cella cliccata viene messa la dama da spostare
			if(board.getState(i, j+2)==-2)				//casi in cui ho altre celle evidenziate da rimettere vuote
				board.setState(i, j+2, 0);
			if(i+2<8 && board.getState(i+2, j)==-2)
				board.setState(i+2, j, 0);
			if(i+2<8 && board.getState(i+2, j+2)==-2)
				board.setState(i+2, j+2, 0);
			board.setState(i+1, j+1, 0);				//svuoto la cella di partenza
		}
		//cella evidenziata destra in su per spostamento
		else if(i+1<8 && j-2>=0 && allowedMoves[i+1][j-1]==1){
			board.setState(i, j, 4);					//nella cella cliccata viene messa la dama da spostare
			if(board.getState(i, j-2)==-2)				//casi in cui ho altre celle evidenziate da rimettere vuote
				board.setState(i, j-2, 0);
			if(i+2<8 && board.getState(i+2, j)==-2)
				board.setState(i+2, j, 0);
			if(i+2<8 && board.getState(i+2, j-2)==-2)
				board.setState(i+2, j-2, 0);
			board.setState(i+1, j-1, 0);				//svuoto la cella di partenza
		}
		
		//cella evidenziata sinistra in giu per mangiata
		else if(i-2>=0 && j+4<8 && allowedMoves[i-2][j+2]==2 && (board.getState(i-1, j+1)==1 || board.getState(i-2, j+2)==4 && board.getState(i-1, j+1)==3)){
			if(board.getState(i-2, j+2)==2){			//controllo se è una pedina
				board.setState(i, j, 2);				//nella cella cliccata viene messa la pedina da spostare
				if(board.getState(i, j+4)==-2)			//caso in cui ho altre celle evidenziate da rimettere vuote
					board.setState(i, j+4, 0);
			}
			else if(board.getState(i-2, j+2)==4){		//controllo se è una dama
				board.setState(i, j, 4);				//nella cella cliccata viene messa la dama da spostare
				if(board.getState(i, j+4)==-2)			//casi in cui ho altre celle evidenziate da rimettere vuote
					board.setState(i, j+4, 0);
				if(i-4>=0 && board.getState(i-4, j)==-2)
					board.setState(i-4, j, 0);
				if(i-4>=0 && board.getState(i-4, j+4)==-2)
					board.setState(i-4, j+4, 0);
			}
			board.setState(i-2, j+2, 0);				//svuoto la cella di partenza
			board.setState(i-1, j+1, 0);				//svuoto la cella della pedina mangiata
		}
		//cella evidenziata destra in giu per mangiata
		else if(i-2>=0 && j-4>=0 && allowedMoves[i-2][j-2]==2 && (board.getState(i-1, j-1)==1 || board.getState(i-2, j-2)==4 && board.getState(i-1, j-1)==3)){
			if(board.getState(i-2, j-2)==2){			//controllo se è una pedina
				board.setState(i, j, 2);				//nella cella cliccata viene messa la pedina da spostare
				if(board.getState(i, j-4)==-2)			//caso in cui ho altre celle evidenziate da rimettere vuote
					board.setState(i, j-4, 0);
			}
			else if(board.getState(i-2, j-2)==4){		//controllo se è una dama
				board.setState(i, j, 4);				//nella cella cliccata viene messa la dama da spostare
				if(board.getState(i, j-4)==-2)			//casi in cui ho altre celle evidenziate da rimettere vuote
					board.setState(i, j-4, 0);
				if(i-4>=0 && board.getState(i-4, j)==-2)
					board.setState(i-4, j, 0);
				if(i-4>=0 && board.getState(i-4, j-4)==-2)
					board.setState(i-4, j-4, 0);
			}
			board.setState(i-2, j-2, 0);				//svuoto la cella di partenza
			board.setState(i-1, j-1, 0);				//svuoto la cella della pedina mangiata
		}
		//cella evidenziata sinistra in su per mangiata
		else if(i+2<8 && j+4<8 && allowedMoves[i+2][j+2]==2 && (board.getState(i+1, j+1)==1 || board.getState(i+1, j+1)==3)){
			board.setState(i, j, 4);					//nella cella cliccata viene messa la dama da spostare
			if(board.getState(i, j+4)==-2)				//casi in cui ho altre celle evidenziate da rimettere vuote
				board.setState(i, j+4, 0);
			if(i+4<8 && board.getState(i+4, j)==-2)
				board.setState(i+4, j, 0);
			if(i+4<8 && board.getState(i+4, j+4)==-2)
				board.setState(i+4, j+4, 0);
			board.setState(i+2, j+2, 0);				//svuoto la cella di partenza
			board.setState(i+1, j+1, 0);				//svuoto la cella della pedina mangiata
		}
		//cella evidenziata destra in su per mangiata
		else if(i+2<8 && j-4>=0 && allowedMoves[i+2][j-2]==2 && (board.getState(i+1, j-1)==1 || board.getState(i+1, j-1)==3)){
			board.setState(i, j, 4);					//nella cella cliccata viene messa la dama da spostare
			if(board.getState(i, j-4)==-2)				//casi in cui ho altre celle evidenziate da rimettere vuote
				board.setState(i, j-4, 0);
			if(i+4<8 && board.getState(i+4, j)==-2)
				board.setState(i+4, j, 0);
			if(i+4<8 && board.getState(i+4, j-4)==-2)
				board.setState(i+4, j-4, 0);
			board.setState(i+2, j-2, 0);				//svuoto la cella di partenza
			board.setState(i+1, j-1, 0);				//svuoto la cella della pedina mangiata
		}
		
		//cella evidenziata sinistra in giu per spostamento, vicino bordo destro
		else if(i-1>=0 && j+2>=8 && allowedMoves[i-1][j+1]==1){
			if(board.getState(i-1, j+1)==2)				//controllo se è una pedina
				board.setState(i, j, 2);				//nella cella cliccata viene messa la pedina da spostare
			else if(board.getState(i-1, j+1)==4){		//controllo se è una dama
				board.setState(i, j, 4);				//nella cella cliccata viene messa la dama da spostare
				if(i-2>=0 && board.getState(i-2, j)==-2)//caso in cui ho altre celle evidenziate da rimettere vuote
					board.setState(i-2, j, 0);
			}
			board.setState(i-1, j+1, 0);				//svuoto la cella di partenza
		}
		//cella evidenziata destra in giu per spostamento, vicino bordo sinistro
		else if(i-1>=0 && j-2<0 && allowedMoves[i-1][j-1]==1){
			if(board.getState(i-1, j-1)==2)				//controllo se è una pedina
				board.setState(i, j, 2);				//nella cella cliccata viene messa la pedina da spostare
			else if(board.getState(i-1, j-1)==4){		//controllo se è una dama
				board.setState(i, j, 4);				//nella cella cliccata viene messa la dama da spostare
				if(i-2>=0 && board.getState(i-2, j)==-2)//caso in cui ho altre celle evidenziate da rimettere vuote
					board.setState(i-2, j, 0);
			}
			board.setState(i-1, j-1, 0);				//svuoto la cella di partenza
		}
		//cella evidenziata sinistra in su per spostamento, vicino bordo destro
		else if(i+1<8 && j+2>=8 && board.getState(i+1, j+1)==4){
			board.setState(i, j, 4);					//nella cella cliccata viene messa la dama da spostare
			if(i+2<8 && board.getState(i+2, j)==-2)		//caso in cui ho altre celle evidenziate da rimettere vuote
				board.setState(i+2, j, 0);
			board.setState(i+1, j+1, 0);				//svuoto la cella di partenza
		}
		//cella evidenziata destra in su per spostamento, vicino bordo sinistro
		else if(i+1<8 && j-2<0 && board.getState(i+1, j-1)==4){
			board.setState(i, j, 4);					//nella cella cliccata viene messa la dama da spostare
			if(i+2<8 && board.getState(i+2, j)==-2)		//caso in cui ho altre celle evidenziate da rimettere vuote
				board.setState(i+2, j, 0);
			board.setState(i+1, j-1, 0);				//svuoto la cella di partenza
		}
		
		//cella evidenziata sinistra in giu per mangiata, vicino bordo destro
		else if(i-2>=0 && j+4>=8 && allowedMoves[i-2][j+2]==2 && (board.getState(i-1, j+1)==1 || board.getState(i-2, j+2)==4 && board.getState(i-1, j+1)==3)){
			if(board.getState(i-2, j+2)==2)				//controllo se è una pedina
				board.setState(i, j, 2);				//nella cella cliccata viene messa la pedina da spostare
			else if(board.getState(i-2, j+2)==4){		//controllo se è una dama
				board.setState(i, j, 4);				//nella cella cliccata viene messa la dama da spostare
				if(i-4>=0 && board.getState(i-4, j)==-2)//caso in cui ho altre celle evidenziate da rimettere vuote
					board.setState(i-4, j, 0);
			}
			board.setState(i-2, j+2, 0);				//svuoto la cella di partenza
			board.setState(i-1, j+1, 0);				//svuoto la cella della pedina mangiata
		}
		//cella evidenziata destra in giu per mangiata, vicino bordo sinistro
		else if(i-2>=0 && j-4<0 && allowedMoves[i-2][j-2]==2 && (board.getState(i-1, j-1)==1 || board.getState(i-2, j-2)==4 && board.getState(i-1, j-1)==3)){
			if(board.getState(i-2, j-2)==2)				//controllo se è una pedina
				board.setState(i, j, 2);				//nella cella cliccata viene messa la pedina da spostare
			else if(board.getState(i-2, j-2)==4){		//controllo se è una dama
				board.setState(i, j, 4);				//nella cella cliccata viene messa la dama da spostare
				if(i-4>=0 && board.getState(i-4, j)==-2)//caso in cui ho altre celle evidenziate da rimettere vuote
					board.setState(i-4, j, 0);
			}
			board.setState(i-2, j-2, 0);				//svuoto la cella di partenza
			board.setState(i-1, j-1, 0);				//svuoto la cella della pedina mangiata
		}
		//cella evidenziata sinistra in su per mangiata, vicino bordo destro
		else if(i+2<8 && j+4>=8 && allowedMoves[i+2][j+2]==2 && (board.getState(i+1, j+1)==1 || board.getState(i+1, j+1)==3)){
			board.setState(i, j, 4);					//nella cella cliccata viene messa la dama da spostare
			if(i+4<8 && board.getState(i+4, j)==-2)		//caso in cui ho altre celle evidenziate da rimettere vuote
				board.setState(i+4, j, 0);
			board.setState(i+2, j+2, 0);				//svuoto la cella di partenza
			board.setState(i+1, j+1, 0);				//svuoto la cella della pedina mangiata
		}
		//cella evidenziata destra in su per mangiata, vicino bordo sinistro
		else if(i+2<8 && j-4<0 && allowedMoves[i+2][j-2]==2 && (board.getState(i+1, j-1)==1 || board.getState(i+1, j-1)==3)){
			board.setState(i, j, 4);					//nella cella cliccata viene messa la dama da spostare
			if(i+4<8 && board.getState(i+4, j)==-2)		//caso in cui ho altre celle evidenziate da rimettere vuote
				board.setState(i+4, j, 0);
			board.setState(i+2, j-2, 0);				//svuoto la cella di partenza
			board.setState(i+1, j-1, 0);				//svuoto la cella della pedina mangiata
		}
		
		//diventa dama nera
		if(board.getState(i,j)==2 && i==7)
		{
			board.setState(i, j, 4);
			becomeKing = true;
		}
		
		return becomeKing;
	}
	
	//funzione che gestisce la fine del turno, controllando la possibilità di doppia mangiata
	private void managementEndTurn(int i, int j, boolean becomeKing) throws IllegalChoiceException{
		
		//ciclo che verifica se è stato fatto uno spostamento (=1) o una mangiata (=2)
		int indexMove=0;
		for(int a=0; a<8 && indexMove==0; a++)
			for(int b=0; b<8 && indexMove==0; b++)
				if(allowedMoves[a][b]>indexMove)
					indexMove=allowedMoves[a][b];
		
		//se è appena stato fatto uno spostamento
		//oppure
		//se è appena stata fatta una mangiata da una pedina, ma la pedina è diventata dama
		if(indexMove==1 || becomeKing==true){
			turn=!turn;		//cambio di turno
			
			//ricalcolo della matrice delle mosse prioritarie per il prossimo turno
			Choose choose = new Choose(turn, board);
			allowedMoves = choose.allowedMoves();
		}
		
		//se è appena stata fatta una mangiata
		else if(indexMove==2){
			
			//ricalcolo della matrice delle mosse prioritarie per lo stesso turno
			Choose choose = new Choose(turn, board);
			allowedMoves = choose.allowedMoves();
			
			//se posso fare un'altra mangiata con lo stesso pezzo
			if(allowedMoves[i][j] == 2)
				if (turn==true)
					rePrint();		//ristampo la damiera con la possibilità di un'ulteriore mangiata
				else{
					rePrint();		//ristampo la damiera con la possibilità di un'ulteriore mangiata
					turn = true;	//forzo il turno bianco perchè l'I.A (nero) ha finito la multipla mangiata
					
					//ricalcolo della matrice delle mosse prioritarie per il turno bianco
					choose = new Choose(turn, board);
					allowedMoves = choose.allowedMoves();
				}
			//se invece non posso più mangiare con lo stesso pezzo
			else{
				turn=!turn;		//cambio di turno
				
				//ricalcolo della matrice delle mosse prioritarie per il prossimo turno
				choose = new Choose(turn, board);
				allowedMoves = choose.allowedMoves();
			}
		}
	}
	
	//funzione che "ristampa" la nuova situazione della damiera
	public void rePrint() throws IllegalChoiceException{
		
		this.board = chosenMoves();		//funzione che gestisce effettivamente le mosse
		
		//ristampa effettiva tramite la classe Viewer
		Viewer viewer = new Viewer(board, allowedMoves, turn);
		viewer.showBoard();
		
		//ciclo che verifica se è stato fatto uno spostamento (=1) o una mangiata (=2)
		int indexMove=0;
		for(int a=0; a<8 && indexMove==0; a++)
			for(int b=0; b<8 && indexMove==0; b++)
				if(allowedMoves[a][b]>indexMove)
					indexMove=allowedMoves[a][b];
		
		//chiamata dell'I.A. nel caso in cui tocchi i neri e siamo all'inizio del turno (CASO DELLA SCELTA DEL PEZZO DA SPOSTARE)
		if(turn==false && indexMove>0){
			ArtificialIntelligence ai = new ArtificialIntelligence(board, allowedMoves, turn, aiMove);
			ai.choiceAi();
		}
	}
}
