import java.util.*;
import java.net.*;
import java.io.*;
import javax.swing.*;

/**
 * Class that implements the CardGame interface and
 * NetworkGame interface.
 * <p>
 * Used to model a BigTwo card game that supports 4
 * players playing over the internet.
 * 
 * @author Ajayveer Singh
 *
 */
public class BigTwoClient implements CardGame, NetworkGame {
	
	private int numOfPlayers;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int playerID;
	private String playerName;
	private String serverIP;
	private int serverPort;
	private Socket sock;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private int currentIdx;
	private BigTwoTable table;
	private boolean gameEndedNicely;
	
	private boolean gameStarted = false;
	
	/**
	 * Constructor for creating a Big Two client.
	 */
	public BigTwoClient() {
		gameEndedNicely = false;
		
		numOfPlayers = 4;
		currentIdx = -1;
		
		playerList = new ArrayList<CardGamePlayer>();
		for (int i = 0; i < 4; i++) {
			playerList.add(new CardGamePlayer());
		}
		
		playerID = -1;
		playerName = (String) JOptionPane.showInputDialog("Enter your name: ");
		if(playerName == null || playerName.trim().isEmpty()) {
			playerName = "Guest";
		}
		
		serverIP = "127.0.0.1";
		serverPort = 2396;
		
		table = new BigTwoTable(this);
		table.disable();
		
		handsOnTable = new ArrayList<Hand>();
		
		makeConnection();
		table.repaint();
	}
	
	/**
	 * Getter method for instance variable gameEndedNicely.
	 * 
	 * @return (boolean) gameEndedNicely
	 */
	public boolean getGameEndedNicely() {
		return gameEndedNicely;
	}
	
	/**
	 * A getter method for the number of players.
	 * 
	 * @return int value specifying the number of
	 * 		   players.
	 */
	public int getNumOfPlayers() {
		return numOfPlayers;
	}
	
	/**
	 * Getter method for instance variable Deck deck.
	 * 
	 * @return Deck containing the deck of cards (BigTwoCards).
	 */
	public Deck getDeck() {
		return deck;
	}
	
	/**
	 * Getter method for the instance variable 'playerList'.
	 * 
	 * @return ArrayList of type CardGamePlayer, which represents the list of players.
	 */
	public ArrayList<CardGamePlayer> getPlayerList() {
		return playerList;
	}
	
	/**
	 * Getter method for instance variable 'handsOnTable'.
	 * 
	 * @return ArrayList of type Hand which represents all the hands (Hand objects) that have been played so far.
	 */
	public ArrayList<Hand> getHandsOnTable() {
		return handsOnTable;
	}
	
	/**
	 * Getter method for instance variable 'currentIdx'.
	 * 
	 * @return int value representing the index of the current player.
	 */
	public int getCurrentIdx() {
		return currentIdx;
	}
	
	/**
	 * Method for starting/restarting the game with 
	 * the provided shuffled deck of cards.
	 * 
	 * @param deck (Deck) Deck of cards to be used
	 * 				in the game.
	 */
	public void start(Deck deck) {
		gameEndedNicely = false;
		gameStarted = true;
		deck = (BigTwoDeck) deck;
		
		// Remove cards from Player, and Table
		for(CardGamePlayer player : playerList) player.removeAllCards();
		handsOnTable.removeAll(handsOnTable);
		
		// Distributing cards, sorting them, and finding who the first player is.
		for(int player = 0; player < 4; player++) {
			for(int card = 0; card < 13; card++) {
				playerList.get(player).addCard(deck.getCard(13 * player + card));
				if (deck.getCard(13 * player + card).compareTo(new Card(0,2)) == 0) {
					currentIdx = player;
					
				}
			}
			playerList.get(player).getCardsInHand().sort();
		}
		
		// Update activePlayer in BigTwoTable. currentIdx is already updated.
		table.setActivePlayer(playerID);
		
		//table.printMsg(playerList.get(currentIdx).getName() + "'s turn:");
		table.repaint();
	}
	
	/**
	 * Method used by a player to make a move.
	 * 
	 * @param playerID (int) Specifies ID of player making the move
	 * @param cardIdx (int[]) Specifies the list of card indices (from the player's hand)
	 * 					that the player wishes to use in their move.
	 */
	public void makeMove(int playerID, int[] cardIdx) {
		CardGameMessage move = new CardGameMessage(CardGameMessage.MOVE, -1, cardIdx);
		sendMessage(move);
	}
	
	/**
	 * Method for checking a move made by a player.
	 * 
	 * @param playerID (int) The index of player making the move
	 * @param cardIdx (int[]) The indices of cards that the player selected.
	 */
	public void checkMove(int playerID, int[] cardIdx) {
		
		boolean illegalMove = false;
		if(this.playerID == currentIdx) table.enable();
		else table.disable();
		table.repaint();
		
		int[] selectedCardIndices = cardIdx;
		
		// Pass
		// If either the first player tries to pass in the beginning, or if the last player who played tries to pass, it is an Invalid move.
		if (selectedCardIndices == null || cardIdx == null) {
			// If it is a Pass
			// Meaning of Condition: (not first player) && (not last player who played a hand)
			if (!handsOnTable.isEmpty() && handsOnTable.get(handsOnTable.size() - 1).getPlayer() != playerList.get(currentIdx)) {
				
				illegalMove = false;
				
				table.printMsg("{Pass}");
				
				currentIdx = (currentIdx + 1 >= 4) ? 0 : currentIdx + 1;
				if(currentIdx == this.playerID) table.enable();
				else table.disable();
				table.printMsg("");
				table.printMsg(playerList.get(currentIdx).getName() + "'s turn:");
				
				return;

			} else {
				// Illegal move: First player cannot pass his turn, neither can last player who played (cycled back)
				illegalMove = true;
				table.printMsg("Illegal Move! 1");
				return;
			}
		}
		
		CardList selectedCards = playerList.get(currentIdx).play(selectedCardIndices);
		Hand bestHandPossible = composeHand(playerList.get(currentIdx), selectedCards);
		
		// Illegal Move: Doesn't make a valid Hand
		if (bestHandPossible == null) {
			illegalMove = true;
			
			table.printMsg("Illegal Move! 2");
			return;
		}
		
		// Illegal Move: First Player not including 0,2 card
		if (handsOnTable.isEmpty() && !bestHandPossible.contains(new Card(0,2))) {
			illegalMove = true;
			
			table.printMsg("Illegal Move! 3");
			return;
		}
		
		// Hand with different number of cards from the last played, or does not beat
		if (!handsOnTable.isEmpty() && !bestHandPossible.beats(handsOnTable.get(handsOnTable.size() - 1))) {
			// currentIdx is not the last one to have played a hand (it has not cycled back)
			if (handsOnTable.get(handsOnTable.size() - 1).getPlayer() != playerList.get(currentIdx)) {
				illegalMove = true;
				
				table.printMsg("Illegal Move! 4");
				return;
			}
		}
		
		if(illegalMove) {
			table.printMsg("Illegal Move! 5");
			return;
		}
		
		table.printMsg("{" + bestHandPossible.getType() + "} " + bestHandPossible);
		
		// Remove Hand from cardsInHand
		playerList.get(currentIdx).removeCards(selectedCards);
		
		// Add to handsOnTable
		handsOnTable.add(bestHandPossible);
		
		table.resetSelected();
		
		boolean gameOver = endOfGame();
		
		if(!endOfGame()) {
			// Move to next Player
			currentIdx = (currentIdx + 1 >= 4) ? 0 : currentIdx + 1;
			//table.setActivePlayer(currentIdx);
			
			table.resetSelected();
			
			if(this.playerID == currentIdx) {
				table.enable();
			} else {
				table.disable();
			}
			
			table.printMsg("");
			table.printMsg(playerList.get(currentIdx).getName() + "'s turn:");
			if(currentIdx == this.playerID) table.enable();
			else table.disable();
			table.repaint();
			
		} else {
			gameEndedNicely = true;
			
			// Display board without an active player
			table.setActivePlayer(-1);
			table.repaint();
			
			table.printMsg("");
			table.printMsg("Game ends");
			
			String gameOverMessage = "";
			
			for (CardGamePlayer player : playerList) {
				if (player.getNumOfCards() == 0) {
					table.printMsg(player.getName() + " wins the game.");
					gameOverMessage += player.getName() + " wins the game.";
				} else {
					table.printMsg(player.getName() + " has " + player.getNumOfCards() + " cards in hand.");
				}
			}
			
			gameOverMessage += "\n\n";
			for(CardGamePlayer player : playerList) {
				if (player.getNumOfCards() != 0) {
					gameOverMessage += player.getName() + " has " + player.getNumOfCards() + " cards in hand.\n";
				}
			}
			
			JOptionPane.showMessageDialog(new JFrame("GameOver"), gameOverMessage);
			
			table.repaint();
			sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
		}		
	}
	
	/**
	 * Method that checks whether the game has ended.
	 * 
	 * @return boolean value specifying whether game is over.
	 */
	public boolean endOfGame() {
		for(CardGamePlayer player : playerList) {
			if(gameStarted && player.getNumOfCards() == 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * A getter method for the instance variable 'playerID'.
	 * @return (int) value specifying the playerID of the (local/current) client.
	 */
	public int getPlayerID() {
		return playerID;
	}
	
	/**
	 * A setter method for the instance variable 'playerID'.
	 * @param playerID (int) value specifying what the playerID should be set to.
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	
	/**
	 * A getter method for the instance variable 'playerName'.
	 * @return (String) specifying the name of the player.
	 */
	public String getPlayerName() {
		return playerName;
	}
	
	/**
	 * A setter method for the instance variable 'playerName'.
	 * @param playerName (String) The value that playerName should be set to.
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	/**
	 * A getter method for the instance variable 'serverIP'.
	 * @return (String) Specifying the serverIP.
	 */
	public String getServerIP() {
		return serverIP;
	}
	
	/**
	 * A setter method for the instance variable 'serverIP'.
	 * @param serverIP (String) Value specifying what serverIP should be set to.
	 */
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	
	/**
	 * A getter method for the instance variable 'serverPort'.
	 * @return (int) Specifying the serverPort.
	 */
	public int getServerPort() {
		return serverPort;
	}
	
	/**
	 * A setter method for the instance variable 'serverPort'.
	 * @param serverPort (int) Specifies what serverPort should be set to.
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	
	/**
	 * Method for making a socket connection with the
	 * game server.
	 */
	public void makeConnection() {
		try {
			if(sock != null) {
				table.printMsg("Cannot join a server twice! You've already connected.");
				return;
			}
			sock = new Socket(serverIP, serverPort);
			oos = new ObjectOutputStream(sock.getOutputStream());
			ois = new ObjectInputStream(sock.getInputStream());
			Thread thread = new Thread(new ServerHandler());
			thread.start();
		} catch(Exception e) {
			table.printMsg("Cannot Join Server.");
			sock = null;
			table.repaint();
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Method for parsing the messages received from
	 * the game server.
	 * @param message (GameMessage) that specifies the message received from the server. 
	 */
	public synchronized void parseMessage(GameMessage message) {	
		switch(message.getType()) {
		case CardGameMessage.PLAYER_LIST:
			this.playerID = message.getPlayerID();
			if(playerName == "Guest") playerName += " " + this.playerID;
			playerList.get(this.playerID).setName(playerName);
			table.setActivePlayer(playerID);
			table.isPresent(this.playerID, true);
			for(int i = 0; i < 4; i++) {
				if(i==this.playerID)continue;
				
				String s = ((String[])message.getData())[i];
				if (s != null) {
					playerList.get(i).setName(s);
					table.isPresent(i, true);
				}
			}
			table.repaint();
			sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, playerName));
			break;
			
		case CardGameMessage.JOIN:
			
			if(message.getPlayerID() == playerID) {
				sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
			} else {
				playerList.get(message.getPlayerID()).setName((String)message.getData());
				table.isPresent(message.getPlayerID(), true);
				table.repaint();
			}
			break;
			
		case CardGameMessage.FULL:
			table.printMsg("Cannot Join. Server /" + serverIP + ":" + serverPort + " is full.");
			table.repaint();
			try {
				sock.close();
				sock = null;
			} catch(Exception e) {
				e.printStackTrace();
			}
			break;
		
		case CardGameMessage.QUIT:
			table.printMsg(playerList.get(message.getPlayerID()).getName() + " (" +(String)message.getData() + ") quit the game.\n\n");
			playerList.get(message.getPlayerID()).setName("");
			table.isPresent(message.getPlayerID(), false);
			if(!endOfGame()) {
				for(int i = 0; i < 4; i++) {
					playerList.get(i).removeAllCards();
				}
				table.disable();
			}
			table.repaint();
			sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
			break;
		
		case CardGameMessage.READY:
			table.printMsg(playerList.get(message.getPlayerID()).getName() + " is Ready.");
			table.repaint();
			break;
		
		case CardGameMessage.START:
			start((BigTwoDeck)message.getData());
			table.printMsg("Game has started.\n\n");
			table.printMsg(playerList.get(currentIdx).getName() + "'s turn:");
			if(playerID == currentIdx) table.enable();
			else table.disable();
			table.repaint();
			break;
		
		case CardGameMessage.MOVE:
			checkMove(message.getPlayerID(),(int[]) message.getData());
			table.repaint();
			break;
		
		case CardGameMessage.MSG:
			table.chatMsg((String)message.getData());
			table.repaint();
			break;
		}
	}
	
	/**
	 * A method for sending the specified message to the server.
	 * @param message (GameMessage) The message that needs to be sent to the server.
	 */
	public void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
		} catch(Exception e) {
			table.printMsg("Unable to send message to the server. Connection will be terminated.");
			table.printMsg("Try to Connect again.");
			try {
				sock.close();
			} catch(Exception ee) {
				// doing nothing
			}
			sock = null;
			table.repaint();
			System.out.println("Unable to send message to the server.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Handles receiving messages from the game server.
	 * <p>
	 * An inner class that implements the Runnable 
	 * interface.
	 * 
	 * @author Ajayveer Singh
	 *
	 */
	public class ServerHandler implements Runnable {
		
		/**
		 * Implementing the run() method from Runnable.
		 * <p>
		 * Method specifies the "job" of the thread.
		 * ie. To handle incoming messages from the
		 * game server.
		 */
		public synchronized void run() {
			GameMessage message;
			try {
				while((message = (GameMessage) ois.readObject()) != null) {
					parseMessage(message);
				}
			} catch(Exception e) {
				table.printMsg("Unable to receive message from server. Connection will be terminated.");
				table.printMsg("Try to Connect again.");
				try {
					sock.close();
				} catch(Exception ee) {
					// doing nothing
				}	
				sock = null;
				System.out.println("Unable to receive message from server.");
				e.printStackTrace();
			}
			table.repaint();
		}
	}
	
	/**
	 * A method for returning a valid hand from the list of cards passed.
	 * 
	 * @param player CardGamePlayer trying to play the hand.
	 * @param cards CardList selected by the player.
	 * @return Hand object specifying a hand that the player can play (best hand), or null if the cards form no valid combination.
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		// Create an object of every hand type and return the strongest valid one.
		ArrayList<Hand> allPossibleHands = new ArrayList<Hand>();
		allPossibleHands.add(new StraightFlush(player,cards));
		allPossibleHands.add(new Quad(player,cards));
		allPossibleHands.add(new FullHouse(player,cards));
		allPossibleHands.add(new Flush(player,cards));
		allPossibleHands.add(new Straight(player,cards));
		allPossibleHands.add(new Triple(player,cards));
		allPossibleHands.add(new Pair(player,cards));
		allPossibleHands.add(new Single(player,cards));
		
		for (Hand hand : allPossibleHands) {
			if (hand.isValid()) {
				return hand;
			}
		}
		return null;
	}
	
	/**
	 * Main method of BigTwoClient.
	 * <p>
	 * Starts game by creating instance of class.
	 * 
	 * @param args Not used.
	 */
	public static void main(String[] args) {
		BigTwoClient client = new BigTwoClient();
	}

}
