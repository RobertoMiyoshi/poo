package br.pucrio.poo.models.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.pucrio.poo.models.BoardSpotsCalculations;
import br.pucrio.poo.models.Position;

public class Game {

	private static final int SPOTS_QUANTITY = 57;
	private static final int RELATIVE_INITIAL_SPOT = 0;
	
	private static Game instance;

	private List<Player> players;
	private int currentIndex;
	private int boardWidth;
	private int boardHeight;
	private BoardSpotsCalculations boardSpotsCalculations;	
	
	
	private Game(List<Player> players, final int boardWidth, final int boardHeight) {

		this.currentIndex = 0;
		this.players = players;
		this.boardHeight = boardHeight;
		this.boardWidth = boardWidth;
		this.boardSpotsCalculations = new BoardSpotsCalculations(boardWidth, boardHeight);
	}

	public Player currentPlayer() {
		return this.players.get(currentIndex);
	}

	public void endPlayerTurn() {
		currentIndex++;
		currentIndex %= players.size();
	}
	
	public static Game getInstance(final int boardWidth, final int boardHeight) throws Exception {
		if (instance == null) {
			// initializing models
			List<Player> players = Arrays.asList(
					new Player(PlayerColor.RED.toString(), PlayerColor.RED, SPOTS_QUANTITY),
					new Player(PlayerColor.GREEN.toString(), PlayerColor.GREEN, SPOTS_QUANTITY),
					new Player(PlayerColor.YELLOW.toString(), PlayerColor.YELLOW, SPOTS_QUANTITY),
					new Player(PlayerColor.BLUE.toString(), PlayerColor.BLUE, SPOTS_QUANTITY));		
			 
	    	instance = new Game(players, boardWidth, boardHeight);
		}
		return instance;
	}

	public List<Player> getPlayers() {
		return new ArrayList<Player>(players);
	}
	
	public int getBoardHeight(){
		return this.boardHeight;
	}
	
	public int getBoardWidth() {
		return this.boardWidth;
	}
	
	public boolean isInitialSpotBloqued() {
		
		int initialSpot = boardSpotsCalculations.getSpotNumberFromRelativeSpotNumber(RELATIVE_INITIAL_SPOT, currentPlayer().getColor());		
		return isSpotBloqued(initialSpot);
	}
	
	public boolean isSpotBloqued(int targetRelativeSpot) {
		int pinsAtTargetSpot = 0;
		
		int targetSpot = boardSpotsCalculations.getSpotNumberFromRelativeSpotNumber(targetRelativeSpot, currentPlayer().getColor());
		
		List<Player> players = getPlayers();
		for (Player player : players) {
			for (int relativeSpotNumber : player.getSpotNumbers()) {
				int spotNumber = boardSpotsCalculations.getSpotNumberFromRelativeSpotNumber(relativeSpotNumber, player.getColor());
				
				if(spotNumber == targetSpot) {
					pinsAtTargetSpot++;
					
					if(pinsAtTargetSpot > 1)
						return true;
				}
			}
		}	
		
		return false;
	}
	
	public Player otherPlayerBlocking(int targetRelativeSpot, Player playerEating) {
		
		int targetSpot = boardSpotsCalculations.getSpotNumberFromRelativeSpotNumber(targetRelativeSpot, currentPlayer().getColor());
		
		List<Player> players = getPlayers();
		for (Player player : players) {
			for (int relativeSpotNumber : player.getSpotNumbers()) {
				int spotNumber = boardSpotsCalculations.getSpotNumberFromRelativeSpotNumber(relativeSpotNumber, player.getColor());
				
				if(spotNumber == targetSpot && player!= playerEating) {
					return player;
				}
			}
		}	
		return null;
	}
	
	public Player getPlayerFromColor(PlayerColor color) {
		for (Player player : getPlayers()) {
			if(player.getColor() == color) 
				return player;
		}
		return null;
	}

	public boolean isPlayerTurn(PlayerColor color) {
		 return color == currentPlayer().getColor();
	}
	
	public boolean isHomeSpot(int spotNumber) {
		Player currentPlayer = this.currentPlayer();
		int relativeSpotNumber = boardSpotsCalculations.getRelativeSpotNumberFromSpotNumber(spotNumber, currentPlayer.getColor());
		return currentPlayer.isHomeSpot(relativeSpotNumber);
	}
	
	public boolean canLeaveHome() {
		Player currentPlayer = this.currentPlayer();
		
		if(!currentPlayer.canLeaveHome())
			return false;		
		
		if(isInitialSpotBloqued())
			return false;
		
		return true;
	}
	
	public void leaveHome() {
		this.currentPlayer().leaveHome();		
	}

	public boolean canMove() {		
		Player currentPlayer = this.currentPlayer();
		List<Integer> currentPlayerSpotNumbers = currentPlayer.getSpotNumbers();		
		
		for (int relativeSpotNumber : currentPlayerSpotNumbers) {
			int spotNumber = boardSpotsCalculations.getSpotNumberFromRelativeSpotNumber(relativeSpotNumber, currentPlayer.getColor());
			if (canMove(spotNumber)) 
				return true;			
		}				
		return false;
	}
	
	public boolean canPlayAgain() {
		return this.currentPlayer().canPlayAgain();
	}
	
	public boolean casaPreta (int targetRelativeSpot) {
		int targetSpot = boardSpotsCalculations.getSpotNumberFromRelativeSpotNumber(targetRelativeSpot, currentPlayer().getColor());

		if ((targetSpot == 10) || (targetSpot == 23) || (targetSpot == 36) ||(targetSpot == 49)) {
			return true;
		}
		
		return false;
	}
	
	public boolean casaInicial (int targetRelativeSpot) {
		int targetSpot = boardSpotsCalculations.getSpotNumberFromRelativeSpotNumber(targetRelativeSpot, currentPlayer().getColor());

		if ((targetSpot == 1) || (targetSpot == 14) || (targetSpot == 27) ||(targetSpot == 40)) {
			return true;
		}
		
		return false;
	}
	
	public boolean canMove(int spotNumber) {
		Player currentPlayer = this.currentPlayer();
		int relativeSpotNumber = boardSpotsCalculations.getRelativeSpotNumberFromSpotNumber(spotNumber, currentPlayer.getColor());
		int steps = currentPlayer.getDicePoints();
		
		if (!currentPlayer.canMove(relativeSpotNumber)) 
			return false;
		
		int targetRelativeSpot = relativeSpotNumber + steps;
		
		for (int path = relativeSpotNumber + 1; path < targetRelativeSpot +1 ; path++) {
			if(isSpotBloqued(path))
				return false;
		}
		
		
		if (casaPreta(targetRelativeSpot) && !isSpotBloqued(targetRelativeSpot))
			return true;		
		
		
		//implementar quando estiver na linha final e for a unica peça em jogo (ultima peça OU as outras 3 no inicio)
		return true;
	}
	
	public boolean canMove20(int spotNumber) {
		Player currentPlayer = this.currentPlayer();
		int relativeSpotNumber = boardSpotsCalculations.getRelativeSpotNumberFromSpotNumber(spotNumber, currentPlayer.getColor());
		int steps = 20;
		
		if (!currentPlayer.canMove(relativeSpotNumber)) 
			return false;
		
		int targetRelativeSpot = relativeSpotNumber + steps;
		
		for (int path = relativeSpotNumber + 1; path < targetRelativeSpot +1 ; path++) {
			if(isSpotBloqued(path))
				return false;
		}
		
		if (casaPreta(targetRelativeSpot) && !isSpotBloqued(targetRelativeSpot))
			return true;		
		
		//implementar quando estiver na linha final e for a unica peça em jogo (ultima peça OU as outras 3 no inicio)

		
		return true;
	}
	public void movePlayer(int spotNumber) {
		Player player = this.currentPlayer();
		int relativeSpotNumber = boardSpotsCalculations.getRelativeSpotNumberFromSpotNumber(spotNumber, player.getColor());
		player.goForward(relativeSpotNumber);
		
		
		int relativeSpotAfterSteps = player.getLastPinPlayed().getSpotNumber();
		int spotAfterSteps = boardSpotsCalculations.getSpotNumberFromRelativeSpotNumber(relativeSpotAfterSteps, player.getColor());
		
		if (isSpotBloqued(relativeSpotAfterSteps) && !casaPreta(relativeSpotAfterSteps) && !casaInicial(relativeSpotAfterSteps)) {
			Player playerEaten = otherPlayerBlocking(relativeSpotAfterSteps, player);
			int spotEaten = boardSpotsCalculations.getRelativeSpotNumberFromSpotNumber(spotAfterSteps, playerEaten.getColor());

			Pin pinEaten = playerEaten.getPinAtSpot(spotEaten);
			playerEaten.goToHome(pinEaten);	
			
			List<Pin> pinsPlayer = player.getPins();
			for (Pin pin : pinsPlayer) {
				int spotPin = boardSpotsCalculations.getSpotNumberFromRelativeSpotNumber(pin.getSpotNumber(), player.getColor());

				if (canMove20(spotPin)) {
					player.go20Forward(pin.getSpotNumber());
					return;
				}					
			}
		}
		return;
	}		
}


